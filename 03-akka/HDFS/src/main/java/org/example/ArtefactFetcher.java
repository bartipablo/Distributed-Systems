package org.example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import org.example.chuncks.ChunkId;
import org.example.chuncks.PersistedDataChunkId;

import java.io.Serializable;
import java.util.*;

public class ArtefactFetcher extends AbstractBehavior<ArtefactFetcher.Command> {

    private final Map<Integer, ActorRef<DataNode.Command>> dataNodeActors;

    private final ActorRef<Output.Command> outputActor;

    private final Map<ChunkId, String> data;

    private final Map<ChunkId, List<PersistedDataChunkId>> chunkIdToPersistedChunkId;

    private final List<PersistedDataChunkId> chunks;

    private final String artefactId;

    private int retryCount = 0;

    private final static int MAX_RETRY_COUNT = 3;

    private final static int REFRESH_INTERVAL = 5000;

    public ArtefactFetcher(
            ActorContext<Command> context,
            Map<Integer, ActorRef<DataNode.Command>> dataNodeActors,
            Map<ChunkId, String> data,
            List<PersistedDataChunkId> chunks,
            ActorRef<Output.Command> outputActor,
            String artefactId
    ) {
        super(context);

        this.dataNodeActors = dataNodeActors;
        this.outputActor = outputActor;
        this.data = data;
        this.chunks = chunks;
        this.artefactId = artefactId;

        chunkIdToPersistedChunkId = new HashMap<>();
        for (PersistedDataChunkId persistedDataChunkId : chunks) {
            if (!chunkIdToPersistedChunkId.containsKey(persistedDataChunkId.chunkId())) {
                chunkIdToPersistedChunkId.put(persistedDataChunkId.chunkId(), new ArrayList<>());
            }
            chunkIdToPersistedChunkId.get(persistedDataChunkId.chunkId()).add(persistedDataChunkId);
        }

        context.spawn(Refresher.create(REFRESH_INTERVAL, context.getSelf(), new ArtefactFetcher.Refresh()),
                "ArtefactFetcherRefresher: " + artefactId + "-" + UUID.randomUUID());
    }


    public static Behavior<Command> create (
            Map<Integer, ActorRef<DataNode.Command>> idToDataNode,
            Map<ChunkId, String> data,
            List<PersistedDataChunkId> chunks,
            ActorRef<Output.Command> outputActor,
            String artefactId
    ) {
        return Behaviors.setup(context -> new ArtefactFetcher(
                context, idToDataNode, data, chunks, outputActor, artefactId));
    }

    /***********************************
     MESSAGES PROTOCOL
     **********************************/
    public interface Command extends Serializable {}

    public record Start(
    ) implements ArtefactFetcher.Command {}


    public record ChunkReply (
            PersistedDataChunkId persistedChunkId,
            String content
    ) implements ArtefactFetcher.Command {}


    public record Refresh (
    ) implements ArtefactFetcher.Command {}
    /***********************************
     MESSAGES PROTOCOL
     **********************************/


    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(Start.class, message ->{
                    handleFetchChunks();
                    return Behaviors.same();
                })
                .onMessage(ChunkReply.class, message ->{
                    handleChunkReply(message);
                    return Behaviors.same();
                })
                .onMessage(Refresh.class, message ->{
                    if (retryCount >= MAX_RETRY_COUNT) {
                        handleErrorOccured("Timeout for fetching artefact: " + artefactId);
                    } else {
                        retryCount++;
                        handleFetchChunks();
                    }
                    return Behaviors.same();
                })
                .build();
    }


    public void handleFetchChunks() {
        if (checkIfAllChunksAreReceived()) {
            handleAllChunksReceived();
        }
        else {
            for (ChunkId chunkId : data.keySet()) {

                if (data.get(chunkId) != null) {
                    continue;
                }

                List<PersistedDataChunkId> persistedDataChunkIds = chunkIdToPersistedChunkId.get(chunkId);
                if (persistedDataChunkIds == null) {
                    handleErrorOccured("Chunk id not found: " + chunkId);
                    return;
                }

                for (PersistedDataChunkId persistedDataChunkId : persistedDataChunkIds) {
                    ActorRef<DataNode.Command> dataNodeActor = dataNodeActors.get(persistedDataChunkId.dataNodeId());
                    if (dataNodeActor == null) {
                        handleErrorOccured("Data node not found: " + persistedDataChunkId.dataNodeId());
                        return;
                    }

                    dataNodeActor.tell(
                            new DataNode.GetChunk(
                                    getContext().getSelf(),
                                    persistedDataChunkId
                            )
                    );
                }
            }
        }
    }


    public void handleChunkReply(ChunkReply message) {
        PersistedDataChunkId persistedChunkId = message.persistedChunkId();

        if (!chunkIdToPersistedChunkId.containsKey(persistedChunkId.chunkId())) {
            handleErrorOccured("Invalid received chunk: " + persistedChunkId + " for artefact: " + artefactId);
            return;
        }

        data.put(message.persistedChunkId().chunkId(), message.content());

        if (checkIfAllChunksAreReceived()) {
            handleAllChunksReceived();
        }
    }


    public boolean checkIfAllChunksAreReceived() {
        for (ChunkId chunkId : data.keySet()) {
            if (data.get(chunkId) == null) {
                return false;
            }
        }
        return true;
    }


    void handleAllChunksReceived() {
        List<ChunkId> chunkIds = new ArrayList<>(data.keySet());
        chunkIds.sort(Comparator.comparingInt(ChunkId::id));

        StringBuilder artefactValue = new StringBuilder();
        for (ChunkId chunkId : chunkIds) {
            artefactValue.append(data.get(chunkId));
        }

        outputActor.tell(
                new Output.PrintArtefact(artefactId, artefactValue.toString())
        );

        getContext().stop(getContext().getSelf());
    }


    void handleErrorOccured(String errorMessage) {
        outputActor.tell(
                new Output.PrintErrorMessages(errorMessage)
        );

        getContext().stop(getContext().getSelf());
    }
}
