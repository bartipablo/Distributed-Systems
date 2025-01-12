package sr.ice.server.devices.sprinklers;

import Smarthome.*;
import com.zeroc.Ice.Current;
import sr.ice.server.devices.DeviceImp;

public class SprinklerImp extends DeviceImp implements Sprinkler {

    private final RadiusRange radiousRange;

    private int radius = 0;

    public SprinklerImp(String id) {
        super(id);
        radiousRange = new RadiusRange(0, 10);
    }

    public SprinklerImp(String id, RadiusRange radiusRange) {
        super(id);
        this.radiousRange = radiusRange;
    }

    @Override
    public void setRadius(int radius, Current current) throws DevicesIsInStandbyMode, InputRadiusOutOfRange {
        if (mode == Mode.STANDBY) {
            throw new DevicesIsInStandbyMode();
        }

        if (radius < radiousRange.min || radius > radiousRange.max) {
            throw new InputRadiusOutOfRange();
        }

        this.radius = radius;
    }

    @Override
    public int getRadius(Current current) throws DevicesIsInStandbyMode {
        if (mode == Mode.STANDBY) {
            throw new DevicesIsInStandbyMode();
        }

        return radius;
    }
}
