package com.bartipablo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.net.Socket;


public class ClientShutdownHook implements Runnable {

    private final ReceiveTCP receiveTCP;

    private final ReceiveUDP receiveUDP;

    private final ReceiveUDPMulticast receiveUDPMulticast;

    private final Socket socketTCP;

    private final DatagramSocket socketUDP;

    private final MulticastSocket multicastSocket;

    private final PrintWriter out;

    private final BufferedReader in;

    private final BufferedReader stdIn;

    public ClientShutdownHook(
            ReceiveTCP receiveTCP,
            ReceiveUDP receiveUDP,
            ReceiveUDPMulticast receiveUDPMulticast,
            Socket socketTCP,
            DatagramSocket socketUDP,
            MulticastSocket multicastSocket,
            PrintWriter out,
            BufferedReader in,
            BufferedReader stdIn) {
        this.receiveTCP = receiveTCP;
        this.receiveUDP = receiveUDP;
        this.receiveUDPMulticast = receiveUDPMulticast;
        this.socketTCP = socketTCP;
        this.socketUDP = socketUDP;
        this.multicastSocket = multicastSocket;
        this.out = out;
        this.in = in;
        this.stdIn = stdIn;
    }

    @Override
    public void run() {
        System.out.println("Closing client...");
        out.println("[Q]");

        receiveTCP.shutdown();
        receiveUDP.shutdown();
        receiveUDPMulticast.shutdown();

        socketUDP.close();
        multicastSocket.close();
        try {
            socketTCP.close();
            in.close();
            //stdIn.close();  process sometimes freezes during closing this resource
        } catch (IOException e) {
            System.err.println("Error while closing TCP socket");
            e.printStackTrace();
        }

        out.close();
    }
}