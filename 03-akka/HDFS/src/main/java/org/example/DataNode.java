package org.example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import org.example.chuncks.PersistedDataChunkId;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DataNode extends AbstractBehavior<DataNode.Command> {

    private final int id;

    private final Map<PersistedDataChunkId, String> chunks;

    public enum State {
        ACTIVE,
        PAUSED
    }

    private State state = State.ACTIVE;

    public DataNode(ActorContext<DataNode.Command> context, int id) {
        super(context);
        this.id = id;
        this.chunks = new HashMap<>();
    }

    static Behavior<DataNode.Command> create(int id) {
        return Behaviors.setup(context -> new DataNode(context, id));
    }

    /***********************************
     MESSAGES PROTOCOL
     **********************************/
    public interface Command extends Serializable {}

    public record AddChunk (
            ActorRef<DataManager.Command> sender,
            PersistedDataChunkId persistedChunkId,
            String content
    ) implements Command {}


    public record RemoveChunk (
            ActorRef<DataManager.Command> sender,
            PersistedDataChunkId persistedChunkId
    ) implements Command {}


    public record GetChunk (
            ActorRef<ArtefactFetcher.Command> sender,
            PersistedDataChunkId persistedChunkId
    ) implements Command {}


    public record ListChunks (
            ActorRef<Output.Command> sender
    ) implements Command {}


    public record Pause (
    ) implements Command {}


    public record Resume (
    ) implements Command {}


    public record GetState (
            ActorRef<Output.Command> sender
    ) implements Command {}
    /***********************************
     MESSAGES PROTOCOL
     **********************************/


    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(AddChunk.class, command -> {
                    if (state == State.ACTIVE) {
                        chunks.put(command.persistedChunkId(), command.content());
                        command.sender.tell(
                                new DataManager.ChunkConfirmation(command.persistedChunkId())
                        );
                    }
                    return Behaviors.same();
                })
                .onMessage(RemoveChunk.class, command -> {
                    if (state == State.ACTIVE) {
                        chunks.remove(command.persistedChunkId());
                        command.sender.tell(
                                new DataManager.ChunkRemovedConfirmation(command.persistedChunkId())
                        );
                    }
                    return Behaviors.same();
                })
                .onMessage(GetChunk.class, command -> {
                    if (state == State.ACTIVE) {
                        String content = chunks.get(command.persistedChunkId());
                    }
                    return Behaviors.same();
                })
                .onMessage(ListChunks.class, command -> {
                    if (state == State.ACTIVE) {
                        chunks.forEach((k, v) -> System.out.println(k + " -> " + v));
                    }
                    return Behaviors.same();
                })
                .onMessage(Pause.class, command -> {
                    state = State.PAUSED;
                    return Behaviors.same();
                })
                .onMessage(Resume.class, command -> {
                    state = State.ACTIVE;
                    return Behaviors.same();
                })
                .build();
    }
}
