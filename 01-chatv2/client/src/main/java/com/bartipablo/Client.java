package com.bartipablo;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Client {

    private static String nick;

    public static void main(String[] args) throws IOException {
        readInputArguments(args);

        Socket socketTCP = null;
        try {
            //create sockets
            socketTCP = new Socket(Config.HOST_NAME, Config.SERVER_PORT_NUMBER);
            DatagramSocket socketUDP = new DatagramSocket();
            MulticastSocket multicastSocket = new MulticastSocket(Config.CLIENT_MULTICAST_PORT_NUMBER);
            InetAddress group = InetAddress.getByName(Config.MULTICAST_ADDRESS);
            multicastSocket.joinGroup(group);


            //create output and input stream
            PrintWriter out = new PrintWriter(socketTCP.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socketTCP.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));


            //create message receiving threads
            ReceiveUDPMulticast receiveMulticastThread = new ReceiveUDPMulticast(multicastSocket, nick);
            receiveMulticastThread.start();

            ReceiveUDP receiveUDPThread = new ReceiveUDP(socketUDP);
            receiveUDPThread.start();

            ReceiveTCP receiveTCPThread = new ReceiveTCP(in);
            receiveTCPThread.start();


            out.println(nick + " " + InetAddress.getLocalHost().getHostAddress() + " " + socketUDP.getLocalPort());
            printState();

            //init shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(new ClientShutdownHook(
                    receiveTCPThread,
                    receiveUDPThread,
                    receiveMulticastThread,
                    socketTCP,
                    socketUDP,
                    multicastSocket,
                    out,
                    in,
                    stdIn
            )));

            // commands handling
            String input;
            while ((input = stdIn.readLine()) != null) {
                if (input.startsWith("/U ")) {
                    String messageToSend = input.substring(3);
                    byte[] sendData = ("[UDP] " + nick + " " + messageToSend).getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                            InetAddress.getByName(Config.HOST_NAME), Config.SERVER_PORT_NUMBER);
                    socketUDP.send(sendPacket);
                }
                else if (input.startsWith("/M")) {
                    String messageToSend = input.substring(3);
                    byte[] sendData = (nick + ": " + messageToSend).getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                            InetAddress.getByName(Config.MULTICAST_ADDRESS), Config.CLIENT_MULTICAST_PORT_NUMBER);
                    multicastSocket.send(sendPacket);
                }
                else if (input.startsWith("/ASCII")) {
                    List<String> ascii = readASCII();
                    for (String line : ascii) {
                        out.println("[TCP] " + nick + " " + line);
                    }
                }
                else if (input.startsWith("/Q")) {
                    break;
                }
                else {
                    out.println("[TCP] " + nick + " " + input);
                }
            }
        } catch (IOException e) {
            if (socketTCP != null) {
                System.err.println("Error while sending message:");
                e.printStackTrace();
            } else {
                System.err.println("The connection to the server could not be established.");
            }
        }

        System.exit(0);
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


    private static List<String> readASCII() {
        BufferedReader reader = null;
        List<String> lines = new ArrayList<>();
        try {
            String filePath = "../../ascii/ascii";

            reader = new BufferedReader(new FileReader(filePath));

            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lines;
    }

    private static void printState() {
        System.out.println("<message> - send message to the server using TCP");
        System.out.println("/U <message> - send message to the server using UDP");
        System.out.println("/M <message> - send message to the server using multicast");
        System.out.println("/ASCII - send ASCII art to the server using TCP");
        System.out.println("/Q - quit the chat");
    }
}