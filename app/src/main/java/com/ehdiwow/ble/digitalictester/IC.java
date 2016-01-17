package com.ehdiwow.ble.digitalictester;

/**
 * This is a user-defined struct/class to define an IC object with its properties/members *
 */

public class IC {
    private String _name;
    private int _power;
    private int _ground;
    private int _inputCount;
    private int _outputCount;
    private int _testCount;

    private int[] _inputPins;
    private int[] _outputPins;
    private int[] _testInputs;
    private int[] _reference;

    public IC(String NAME, int VCC, int GND, int INPCT, int OUTCT, int TSTCT,
              int[] INP, int[] OUT, int[] TSTINP, int[] REF) {
        _name = NAME;
        _power = VCC;
        _ground = GND;
        _inputCount = INPCT;
        _outputCount = OUTCT;
        _testCount = TSTCT;
        _inputPins = INP;
        _outputPins = OUT;
        _testInputs = TSTINP;
        _reference = REF;
    }

    public String get_name() {
        return _name;
    }
    public int[] get_reference() {
        return _reference;
    }
}
