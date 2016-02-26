package com.ehdiwow.ble.digitalictester;

/**
 * This is a user-defined struct/class to define an IC object with its properties/members
 */

public class IC {
    private final String _name;           // the unique name of the IC
    private final String _description;    // the IC's descpription
    private final String _type;           // the IC's type (TTL or CMOS)
    private final int _id;                // the IC's unique ID associated with the Arduino program
    private final PIN _power;             // the POWER pin where VCC is applied
    private final PIN _ground;            // the GROUND pin where 0V is applied
    private final int _detect;            // the IC's decoded pinStates when power is applied (used for auto detection)
    private final int _inputCount;        // the number of the IC's input pins
    private final int _outputCount;       // the number of the IC's output pins
    private final int _testCount;         // the number of test waveforms to be introduced based on the datasheet
    private final int _analogCount;       // the number of IC output pins mapped to an analog Arduino pin

    private final PIN[] _analogOut;       // an array that contains the IC's output pins mapped to an analog pin in Arduino
    private final PIN[] _inputPins;       // an array that contains the IC's input pins mapped to Arduino
    private final PIN[] _outputPins;      // an array that contains the IC's output pins mapped to Arduino
    private final int[] _testInputs;      // an array that contains the encoded test waveforms
    private final int[] _reference;       // the encoded expected result measured from the IC's output pins

    public IC(String NAME, String DESC, String TYPE, int ID, PIN VCC, PIN GND, int DTECT, int INPCT, int OUTCT, int TSTCT,  int ANACT,
              PIN[] ANOUT, PIN[] INP, PIN[] OUT, int[] TSTINP, int[] REF) {
        this._name = NAME;
        this._description = DESC;
        this._type = TYPE;
        this._id = ID;
        this._power = VCC;
        this._ground = GND;
        this._detect = DTECT;
        this._inputCount = INPCT;
        this._outputCount = OUTCT;
        this._testCount = TSTCT;
        this._analogCount = ANACT;
        this._analogOut = ANOUT;
        this._inputPins = INP;
        this._outputPins = OUT;
        this._testInputs = TSTINP;
        this._reference = REF;
    }

    public String get_name() { return _name; }
    public String get_description() {return _description; }
    public String get_type() { return _type; }
    public int get_id() { return _id; }
    public PIN get_power() { return _power; }
    public PIN get_ground() { return _ground; }
    public int get_detect() { return _detect; }
    public PIN[] get_inputPins() { return _inputPins; }
    public PIN[] get_outputPins() { return _outputPins; }
    public int get_inputCount() { return _inputCount; }
    public int get_outputCount() {return _outputCount; }
    public int get_testCount() { return _testCount; }
    public int[] get_testInputs() { return _testInputs; }
    public int[] get_reference() { return _reference;
    }
}
