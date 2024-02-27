package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private static String nick;

    private static final String hostName = "localhost";

    private static final int portNumber = 12345;

    private static Socket socketTCP = null;

    private static DatagramSocket socketUDP = null;

    private static boolean quit = false;

    public static void main(String[] args) throws IOException {

        readInputArguments(args);

        try {

            // create TCP socket
            socketTCP = new Socket(hostName, portNumber);

            // create UDP socket
            socketUDP = new DatagramSocket();

            // create thread to recive messages
            Thread receiveThread = new Thread(() -> {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socketTCP.getInputStream()));
                    String message;
                    while (!quit && ((message = in.readLine()) != null)) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiveThread.start();

            // create thread to send messages
            Thread sendThread = new Thread(() -> {
                try {
                    PrintWriter out = new PrintWriter(socketTCP.getOutputStream(), true);
                    BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

                    // sending nick to server
                    out.println(nick);

                    String input;
                    while ((input = userInput.readLine()) != null) {
                        if (input.startsWith("/quit")) {
                            out.println("[Q] " + nick + " X");
                            quit = true;
                            break;
                        } else if (input.startsWith("/U ")) {
                            String messageToSend = input.substring(3);

                            byte[] sendData = ("[UDP] " + nick + " " + messageToSend).getBytes();
                            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                                    InetAddress.getByName(hostName), portNumber);
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

            receiveThread.join();
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
