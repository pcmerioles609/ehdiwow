package com.ehdiwow.ble.digitalictester;

/**
 * This is a temporary global class to hold global static IC variables
 * until a proper SQLite Database system is implemented
 */

public class DeviceList {

    public static final IC _7404 = new IC("7404", 31, 12, 6, 6, 2,
            new int[]{2,4,6,26,28,30}, new int[]{3,5,7,25,27,29}, new int[]{0,63}, new int[]{63,0});

    public static final IC _7408 = new IC("7408", 31, 12, 8, 4, 4,
            new int[]{2,3,5,6,26,27,29,30}, new int[]{4,7,25,28}, new int[]{0,85,170,255}, new int[]{0,0,0,15});


}
