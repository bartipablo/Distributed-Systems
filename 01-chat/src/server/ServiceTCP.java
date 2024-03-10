package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServiceTCP extends Thread {

    private boolean exit = false;

    private final String userName;

    private final Socket socket;

    private final ServerTCPHandler serverTCPHandler;

    private final ServerUDPHandler serverUDPHandler;

    public void shutdown() {
        exit = true;
    }

    public ServiceTCP(String userName, Socket socket, ServerTCPHandler serverTCPHandler,
            ServerUDPHandler serverUDPHandler) {
        this.userName = userName;
        this.socket = socket;
        this.serverTCPHandler = serverTCPHandler;
        this.serverUDPHandler = serverUDPHandler;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            String message;
            while (!exit && ((message = in.readLine()) != null)) {

                String[] parts = message.split(" ", 3);
                String messageType = parts[0];
                String nick = parts[1];
                String actualMessage = parts[2];

                if (messageType.equals("[TCP]")) {
                    serverTCPHandler.broadcast(nick, actualMessage);
                } else if (messageType.equals("[Q]")) {
                    System.out.println("client disconnected: " + userName);
                    serverTCPHandler.removeSocket(userName);
                    serverUDPHandler.removeClientInfo(nick);
                    break;
                }

            }
        } catch (IOException e) {
            System.out.println("The connection to the customer was broken: " + userName);
            serverTCPHandler.removeSocket(userName);
            serverUDPHandler.removeClientInfo(userName);
        }
    }
}