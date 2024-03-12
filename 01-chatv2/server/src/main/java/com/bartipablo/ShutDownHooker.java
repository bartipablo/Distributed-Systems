package com.bartipablo;

import com.bartipablo.threads.HandlerTCP;
import com.bartipablo.threads.HandlerUDP;
import com.bartipablo.threads.RegisteringUsers;

import java.io.BufferedReader;


public class ShutDownHooker implements Runnable {

    private final BufferedReader userInput;

    private final Connections connections;

    private final RegisteringUsers registeringUsers;

    private final HandlerUDP handlerUDP;

    public ShutDownHooker(Connections connections, RegisteringUsers registeringUsers, HandlerUDP handlerUDP, BufferedReader userInput) {
        this.connections = connections;
        this.registeringUsers = registeringUsers;
        this.handlerUDP = handlerUDP;
        this.userInput = userInput;
    }

    @Override
    public void run() {
        System.out.println("Shutting down server...");

        // shutdown threads...
        handlerUDP.shutdown();
        registeringUsers.shutdown();
        for (HandlerTCP handlerTCP : connections.getHandlerTCPs()) {
            handlerTCP.shutdown();
        }

        // close resources...
        connections.removeAllConnections();
//        try {   //Process sometimes freezes during closing this resource.
//            userInput.close();
//        } catch (IOException e) {
//            System.out.println("Error during closing user input");
//            throw new RuntimeException(e);
//        }
    }
}
