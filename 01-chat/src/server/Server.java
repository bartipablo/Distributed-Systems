package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static final int SERVER_PORT_NUMBER = 12345;

    public static void main(String[] args) throws IOException {

        System.out.println("CHAT SERVER");

        try {
            // create TCP server socket
            ServerSocket serverSocketTCP = new ServerSocket(SERVER_PORT_NUMBER);
            ServerTCPHandler serverTCPHandler = new ServerTCPHandler();

            // create UDP server socket
            DatagramSocket serverSocketUDP = new DatagramSocket(SERVER_PORT_NUMBER);
            ServerUDPHandler serverUDPHandler = new ServerUDPHandler();
            
            Thread serviceUDP = new Thread(new ServiceUDP(serverSocketUDP, serverUDPHandler));
            serviceUDP.start();

            //listening for new clients
            List<ServiceTCP> clientThreads = new ArrayList<>();

            // init shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(new ServerShutdownHook(serverTCPHandler, serverSocketTCP, serverSocketUDP, clientThreads)));

            while (true) {
                // accept client
                Socket clientSocket = serverSocketTCP.accept();

                // reading ClientInfo
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String initialMessage = in.readLine();

                String[] parts = initialMessage.split(" ", 3);
                String clientNick = parts[0];
                String clientAddress = parts[1];
                String clientPort = parts[2];

                System.out.println("client connected: " + clientNick);

                serverUDPHandler.addClientInfo(new ClientInfo(clientNick, clientAddress, Integer.parseInt(clientPort)));
                serverTCPHandler.addSocket(clientNick, clientSocket);
                
                ServiceTCP clientTCPThread = new ServiceTCP(clientNick, clientSocket, serverTCPHandler, serverUDPHandler);
                clientThreads.add(clientTCPThread);
                clientTCPThread.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

}
