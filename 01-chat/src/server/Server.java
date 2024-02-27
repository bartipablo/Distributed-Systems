package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int SERVER_PORT_NUMBER = 12345;

    public static void main(String[] args) throws IOException {

        System.out.println("CHAT SERVER");

        ServerSocket serverSocketTCP = null;
        DatagramSocket serverSocketUDP = null;

        try {
            // create TCP server socket
            serverSocketTCP = new ServerSocket(SERVER_PORT_NUMBER);
            ServerTCPHandler serverTCPHandler = new ServerTCPHandler();

            // create UDP server socket
            serverSocketUDP = new DatagramSocket(SERVER_PORT_NUMBER);
            ServerUDPHandler serverUDPHandler = new ServerUDPHandler();
            
            Thread serviceUDP = new Thread(new ServiceUDP(serverSocketUDP, serverUDPHandler));
            serviceUDP.start();

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
                
                Thread clientTCPThread = new Thread(
                        new ServiceTCP(clientNick, clientSocket, serverTCPHandler, serverUDPHandler));
                clientTCPThread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocketTCP != null) {
                serverSocketTCP.close();
            }
        }
    }

}
