package com.ehdiwow.ble.digitalictester;


import java.util.ArrayList;
import java.util.List;

/**
 * This is a user-defined class that uses the Singleton implementation
 * This is to ensure single instantiation of the _dutICs variable
 * that holds the info about the CURRENT Device Under Test
 */

public class DevicesUnderTest {
    private static DevicesUnderTest ourInstance = new DevicesUnderTest();

    public static DevicesUnderTest getInstance() {
        return ourInstance;
    }

    private List<IcUnderTest> _dutICs = new ArrayList<IcUnderTest>();       // a List of all matched ICs
    private int _currentDutIndex = 0;                             // the index of _dutICs that the program is working on
    private String _icType;                                         // TTL, CMOS, FAIL or NONE

    private DevicesUnderTest() {
    }

    public void reset_dutIndex() { this._currentDutIndex = 0; }
    public IcUnderTest getCurrent_dutIc() { return _dutICs.get(_currentDutIndex); }
    public void next_dutIc() { this._currentDutIndex++; }
    public void add_dutIc(IcUnderTest value) { this._dutICs.add(value); }
    public List<IcUnderTest> get_dutICs() { return _dutICs; }
    public void clear_dutICs() { this._dutICs.clear(); }
    public void set_icType(String value) { this._icType = value; }
    public String get_icType() { return _icType; }
}
