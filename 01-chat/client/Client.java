import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private static String nick;

    private static final String hostName = "localhost";

    private static final int portNumber = 12345;

    private static Socket socket = null;

    private static boolean quit = false;

    public static void main(String[] args) throws IOException {

        readInputArguments(args);

        try {
            // create socket
            socket = new Socket(hostName, portNumber);

            // create thread to recive messages
            Thread receiveThread = new Thread(() -> {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

                    // sending nick to server
                    out.println(nick);

                    String input;
                    while ((input = userInput.readLine()) != null) {
                        if (input.startsWith("/quit")) {
                            out.println("[Q] " + nick + " X");
                            quit = true;
                            break;
                        }
                        out.println("[TCP] " + nick + " " + input);
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
            if (socket != null) {
                socket.close();
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
