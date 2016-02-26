package com.ehdiwow.ble.digitalictester;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Merioles on 2/20/2016.
 */
public class IcUnderTest {
    private IC _icUT;
    private List<Integer> _digital = new ArrayList<Integer>();
    private List<Integer> _analogLow = new ArrayList<Integer>();
    private List<Integer> _analogHigh = new ArrayList<Integer>();

    public IcUnderTest (IC ICUT){
        this._icUT = ICUT;
    }

    public IC get_icUT() { return _icUT; }

    public void add_digital(Integer value) { this._digital.add(value); }
    public List<Integer> get_digital() { return _digital; }

    public void add_analogLow(Integer value) { this._analogLow.add(value); }
    public List<Integer> get_analogLow() { return _analogLow; }

    public void add_analogHigh(Integer value) {this._analogHigh.add(value); }
    public List<Integer> get_analogHigh() { return _analogHigh;}
}

