package org.example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import org.example.config.Configuration;
import org.example.persistence.Artefact;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RootManager extends AbstractBehavior<RootManager.Command> {

    private final Configuration config;

    private final Map<Integer, ActorRef<DataNode.Command>> dataNodeActors;

    private final ActorRef<Output.Command> outputActor;

    private final ActorRef<DataManager.Command> dataManagerActor;


    public RootManager(ActorContext<Command> context, Configuration config) {
        super(context);

        this.dataNodeActors = new HashMap<>();
        this.config = config;

        for (int i = 0; i < config.dataNodesQuantity(); i++) {
            ActorRef<DataNode.Command> dataNodeActor = context.spawn(DataNode.create(i), "data-node-" + i);
            dataNodeActors.put(i, dataNodeActor);
        }

        this.outputActor = context.spawn(Output.create(), "console-output");

        this.dataManagerActor = context.spawn(DataManager.create(dataNodeActors, outputActor, config.replicationQuantity()), "data-manager");
    }


    public static Behavior<RootManager.Command> create(Configuration config) {
        return Behaviors.setup(context -> new RootManager(context, config));
    }

    /***********************************
     MESSAGES PROTOCOL
     **********************************/
    public interface Command extends Serializable {}

    public record Upload (
            String artefactId,
            String artefactContent
    ) implements Command {}


    public record Delete (
            String artefactId
    ) implements Command {}


    public record Fetch (
            String artefactId
    ) implements Command {}


    public record PauseNode (
            int nodeId
    ) implements Command {}


    public record ResumeNode (
            int nodeId
    ) implements Command {}


    public record NodeStatus (
            int nodeId
    ) implements Command {}


    public record ListNode (
            int nodeId
    ) implements Command {}


    public record ArtefactStatus (
            String artefactId
    ) implements Command {}
    /***********************************
     MESSAGES PROTOCOL
     **********************************/

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(Upload.class, message -> {
                    dataManagerActor.tell(new DataManager.UploadArtefact(
                            new Artefact(message.artefactId, message.artefactContent, config.chunkSize()))
                    );
                    return Behaviors.same();
                })
                .onMessage(Delete.class, message -> {
                    dataManagerActor.tell(new DataManager.DeleteArtefact(
                            new Artefact(message.artefactId))
                    );
                    return Behaviors.same();
                })
                .onMessage(Fetch.class, message -> {
                    dataManagerActor.tell(new DataManager.FetchArtefact(
                            new Artefact(message.artefactId))
                    );
                    return Behaviors.same();
                })
                .onMessage(PauseNode.class, message -> {
                    dataNodeActors.get(message.nodeId).tell(new DataNode.Pause());
                    return Behaviors.same();
                })
                .onMessage(ResumeNode.class, message -> {
                    dataNodeActors.get(message.nodeId).tell(new DataNode.Resume());
                    return Behaviors.same();
                })
                .onMessage(NodeStatus.class, message -> {
                    dataNodeActors.get(message.nodeId).tell(new DataNode.GetState(outputActor));
                    return Behaviors.same();
                })
                .onMessage(ArtefactStatus.class, message -> {
                    // TO DO.
                    return this;
                })
                .onMessage(ListNode.class, message -> {
                    // TO DO.
                    return Behaviors.same();
                })
                .build();
    }
}
