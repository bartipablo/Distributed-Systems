package com.bartipablo.threads;

import com.bartipablo.Communication;
import com.bartipablo.Connections;

import java.io.BufferedReader;
import java.io.IOException;

public class HandlerTCP extends Thread {

    private final String nick;

    private final Communication communication;

    private final Connections connections;

    private boolean exit = false;

    public HandlerTCP(Communication communication, Connections connections, String nick) {
        this.communication = communication;
        this.connections = connections;
        this.nick = nick;
    }

    public void shutdown() {
        exit = true;
    }

    @Override
    public void run() {
        BufferedReader in = connections.getClient(nick).getReader();
        String message;
        try {
            while ((!exit && ((message = in.readLine()) != null))) {
                if (message.equals("[Q]")) {
                    Thread.sleep(20);
                    connections.removeConnection(nick);
                    break;
                } else {
                    String[] parts = message.split(" ", 3);
                    String actualMessage = parts[2];
                    communication.broadcastTCP(nick, actualMessage);
                }
            }
        } catch (IOException | InterruptedException e) {
            if (!exit) {
                System.out.println("Error while receiving TCP message");
                e.printStackTrace();
                connections.removeConnection(nick);
            }
        }
    }
}

