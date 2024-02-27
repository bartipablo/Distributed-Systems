package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServiceTCP implements Runnable {

    private final String userName;

    private final Socket socket;

    private final ServerTCPHandler serverTCPHandler;

    private final ServerUDPHandler serverUDPHandler;

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
            while ((message = in.readLine()) != null) {

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
            e.printStackTrace();
        }
    }
}