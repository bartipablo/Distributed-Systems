package org.example;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import org.example.chuncks.ChunkId;

import java.io.Serializable;
import java.util.Map;

public class Output extends AbstractBehavior<Output.Command> {

    private Output(ActorContext<Command> context) {
        super(context);
    }


    public static Behavior<Output.Command> create() {
        return Behaviors.setup(Output::new);
    }

    /***********************************
     MESSAGES PROTOCOL
     **********************************/
    public interface Command extends Serializable {}


    public record PrintArtefact (
        String artefactId,
        String artefact
    ) implements Output.Command {}


    public record PrintListOfChunks (
        int nodeId,
        Map<ChunkId, String> chunks
    ) implements Output.Command {}


    public record PrintErrorMessages (
        String errorMessage
    ) implements Output.Command {}


    public record PrintDataNodeStatus (
        int nodeId,
        String status
    ) implements Output.Command {}


    public record PrintArtefactStatus (
        String artefactId,
        Map<ChunkId, Boolean> chunkIsRemote,
        int totalChunks,
        int confirmedChunksQuantity,
        int unconfirmedChunksQuantity,
        int deletedChunksQuantity
    ) implements Output.Command {}
    /***********************************
     MESSAGES PROTOCOL
     **********************************/


    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(PrintArtefact.class, message -> {
                    System.out.println("Artefact: " + message.artefactId);
                    System.out.println("Value: " + message.artefact);
                    return this;
                })
                .onMessage(PrintListOfChunks.class, message -> {
                    System.out.println("Node: " + message.nodeId);
                    message.chunks.forEach((chunkId, value) -> {
                        System.out.println("Chunk: " + chunkId);
                        System.out.println("Value: " + value);
                    });
                    return this;
                })
                .onMessage(PrintErrorMessages.class, message -> {
                    System.out.println("[Error]: " + message.errorMessage);
                    return this;
                })
                .onMessage(PrintDataNodeStatus.class, message -> {
                    System.out.println("Node: " + message.nodeId);
                    System.out.println("Status: " + message.status);
                    return this;
                })
                .build();
    }

}
