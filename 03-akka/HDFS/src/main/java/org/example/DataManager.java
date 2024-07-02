package org.example;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import org.example.chuncks.PersistedDataChunkId;
import org.example.config.Configuration;
import org.example.persistence.Artefact;
import org.example.persistence.PersistenceManager;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;


public class DataManager extends AbstractBehavior<DataManager.Command> {

    private final PersistenceManager persistenceManager;

    public DataManager(ActorContext<DataManager.Command> context, Configuration configuration) {
        super(context);
        this.persistenceManager = new PersistenceManager(configuration);
    }

    static Behavior<DataManager.Command> create(Configuration configuration) {
        return Behaviors.setup(context -> new DataManager(context, configuration));
    }

    /***********************************
     MESSAGES PROTOCOL
     **********************************/
    public interface Command extends Serializable {}

    public record DeleteArtefact (
       Artefact artefact
    ) implements Command, Serializable {}


    public record UploadArtefact (
        Artefact artefact
    ) implements Command, Serializable {}
    
    
    public record fetchArtefact (
        Artefact artefact
    ) implements Command, Serializable {}


    public record ChunkConfirmation (
        PersistedDataChunkId persistedChunkId
    ) implements Command, Serializable {}


    public record ChunkRemovedConfirmation (
        PersistedDataChunkId persistedChunkId
    ) implements Command, Serializable {}


    public record MonitorProcessing (
    ) implements Command, Serializable {}

    /***********************************
     MESSAGES PROTOCOL
     **********************************/
    @Override
    public Receive<DataManager.Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(DeleteArtefact.class, message -> {
                    persistenceManager.removeArtefact(message.artefact);
                    distributeResources();
                    return Behaviors.same();
                })
                .onMessage(UploadArtefact.class, message -> {
                    persistenceManager.uploadArtefact(message.artefact);
                    distributeResources();
                    return Behaviors.same();
                })
                .onMessage(fetchArtefact.class, message -> {
                    // TO DO.
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
                .onMessage(MonitorProcessing.class, message -> {
                    // TO DO.
                    return Behaviors.same();
                })
                .build();
    }
    
    
    private void distributeResources() {
        List<PersistedDataChunkId> chunksToConfirm = persistenceManager.getUnconfirmedChunks();

        for (PersistedDataChunkId chunkId : chunksToConfirm) {
            Optional<Artefact> artefact = persistenceManager.getArtefact(chunkId.chunkId().artefactId());
            if (artefact.isEmpty()) {
                throw new RuntimeException("Artefact not found");
            }

            Optional<String> content = artefact.get().getContent(chunkId.chunkId());
            if (content.isEmpty()) {
                throw new RuntimeException("Chunk content not found");
            }
            // TO DO: send message to DataNode to confirm chunk.
        }

        List<PersistedDataChunkId> chunksToDelete = persistenceManager.getDeletedChunks();

        for (PersistedDataChunkId chunkId : chunksToDelete) {
            // TO DO: send message to DataNode to confirm chunk deletion.
        }
    }


}
