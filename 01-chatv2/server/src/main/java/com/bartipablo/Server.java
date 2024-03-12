package com.bartipablo;


import com.bartipablo.threads.HandlerUDP;
import com.bartipablo.threads.RegisteringUsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;

public class Server {

    public static void main(String[] args) {
        try{
            Connections connections = new Connections();
            Communication communication = new Communication(connections);

            //init UDP server socket and handler thread
            DatagramSocket serverSocketUDP = new DatagramSocket(Config.SERVER_PORT);
            HandlerUDP handlerUDP = new HandlerUDP(communication, serverSocketUDP);
            handlerUDP.start();

            //init registering users thread to init TCP connection
            RegisteringUsers registeringUsers = new RegisteringUsers(connections, communication);
            registeringUsers.start();


            System.out.println("Running server...");
            System.out.println("Server is running on port " + Config.SERVER_PORT);

            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            //register shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(new ShutDownHooker(connections, registeringUsers, handlerUDP, userInput)));

            String input;

            while ((input = userInput.readLine()) != null) {
                if (input.startsWith("help")) {
                    System.out.println("Available commands:");
                    System.out.println("'quit' close server");
                    System.out.println("'list' list all connected users");
                }
                else if (input.startsWith("quit")) {
                    System.out.println("Closing server...");
                    break;
                }
                else if (input.startsWith("list")) {
                    System.out.println("Connected users:");
                    connections.getConnectedClientsNick().forEach(System.out::println);
                }
                else {
                    System.out.println("Unknown command");
                    System.out.println("Type 'help' to see available commands");
                }
            }
            System.exit(0);
        } catch (IOException e) {
            System.err.println("Error while running server");
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }

}