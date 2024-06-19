package com.bartipablo.threads;

import com.bartipablo.Client;
import com.bartipablo.Communication;
import com.bartipablo.Connections;

import java.io.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class RegisteringUsers extends Thread {

    private final Connections connections;

    private final Communication communication;

    private boolean exit = false;

    public RegisteringUsers(Connections connections, Communication communication) {
        this.connections = connections;
        this.communication = communication;
    }

    public void shutdown() {
        exit = true;
    }

    @Override
    public void run() {
        while (!exit) {
            ServerSocket serverSocket = connections.getServerSocket();

            try {
                Socket socket = serverSocket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                String initialMessage = reader.readLine();
                String[] parts = initialMessage.split(" ", 3);
                String clientNick = parts[0];
                String clientAddress = parts[1];
                String clientPort = parts[2];

                InetAddress clientInetAddress = InetAddress.getByName(clientAddress);
                DatagramSocket datagramSocket = new DatagramSocket();

                if (connections.userAlreadyExist(clientNick)) {
                    writer.println("[ALREADY EXIST]");
                    socket.close();
                    reader.close();
                    writer.close();
                    System.out.println("Rejected client: " + clientNick);
                    return;
                }

                HandlerTCP handlerTCP = new HandlerTCP(communication, connections, clientNick);

                connections.addConnection(new Client(
                        clientNick,
                        clientAddress,
                        Integer.parseInt(clientPort),
                        socket,
                        reader,
                        writer,
                        datagramSocket,
                        handlerTCP
                ));

                handlerTCP.start();
                System.out.println("client connected: " + clientNick);

            } catch (IOException e) {
                if (!exit) {
                    System.out.println("Error while accepting new user");
                    e.printStackTrace();
                }
            }
        }
    }

}
