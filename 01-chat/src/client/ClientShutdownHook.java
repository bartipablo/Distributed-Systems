package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.net.Socket;

public class ClientShutdownHook implements Runnable {

    private final PrintWriter out;

    private final String nick;

    //sockets
    private final Socket socketTCP;
    private final DatagramSocket socketUDP;
    private final MulticastSocket multicastSocket;

    //recive threads 
    private final ReciveUDPMulticast receiveMulticastThread;
    private final ReciveUDP receiveUDPThread;
    private final ReciveTCP receiveTCPThread;

    public ClientShutdownHook(PrintWriter out, String nick, Socket socketTCP, DatagramSocket socketUDP, MulticastSocket multicastSocket, ReciveUDPMulticast receiveMulticastThread, ReciveUDP receiveUDPThread, ReciveTCP receiveTCPThread) {
        this.out = out;
        this.socketTCP = socketTCP;
        this.socketUDP = socketUDP;
        this.multicastSocket = multicastSocket;
        this.receiveMulticastThread = receiveMulticastThread;
        this.receiveUDPThread = receiveUDPThread;
        this.receiveTCPThread = receiveTCPThread;
        this.nick = nick;
    }

    @Override
    public void run() {
        out.println("[Q] " + nick + " X");

        receiveMulticastThread.shutdown();
        receiveUDPThread.shutdown();
        receiveTCPThread.shutdown();

        try {
            receiveMulticastThread.join();
            receiveUDPThread.join();
            receiveTCPThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            socketTCP.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        socketUDP.close();
        multicastSocket.close();
    }
}
