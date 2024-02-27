package server;

public class ClientInfo {

    private final String nick;

    private final String address;

    private final int port;

    public ClientInfo(String nick, String address, int port) {
        this.nick = nick;
        this.address = address;
        this.port = port;
    }

    public String getNick() {
        return nick;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
}
