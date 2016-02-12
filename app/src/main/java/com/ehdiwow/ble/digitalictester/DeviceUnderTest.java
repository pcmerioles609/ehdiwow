package com.ehdiwow.ble.digitalictester;


import java.util.Arrays;

/**
 * This is a user-defined class that uses the Singleton implementation
 * This is to ensure single instantiation of the _dutIC variable
 * that holds the info about the CURRENT Device Under Test
 */

public class DeviceUnderTest {
    private static DeviceUnderTest ourInstance = new DeviceUnderTest();

    public static DeviceUnderTest getInstance() {
        return ourInstance;
    }

    private IC _dutIC;
    private int[] _readings;
    private boolean _result;

    private DeviceUnderTest() {
    }

    public void set_dutIC(IC value) {
        _dutIC = value;
    }
    public IC get_dutIC() {
        return _dutIC;
    }

    public void set_readings(int[] value) {
        _readings = value;
        _result = Arrays.equals(_readings, _dutIC.get_reference());
    }

    public int[] get_readings() { return _readings;}

    public boolean is_result() {
        return _result;
    }
}
