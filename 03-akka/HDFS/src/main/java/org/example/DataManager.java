package org.example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import org.example.chuncks.ChunkId;
import org.example.chuncks.PersistedDataChunkId;
import org.example.persistence.Artefact;
import org.example.persistence.PersistenceManager;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


public class DataManager extends AbstractBehavior<DataManager.Command> {

    private final PersistenceManager persistenceManager;

    private final Map<Integer, ActorRef<DataNode.Command>> dataNodeActors;

    private final ActorRef<Output.Command> outputActor;

    private DataManager(ActorContext<DataManager.Command> context,
                       Map<Integer, ActorRef<DataNode.Command>> dataNodeActors,
                       ActorRef<Output.Command> outputActor,
                       int replicationQuantity) {
        super(context);
        this.persistenceManager = new PersistenceManager(dataNodeActors.keySet().stream().toList(), replicationQuantity);
        this.dataNodeActors = dataNodeActors;
        this.outputActor = outputActor;

        context.spawn(Refresher.create(20_000, context.getSelf(), new Refresh()), "DataManagerRefresher");
    }


    static Behavior<DataManager.Command> create(Map<Integer, ActorRef<DataNode.Command>> dataNodeActors,
                                                ActorRef<Output.Command> outputActor,
                                                int replicationQuantity) {
        return Behaviors.setup(context -> new DataManager(context, dataNodeActors, outputActor, replicationQuantity));
    }

    /***********************************
     MESSAGES PROTOCOL
     **********************************/
    public interface Command extends Serializable {}

    public record DeleteArtefact (
       Artefact artefact
    ) implements Command {}


    public record UploadArtefact (
        Artefact artefact
    ) implements Command {}
    
    
    public record FetchArtefact (
        Artefact artefact
    ) implements Command {}


    public record ChunkConfirmation (
        PersistedDataChunkId persistedChunkId
    ) implements Command {}


    public record ChunkRemovedConfirmation (
        PersistedDataChunkId persistedChunkId
    ) implements Command {}


    public record ArtefactStatus (
        Artefact artefact
    ) implements Command {}


    public record Refresh(
    ) implements Command {}

    /***********************************
     MESSAGES PROTOCOL
     **********************************/
    @Override
    public Receive<DataManager.Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(DeleteArtefact.class, message -> {
                    persistenceManager.removeArtefact(message.artefact);
                    List<PersistedDataChunkId> deletedChunks = persistenceManager.getDeletedChunksByArtefactId(message.artefact.getId());
                    distributeDeletedChunks(deletedChunks);
                    return Behaviors.same();
                })
                .onMessage(UploadArtefact.class, message -> {
                    persistenceManager.uploadArtefact(message.artefact);
                    List<PersistedDataChunkId> deletedChunks = persistenceManager.getDeletedChunksByArtefactId(message.artefact.getId());
                    List<PersistedDataChunkId> unconfirmedChunks = persistenceManager.getUnconfirmedChunksByArtefactId(message.artefact.getId());
                    distributeDeletedChunks(deletedChunks);
                    distributeUnconfirmedChunks(unconfirmedChunks);
                    return Behaviors.same();
                })
                .onMessage(FetchArtefact.class, message -> {
                    fetchArtefact(message.artefact);
                    return Behaviors.same();
                })
                .onMessage(ChunkConfirmation.class, message -> {
                    persistenceManager.confirmChunk(message.persistedChunkId);
                    return Behaviors.same();
                })
                .onMessage(ChunkRemovedConfirmation.class, message -> {
                    persistenceManager.confirmDeleteChunk(message.persistedChunkId);
                    return Behaviors.same();
                })
                .onMessage(Refresh.class, message -> {
                    distributeAll();
                    return Behaviors.same();
                })
                .onMessage(ArtefactStatus.class, message -> {
                    artefactStatusHandler(message.artefact.getId());
                    return Behaviors.same();
                })
                .build();
    }


    private void distributeUnconfirmedChunks(List<PersistedDataChunkId> chunks) {
        for (PersistedDataChunkId chunkId : chunks) {
            Optional<Artefact> artefact = persistenceManager.getArtefact(chunkId.chunkId().artefactId());
            if (artefact.isEmpty()) {
                throw new RuntimeException("Artefact not found");
            }

            Optional<String> content = artefact.get().getContent(chunkId.chunkId());
            if (content.isEmpty()) {
                throw new RuntimeException("Chunk content not found");
            }

            var dataNodeActor = dataNodeActors.get(chunkId.dataNodeId());
            dataNodeActor.tell(
                    new DataNode.AddChunk(getContext().getSelf() ,chunkId, content.get())
            );
        }
    }


    private void distributeDeletedChunks(List<PersistedDataChunkId> deletedChunks) {
        for (PersistedDataChunkId chunkId : deletedChunks) {
            var dataNodeActor = dataNodeActors.get(chunkId.dataNodeId());
            dataNodeActor.tell(
                    new DataNode.RemoveChunk(getContext().getSelf(), chunkId)
            );
        }
    }


    private void distributeAll() {
        distributeUnconfirmedChunks(persistenceManager.getUnconfirmedChunks());
        distributeDeletedChunks(persistenceManager.getDeletedChunks());
    }


    private void artefactStatusHandler(String artefactId) {
        Optional<Artefact> optionalArtefact = persistenceManager.getArtefact(artefactId);
        if (optionalArtefact.isEmpty()) {
            outputActor.tell(new Output.PrintErrorMessages("Artefact not found."));
            return;
        }

        Artefact artefact = optionalArtefact.get();
        Set<ChunkId> chunkIds = artefact.getChunksIds();

        int totalChunks = chunkIds.size();
        int confirmedChunks = persistenceManager.getConfirmedChunksByArtefactId(artefact.getId()).size();
        int unconfirmedChunks = persistenceManager.getUnconfirmedChunksByArtefactId(artefact.getId()).size();
        int deletedChunks = persistenceManager.getDeletedChunksByArtefactId(artefact.getId()).size();

        Map<ChunkId, Boolean> chunkIsRemote = chunkIds.stream()
                .collect(Collectors.toMap(
                        chunkId -> chunkId,
                        chunkId -> artefact.getContent(chunkId).isEmpty()
                ));

        outputActor.tell(new Output.PrintArtefactStatus(artefact.getId(), chunkIsRemote, totalChunks, confirmedChunks, unconfirmedChunks, deletedChunks));
    }


    private void fetchArtefact(Artefact artefact) {
        var artefactFetcher = getContext().spawn(
                ArtefactFetcher.create(
                        dataNodeActors,
                        persistenceManager.getArtefactContents(artefact.getId()),
                        persistenceManager.getConfirmedChunksByArtefactId(artefact.getId()),
                        outputActor,
                        artefact.getId()),
                "ArtefactFetcher-" + artefact.getId() + "-" + UUID.randomUUID().toString());

        artefactFetcher.tell(new ArtefactFetcher.Start());
    }


}
