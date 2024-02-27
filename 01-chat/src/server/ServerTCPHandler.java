package server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerTCPHandler {

    // mapping of nick to socket
    private final Map<String, Socket> sockets = new HashMap<>();

    // mapping of nick to print writer
    private final Map<String, PrintWriter> printWriters = new HashMap<>();

    public void addSocket(String nick, Socket socket) throws IOException {
        sockets.put(nick, socket);
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
        printWriters.put(nick, writer);
    }

    public void removeSocket(String nick) {
        Socket socketToRemove = sockets.remove(nick);
        if (socketToRemove != null) {
            PrintWriter writer = printWriters.remove(nick);
            if (writer != null) {
                writer.close();
            }
            try {
                socketToRemove.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcast(String senderNick, String message) {
        for (Map.Entry<String, Socket> entry : sockets.entrySet()) {
            String nick = entry.getKey();
            if (!nick.equals(senderNick)) {
                PrintWriter out = printWriters.get(nick);
                out.println(senderNick + ": " + message);
            }
        }
    }

}
