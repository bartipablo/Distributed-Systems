package server;

import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.List;

public class ServerShutdownHook implements Runnable {

    private final ServerTCPHandler serverTCPHandler;

    private final ServerSocket serverSocketTCP;

    private final DatagramSocket serverSocketUDP;

    private final List<ServiceTCP> clientThreads;


    public ServerShutdownHook(ServerTCPHandler serverTCPHandler, ServerSocket serverSocketTCP, DatagramSocket serverSocketUDP, List<ServiceTCP> clientThreads) {
        this.serverTCPHandler = serverTCPHandler;
        this.serverSocketTCP = serverSocketTCP;
        this.serverSocketUDP = serverSocketUDP;
        this.clientThreads = clientThreads;
    }

    @Override
    public void run() {
        System.out.println("Server shutdown");

        System.out.println("All clients closed");


        serverTCPHandler.removeAllSockets();

        try {
            serverSocketTCP.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        serverSocketUDP.close();
    }
}
