package com.ehdiwow.ble.digitalictester;

/**
 * This is a temporary global class to hold global static IC variables
 * until a proper SQLite Database system is implemented
 */

/*
         TESTER PIN CONFIG
             ___  ___
        2 --|   --   |-- 31
        3 --|        |-- 30
        4 --|        |-- 29
        5 --|  ICXX  |-- 28
        6 --|        |-- 27
        7 --|        |-- 26
       12 --|________|-- 25
*/

public class DeviceList {

    // TTL QUAD 2-INPUT NAND GATE
    public static final IC _7400 = new IC("7400", 31, 12, 8, 4, 4,
            new int[]{2,3,5,6,26,27,29,30}, new int[]{4,7,25,28}, new int[]{0,85,170,255}, new int[]{15,15,15,0});
    // TTL QUAD 2-INPUT NOR GATE
    public static final IC _7402 = new IC("7402", 31, 12, 8, 4, 4,
            new int[]{3,4,6,7,25,26,28,29}, new int[]{2,5,27,30}, new int[]{0,85,170,255}, new int[]{15,0,0,0});
    // TTL HEX INVERTERS
    public static final IC _7404 = new IC("7404", 31, 12, 6, 6, 2,
            new int[]{2,4,6,26,28,30}, new int[]{3,5,7,25,27,29}, new int[]{0,63}, new int[]{63,0});
    // TTL QUAD 2-INPUT AND GATE
    public static final IC _7408 = new IC("7408", 31, 12, 8, 4, 4,
            new int[]{2,3,5,6,26,27,29,30}, new int[]{4,7,25,28}, new int[]{0,85,170,255}, new int[]{0,0,0,15});
    // TTL QUAD 2-INPUT OR GATE
    public static final IC _7432 = new IC("7432", 31, 12, 8, 4, 4,
            new int[]{2,3,5,6,26,27,29,30}, new int[]{4,7,25,28}, new int[]{0,85,170,255}, new int[]{0,15,15,15});
    // TTL QUAD 2-INPUT XOR GATE
    public static final IC _7486 = new IC("7486", 31, 12, 8, 4, 4,
            new int[]{2,3,5,6,26,27,29,30}, new int[]{4,7,25,28}, new int[]{0,85,170,255}, new int[]{0,15,15,0});
    // TTL QUAD 2-INPUT XNOR GATE (OPEN COLLECTOR OUTPUT)
    public static final IC _74266 = new IC("74266", 31, 12, 8, 4, 4,
            new int[]{2,3,6,7,25,26,29,30}, new int[]{4,5,27,28}, new int[]{0,85,170,255}, new int[]{15,0,0,15});


    // CMOS QUAD 2-INPUT NOR GATE
    public static final IC _4001 = new IC("4001", 31, 12, 8, 4, 4,
            new int[]{2,3,6,7,25,26,29,30}, new int[]{4,5,27,28}, new int[]{0,85,170,255}, new int[]{15,0,0,0});
    // CMOS QUAD 2-INPUT NAND GATE
    public static final IC _4011 = new IC("4011", 31, 12, 8, 4, 4,
            new int[]{2,3,6,7,25,26,29,30}, new int[]{4,5,27,28}, new int[]{0,85,170,255}, new int[]{15,15,15,0});
    // CMOS HEX INVERTERS
    public static final IC _4069 = new IC("4069", 31, 12, 6, 6, 2,
            new int[]{2,4,6,26,28,30}, new int[]{3,5,7,25,27,29}, new int[]{0,63}, new int[]{63,0});
    // CMOS QUAD 2-INPUT XOR GATE
    public static final IC _4070 = new IC("4070", 31, 12, 8, 4, 4,
            new int[]{2,3,6,7,25,26,29,30}, new int[]{4,5,27,28}, new int[]{0,85,170,255}, new int[]{0,15,15,0});
    // CMOS QUAD 2-INPUT OR GATE
    public static final IC _4071 = new IC("4071", 31, 12, 8, 4, 4,
            new int[]{2,3,6,7,25,26,29,30}, new int[]{4,5,27,28}, new int[]{0,85,170,255}, new int[]{0,15,15,15});
    // CMOS QUAD 2-INPUT XNOR GATE
    public static final IC _4077 = new IC("4077", 31, 12, 8, 4, 4,
            new int[]{2,3,6,7,25,26,29,30}, new int[]{4,5,27,28}, new int[]{0,84,170,255}, new int[]{15,0,0,15});
    // CMOS QUAD 2-INPUT AND GATE
    public static final IC _4081 = new IC("4081", 31, 12, 8, 4, 4,
            new int[]{2,3,6,7,25,26,29,30}, new int[]{4,5,27,28}, new int[]{0,85,170,255}, new int[]{0,0,0,15});


}
