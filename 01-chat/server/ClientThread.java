import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread implements Runnable {

    private final String userName;

    private final Socket socket;

    private final ServerTCPHandler serverTCPHandler;

    public ClientThread(String userName, Socket socket, ServerTCPHandler serverTCPHandler) {
        this.userName = userName;
        this.socket = socket;
        this.serverTCPHandler = serverTCPHandler;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message;
            while ((message = in.readLine()) != null) {

                String[] parts = message.split(" ", 3);
                String messageType = parts[0];
                String nick = parts[1];
                String actualMessage = parts[2];

                if (messageType.equals("[TCP]")) {
                    serverTCPHandler.broadcast(nick, actualMessage);
                } else if (messageType.equals("[Q]")) {
                    System.out.println("client disconnected: " + userName);
                    serverTCPHandler.removeSocket(userName);
                    break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}