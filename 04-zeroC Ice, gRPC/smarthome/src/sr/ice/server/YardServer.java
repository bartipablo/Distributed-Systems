package sr.ice.server;

import sr.ice.server.devices.DeviceImp;
import sr.ice.server.devices.mowers.MowerImp;
import sr.ice.server.devices.sprinklers.SprinklerImp;

import java.util.ArrayList;
import java.util.List;

public class YardServer {

    public static void main(String[] args) {

        String serverIp = "127.0.0.1";
        int port = 40042;

        // init devices
        String category = "yard-devices";
        String adapter = "yard-adapter";

        List<DeviceImp> devices = new ArrayList<>();
        MowerImp mower1 = new MowerImp("Mower1");
        SprinklerImp sprinkler1 = new SprinklerImp("Sprinkler1");
        SprinklerImp sprinkler2 = new SprinklerImp("Sprinkler2");

        devices.add(mower1);
        devices.add(sprinkler1);
        devices.add(sprinkler2);

        Server server = new Server(serverIp, port, devices, category, adapter);
        server.start();
    }
}
