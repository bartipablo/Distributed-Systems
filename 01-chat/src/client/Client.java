package client;

import java.io.*;
import java.net.*;

public class Client {

    private static String nick;
    private static final String HOST_NAME = "localhost";
    private static final String MULTICAST_ADDRESS = "239.0.0.1";
    private static final int SERVER_PORT_NUMBER = 12345;
    private static final int CLIENT_MULTICAST_PORT_NUMBER = 12346;

    public static void main(String[] args) {
        readInputArguments(args);

        try {
            Socket socketTCP = new Socket(HOST_NAME, SERVER_PORT_NUMBER);
            DatagramSocket socketUDP = new DatagramSocket();
            MulticastSocket multicastSocket = new MulticastSocket(CLIENT_MULTICAST_PORT_NUMBER);

            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            multicastSocket.joinGroup(group);

            ReciveUDPMulticast receiveMulticastThread = new ReciveUDPMulticast(multicastSocket);
            receiveMulticastThread.start();

            ReciveUDP receiveUDPThread = new ReciveUDP(socketUDP);
            receiveUDPThread.start();

            ReciveTCP receiveTCPThread = new ReciveTCP(socketTCP);
            receiveTCPThread.start();

            PrintWriter out = new PrintWriter(new OutputStreamWriter(socketTCP.getOutputStream(), "UTF-8"), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            out.println(nick + " " + InetAddress.getLocalHost().getHostAddress() + " " + socketUDP.getLocalPort());

            //init shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(new ClientShutdownHook(out, nick, socketTCP, socketUDP, multicastSocket, receiveMulticastThread, receiveUDPThread, receiveTCPThread)));

            String input;
            while ((input = userInput.readLine()) != null) {
                if (input.startsWith("/U ")) {
                    String messageToSend = input.substring(3);
                    byte[] sendData = ("[UDP] " + nick + " " + messageToSend).getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                            InetAddress.getByName(HOST_NAME), SERVER_PORT_NUMBER);
                    socketUDP.send(sendPacket);
                } else if (input.startsWith("/M")) {
                    String messageToSend = input.substring(3);
                    byte[] sendData = (nick + ": " + messageToSend).getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                            InetAddress.getByName(MULTICAST_ADDRESS), CLIENT_MULTICAST_PORT_NUMBER);
                    multicastSocket.send(sendPacket);
                } else {
                    out.println("[TCP] " + nick + " " + input);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readInputArguments(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java ClientTCP <nick>");
            System.exit(1);
        }
        nick = args[0].trim();
        if (nick.isEmpty()) {
            System.err.println("Nick cannot be empty.");
            System.exit(1);
        }
    }
}