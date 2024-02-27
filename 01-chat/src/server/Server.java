package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {

        System.out.println("CHAT SERVER");

        // CONFIGURATION -------------------
        int portNumber = 12345;
        ServerSocket serverSocketTCP = null;
        DatagramSocket serverSocketUDP = null;
        // ---------------------------------

        try {
            // create TCP server socket
            serverSocketTCP = new ServerSocket(portNumber);
            ServerTCPHandler serverTCPHandler = new ServerTCPHandler();

            // create UDP server socket
            serverSocketUDP = new DatagramSocket(portNumber);
            Thread clientUDPThread = new Thread(new ClientUDPThread(serverTCPHandler, serverSocketUDP));
            clientUDPThread.start();

            while (true) {
                // accept client
                Socket clientSocket = serverSocketTCP.accept();

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String clientNick = in.readLine();
                System.out.println("client connected: " + clientNick);

                serverTCPHandler.addSocket(clientNick, clientSocket);
                Thread clientTCPThread = new Thread(new ClientTCPThread(clientNick, clientSocket, serverTCPHandler));
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
