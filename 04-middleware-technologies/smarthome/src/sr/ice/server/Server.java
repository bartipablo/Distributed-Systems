package sr.ice.server;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Identity;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

import Smarthome.Mower;
import sr.ice.server.devices.DeviceImp;
import sr.ice.server.devices.fridges.FridgeImp;
import sr.ice.server.devices.fridges.FridgeWithIceMakerImp;
import sr.ice.server.devices.fridges.FridgeWithProductsMonitoring;
import sr.ice.server.devices.mowers.MowerImp;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {

    private final String IP;

    private final int port;

    private final Communicator communicator;

    private final ObjectAdapter adapter;

    private final String ADAPTER_NAME = "Adapter1";

    private final List<DeviceImp> devices = new ArrayList<>();


    Server(String IP, int port, String[] iceArgs) {
        this.IP = IP;
        this.port = port;

        communicator = Util.initialize(iceArgs);

        String connectionEndpoints = "tcp -h " + IP + " -p " + port + " : udp -h " + IP + " -p " + port;
        adapter = communicator.createObjectAdapterWithEndpoints(ADAPTER_NAME, connectionEndpoints);

        initDevices();
    }


    private void initDevices() {
        FridgeImp fridge = new FridgeImp("Fridge1");
        FridgeWithIceMakerImp fridgeWithIceMaker = new FridgeWithIceMakerImp("FridgeWithIceMaker1");
        FridgeWithProductsMonitoring fridgeWithProductsMonitoring = new FridgeWithProductsMonitoring("FridgeWithProductsMonitoring1");
        MowerImp mower = new MowerImp("Mower1");

        devices.add(fridge);
        devices.add(fridgeWithIceMaker);
        devices.add(fridgeWithProductsMonitoring);
        devices.add(mower);

        adapter.add(fridge, new Identity(fridge.getId(), "fridge"));
        adapter.add(fridgeWithIceMaker, new Identity(fridgeWithIceMaker.getId(), "fridge"));
        adapter.add(fridgeWithProductsMonitoring, new Identity(fridgeWithProductsMonitoring.getId(), "fridge"));
        adapter.add(mower, new Identity(mower.getId(), "mower"));
    }

    private void listDevices() {
        for (DeviceImp device : devices) {
            System.out.println(device.getId());
        }
    }


    public void start() {
        adapter.activate();

        System.out.println("Server is running on " + IP + ":" + port);

        while (true) {
            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();

            if (command.equals("help")) {
                System.out.println("Available commands: list, exit");
            }
            else if (command.equals("list")) {
                listDevices();
            }
            else if (command.equals("exit")) {
                adapter.deactivate();
                communicator.shutdown();
                communicator.destroy();
                System.exit(0);
            }
        }
    }
}
