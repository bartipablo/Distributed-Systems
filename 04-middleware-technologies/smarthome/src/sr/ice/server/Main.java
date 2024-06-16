package sr.ice.server;

import sr.ice.server.exceptions.InvalidIPAddress;
import sr.ice.server.exceptions.InvalidPortNumber;

import java.util.Arrays;

public class Main {

	public static void main(String[] args) {

		if (args.length < 2) {
			System.out.println("Necessary arguments: <server-ip> <server-port> <args>");
			System.exit(1);
		}

		try {
			String serverIp = args[0];
			int port = Integer.parseInt(args[1]);

			String[] iceArgs = Arrays.copyOfRange(args, 2, args.length);

			validateIPFormat(serverIp);
			validatePort(port);

			Server server = new Server(serverIp, port, iceArgs);
			server.start();

		} catch (InvalidIPAddress | InvalidPortNumber | NumberFormatException e) {
			System.out.println(e.getMessage());

			System.out.println("expected args: <server-ip> <port> <ice-args>");
			System.exit(2);
		}
	}

	private static void validateIPFormat(String ip) {
		if (!ip.matches("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$")) {
			throw new InvalidIPAddress("Invalid IP address format");
		}
	}

	private static void validatePort(int port) {
		if (port < 49152 && port > 65535) {
			throw new InvalidPortNumber("Invalid port number");
		}
	}
}