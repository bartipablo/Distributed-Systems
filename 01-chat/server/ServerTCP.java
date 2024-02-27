import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTCP {

    public static void main(String[] args) throws IOException {

        System.out.println("CHAT SERVER");

        // CONFIGURATION -------------------
        int portNumber = 12345;
        ServerSocket serverSocket = null;
        // ---------------------------------

        try {
            // create socket
            serverSocket = new ServerSocket(portNumber);

            ServerTCPHandler serverTCPHandler = new ServerTCPHandler();

            while (true) {
                // accept client
                Socket clientSocket = serverSocket.accept();

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String clientNick = in.readLine();
                System.out.println("client connected: " + clientNick);

                serverTCPHandler.addSocket(clientNick, clientSocket);
                Thread clientThread = new Thread(new ClientThread(clientNick, clientSocket, serverTCPHandler));
                clientThread.start();

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }

}
