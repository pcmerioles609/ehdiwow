package com.ehdiwow.ble.digitalictester;

/**
 *  This is a user-defined struct/class to define an IC's PIN object with its properties/members
 */
public class PIN {
    private String _name;       // the name of the individual pin
    private int _pinIC;         // the number of the pin in the IC
    private int _pinZIF;         // the corresponding ZIF socket pin number

    public PIN(String NAME, int pinIC, int pinZIF) {
        _name = NAME;
        _pinIC = pinIC;
        _pinZIF = pinZIF;
    }

    public String get_name() { return _name; }
    public int get_pinZIF() { return _pinZIF; }
}
