package sr.ice.server.devices.mowers;

import Smarthome.*;
import com.zeroc.Ice.Current;
import sr.ice.server.devices.DeviceImp;

public class MowerImp extends DeviceImp implements Mower {

    int speed = 0;

    private final SpeedRange speedRange;

    double batteryLevel = 100;

    public MowerImp(String id) {
        super(id);
        speedRange = new SpeedRange(0, 5);
    }

    public MowerImp(String id, SpeedRange speedRange) {
        super(id);
        this.speedRange = speedRange;
    }

    public MowerImp(String id, SpeedRange speedRange, double batteryLevel) {
        super(id);
        this.speedRange = speedRange;
        this.batteryLevel = batteryLevel;
    }

    @Override
    public void setMode(Mode mode, Current current) {
        if (mode == Mode.STANDBY) {
            speed = 0;
        }

        super.setMode(mode, current);
    }

    @Override
    public Position getPosition(Current current) throws DevicesIsInStandbyMode {
        if (mode == Mode.STANDBY) {
            throw new DevicesIsInStandbyMode();
        }

        return new Position(0, 0);
    }

    @Override
    public void setSpeed(int speed, Current current) throws DevicesIsInStandbyMode, InputSpeedOutOfRange, BatteryLvlLow {
        if (mode == Mode.STANDBY) {
            throw new DevicesIsInStandbyMode();
        }

        if (speed < speedRange.min || speed > speedRange.max) {
            throw new InputSpeedOutOfRange();
        }

        if (batteryLevel == 0) {
            throw new BatteryLvlLow();
        }

        this.speed = speed;
    }

    @Override
    public int getSpeed(Current current) throws DevicesIsInStandbyMode {
        if (mode == Mode.STANDBY) {
            throw new DevicesIsInStandbyMode();
        }

        return speed;
    }

    @Override
    public double getBatteryLevel(Current current) throws DevicesIsInStandbyMode {
        if (mode == Mode.STANDBY) {
            throw new DevicesIsInStandbyMode();
        }

        return batteryLevel;
    }
}
