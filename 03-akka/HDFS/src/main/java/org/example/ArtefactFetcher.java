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
import java.util.Map;
import java.util.Set;

public class ArtefactFetcher extends AbstractBehavior<ArtefactFetcher.Command> {

    private final Map<Integer, ActorRef<DataNode.Command>> idToDataNode;

    private final Map<ChunkId, String> data;

    private final Set<PersistedDataChunkId> chunks;

    public ArtefactFetcher(
            ActorContext<Command> context,
            Map<Integer, ActorRef<DataNode.Command>> idToDataNode,
            Map<ChunkId, String> data,
            Set<PersistedDataChunkId> chunks
    ) {
        super(context);

        this.idToDataNode = idToDataNode;
        this.data = data;
        this.chunks = chunks;
    }


    public static Behavior<Command> create (
            Map<Integer, ActorRef<DataNode.Command>> idToDataNode,
            Map<ChunkId, String> data,
            Set<PersistedDataChunkId> chunks
    ) {
        return Behaviors.setup(context -> new ArtefactFetcher(context, idToDataNode, data, chunks));
    }

    /***********************************
     MESSAGES PROTOCOL
     **********************************/
    public interface Command extends Serializable {}

    public record Start(
    ) implements ArtefactFetcher.Command {}


    public record ChunkReply (
            ChunkId chunkId,
            String content
    ) implements ArtefactFetcher.Command {}


    public record ProcessControl (
    ) implements ArtefactFetcher.Command {}
    /***********************************
     MESSAGES PROTOCOL
     **********************************/


    @Override
    public Receive<Command> createReceive() {
        return null;
    }

}
