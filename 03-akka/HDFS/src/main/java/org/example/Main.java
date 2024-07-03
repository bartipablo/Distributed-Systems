package org.example;

import akka.actor.typed.ActorSystem;
import org.example.config.Configuration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {

        final int chunkSize = 1024;
        final int replicationQuantity = 3;
        final int dataNodesQuantity = 4;

        Configuration config = new Configuration(chunkSize, replicationQuantity, dataNodesQuantity);

        ActorSystem<RootManager.Command> actorSystem = ActorSystem.create(RootManager.create(config), "Root");

        Main main = new Main(actorSystem);
        main.startCommandListener();
    }

    private final String uploadCommand = "upload\\s+(\\w+)\\s+(\\w+)";
    private final String deleteCommand = "delete\\s+(\\w+)";
    private final String fetchCommand = "fetch\\s+(\\w+)";
    private final String pauseCommand = "pause\\s+(\\d+)";
    private final String resumeCommand = "resume\\s+(\\d+)";
    private final String nodeStatusCommand = "nodestatus\\s+(\\d+)";
    private final String artefactStatusCommand = "artefactstatus\\s+(\\w+)";
    private final String listNodeCommand = "list\\s+(\\d+)";
    private final String helpCommand = "help";

    private final ActorSystem<RootManager.Command> actorSystem;


    public Main(ActorSystem<RootManager.Command> actorSystem) {
        this.actorSystem = actorSystem;
    }

    private void startCommandListener() {
        while (true) {
            String command = System.console().readLine();
            if (command != null) {
                if (command.matches(uploadCommand)) {
                    String[] args = extractArguments(command, uploadCommand);
                    String artifactName = args[0];
                    String destinationNode = args[1];
                    actorSystem.tell(new RootManager.Upload(artifactName, destinationNode));
                }
                else if (command.matches(deleteCommand)) {
                    String artifactName = extractArgument(command, deleteCommand);
                    actorSystem.tell(new RootManager.Delete(artifactName));
                }
                else if (command.matches(fetchCommand)) {
                    String artifactName = extractArgument(command, fetchCommand);
                    actorSystem.tell(new RootManager.Fetch(artifactName));
                }
                else if (command.matches(pauseCommand)) {
                    int nodeId = Integer.parseInt(extractArgument(command, pauseCommand));
                    actorSystem.tell(new RootManager.PauseNode(nodeId));
                }
                else if (command.matches(resumeCommand)) {
                    int nodeId = Integer.parseInt(extractArgument(command, resumeCommand));
                    actorSystem.tell(new RootManager.ResumeNode(nodeId));
                }
                else if (command.matches(nodeStatusCommand)) {
                    int nodeId = Integer.parseInt(extractArgument(command, nodeStatusCommand));
                    actorSystem.tell(new RootManager.NodeStatus(nodeId));
                }
                else if (command.matches(artefactStatusCommand)) {
                    String artifactName = extractArgument(command, artefactStatusCommand);
                    actorSystem.tell(new RootManager.ArtefactStatus(artifactName));
                }
                else if (command.matches(listNodeCommand)) {
                    int nodeId = Integer.parseInt(extractArgument(command, listNodeCommand));
                    actorSystem.tell(new RootManager.ListNode(nodeId));
                }
                else if (command.matches(helpCommand)) {
                    printHelp();
                }
                else {
                    System.out.println("Unrecognized command: " + command);
                }
            }
        }
    }


    private String[] extractArguments(String command, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(command);
        if (m.find()) {
            String[] args = new String[m.groupCount()];
            for (int i = 0; i < args.length; i++) {
                args[i] = m.group(i + 1);
            }
            return args;
        } else {
            throw new IllegalArgumentException("Command does not match expected pattern: " + pattern);
        }
    }


    private String extractArgument(String command, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(command);
        if (m.find()) {
            return m.group(1);
        } else {
            throw new IllegalArgumentException("Command does not match expected pattern: " + pattern);
        }
    }


    private void printHelp() {
        System.out.println("Available commands:");
        System.out.println("upload <artifactName> <destinationNode>");
        System.out.println("delete <artifactName>");
        System.out.println("fetch <artifactName>");
        System.out.println("pause <nodeId>");
        System.out.println("resume <nodeId>");
        System.out.println("nodestatus <nodeId>");
        System.out.println("artefactstatus <artifactName>");
        System.out.println("list <nodeId>");
    }
}
