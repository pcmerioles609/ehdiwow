package com.ehdiwow.ble.digitalictester;

/**
 * This is a user-defined struct/class to define an IC object with its properties/members
 */

public class IC {
    private String _name;
    private String _description;
    private PIN _power;
    private PIN _ground;
    private int _inputCount;
    private int _outputCount;
    private int _testCount;

    private PIN[] _inputPins;
    private PIN[] _outputPins;
    private int[] _testInputs;
    private int[] _reference;

    public IC(String NAME, String DESC, PIN VCC, PIN GND, int INPCT, int OUTCT, int TSTCT,
              PIN[] INP, PIN[] OUT, int[] TSTINP, int[] REF) {
        _name = NAME;
        _description = DESC;
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

    public String get_name() { return _name; }
    public String get_description() {return _description; }
    public PIN[] get_inputPins() { return _inputPins; }
    public PIN[] get_outputPins() { return _outputPins; }
    public int get_inputCount() { return _inputCount; }
    public int get_outputCount() {return _outputCount; }
    public int get_testCount() { return _testCount; }
    public int[] get_testInputs() { return _testInputs; }
    public int[] get_reference() { return _reference;
    }
}
