package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private static String nick;

    private static final String hostName = "localhost";

    private static final int serverPortNumber = 12345;

    private static Socket socketTCP = null;

    private static DatagramSocket socketUDP = null;

    private static boolean quit = false;

    public static void main(String[] args) throws IOException {

        readInputArguments(args);

        try {

            // create TCP socket
            socketTCP = new Socket(hostName, serverPortNumber);

            // create UDP socket
            socketUDP = new DatagramSocket();

            // create thread to receive UDP messages
            Thread receiveUDPThread = new Thread(() -> {
                try {
                    byte[] receiveData = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                    while (!quit) {
                        socketUDP.receive(receivePacket);
                        String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength(),
                                "UTF-8");
                        System.out.println(receivedMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiveUDPThread.start();

            // create thread to recive TCP messages
            Thread receiveTCPThread = new Thread(() -> {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socketTCP.getInputStream(), "UTF-8"));
                    String message;
                    while (!quit && ((message = in.readLine()) != null)) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiveTCPThread.start();

            // create thread to send messages
            Thread sendThread = new Thread(() -> {
                try {
                    PrintWriter out = new PrintWriter(new OutputStreamWriter(socketTCP.getOutputStream(), "UTF-8"),
                            true);
                    BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

                    // sending nick, address and UDP port to server
                    out.println(
                            nick + " " + InetAddress.getLocalHost().getHostAddress() + " " + socketUDP.getLocalPort());

                    String input;
                    while ((input = userInput.readLine()) != null) {
                        if (input.startsWith("/quit")) {
                            out.println("[Q] " + nick + " X");
                            System.exit(0);
                        } else if (input.startsWith("/U ")) {
                            String messageToSend = input.substring(3);

                            byte[] sendData = ("[UDP] " + nick + " " + messageToSend).getBytes();
                            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                                    InetAddress.getByName(hostName), serverPortNumber);
                            socketUDP.send(sendPacket);
                        } else {
                            out.println("[TCP] " + nick + " " + input);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            sendThread.start();

            receiveTCPThread.join();
            receiveUDPThread.join();
            sendThread.join();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socketTCP != null) {
                socketTCP.close();
            }
        }
    }

    private static void readInputArguments(String[] args) {
        // sign nick
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
