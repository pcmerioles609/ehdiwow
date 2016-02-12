package com.ehdiwow.ble.digitalictester;

/**
 * This is a temporary global class to hold global static IC variables
 * until a proper SQLite Database system is implemented
 */

/*
       TESTER ZIF PIN CONFIG
             ___  ___
       31 --|   --   |-- 1
       30 --|        |-- 2
       29 --|        |-- 4
       28 --|  ICXX  |-- 5
       27 --|        |-- 6
       26 --|        |-- 7
       25 --|________|-- 16
*/

public class DeviceList {

    // TTL QUAD 2-INPUT NAND GATE
    public static final IC _7400 = new IC("7400", "QUAD 2-INPUT NAND GATES",
            new PIN("VCC",14,1), new PIN("GND",7,25), 8, 4, 4,
            new PIN[]{new PIN("A1",1,31), new PIN("B1",2,30), new PIN("A2",4,28), new PIN("B2",5,27),
                      new PIN("A3",9,7), new PIN("B3",10,6), new PIN("A4",12,4), new PIN("B4",13,2)},
            new PIN[]{new PIN("Y1",3,29), new PIN("Y2",6,26), new PIN("Y3",8,16), new PIN("Y4",11,5)},
            new int[]{0,85,170,255}, new int[]{15,15,15,0});

    // TTL QUAD 2-INPUT NOR GATE
    public static final IC _7402 = new IC("7402", "QUAD 2-INPUT NOR GATES",
            new PIN("VCC",14,1), new PIN("GND",7,25), 8, 4, 4,
            new PIN[]{new PIN("A1",2,30), new PIN("B1",3,29), new PIN("A2",5,27), new PIN("B2",6,26),
                      new PIN("A3",8,16), new PIN("B3",9,7), new PIN("A4",11,5), new PIN("B4",12,4)},
            new PIN[]{new PIN("Y1",1,31), new PIN("Y2",4,28), new PIN("Y3",10,6), new PIN("Y4",13,2)},
            new int[]{0,85,170,255}, new int[]{15,0,0,0});

    // TTL HEX INVERTERS
    public static final IC _7404 = new IC("7404", "HEX INVERTERS (NOT GATES)",
            new PIN("VCC",14,1), new PIN("GND",7,25), 6, 6, 2,
            new PIN[]{new PIN("A1",1,31), new PIN("A2",3,29), new PIN("A3",5,27), new PIN("A4",9,7), new PIN("A5",11,5), new PIN("A6",13,2)},
            new PIN[]{new PIN("Y1",2,30), new PIN("Y2",4,28), new PIN("Y3",6,26), new PIN("Y4",8,16), new PIN("Y5",10,6), new PIN("Y6",12,4)},
            new int[]{0,63}, new int[]{63,0});

    // TTL QUAD 2-INPUT AND GATE
    public static final IC _7408 = new IC("7408", "QUAD 2-INPUT AND GATES",
            new PIN("VCC",14,1), new PIN("GND",7,25), 8, 4, 4,
            new PIN[]{new PIN("A1",1,31), new PIN("B1",2,30), new PIN("A2",4,28), new PIN("B2",5,27),
                      new PIN("A3",9,7), new PIN("B3",10,6), new PIN("A4",12,4), new PIN("B4",13,2)},
            new PIN[]{new PIN("Y1",3,29), new PIN("Y2",6,26), new PIN("Y3",8,16), new PIN("Y4",11,5)},
            new int[]{0,85,170,255}, new int[]{0,0,0,15});

    // TTL QUAD 2-INPUT OR GATE
    public static final IC _7432 = new IC("7432", "QUAD 2-INPUT OR GATES",
            new PIN("VCC",14,1), new PIN("GND",7,25), 8, 4, 4,
            new PIN[]{new PIN("A1",1,31), new PIN("B1",2,30), new PIN("A2",4,28), new PIN("B2",5,27),
                      new PIN("A3",9,7), new PIN("B3",10,6), new PIN("A4",12,4), new PIN("B4",13,2)},
            new PIN[]{new PIN("Y1",3,29), new PIN("Y2",6,26), new PIN("Y3",8,16), new PIN("Y4",11,5)},
            new int[]{0,85,170,255}, new int[]{0,15,15,15});

    // TTL QUAD 2-INPUT XOR GATE
    public static final IC _7486 = new IC("7486", "QUAD 2-INPUT XOR GATES",
            new PIN("VCC",14,1), new PIN("GND",7,25), 8, 4, 4,
            new PIN[]{new PIN("A1",1,31), new PIN("B1",2,30), new PIN("A2",4,28), new PIN("B2",5,27),
                      new PIN("A3",9,7), new PIN("B3",10,6), new PIN("A4",12,4), new PIN("B4",13,2)},
            new PIN[]{new PIN("Y1",3,29), new PIN("Y2",6,26), new PIN("Y3",8,16), new PIN("Y4",11,5)},
            new int[]{0,85,170,255}, new int[]{0,15,15,0});

    // TTL QUAD 2-INPUT XNOR GATE (OPEN COLLECTOR OUTPUT)
    public static final IC _74266 = new IC("74266", "QUAD 2-INPUT XNOR GATES (OPEN COLLECTOR)",
            new PIN("VCC",14,1), new PIN("GND",7,25), 8, 4, 4,
            new PIN[]{new PIN("A1",1,31), new PIN("B1",2,30), new PIN("A2",5,27), new PIN("B2",6,26),
                      new PIN("A3",8,16), new PIN("B3",9,7), new PIN("A4",12,4), new PIN("B4",13,2)},
            new PIN[]{new PIN("Y1",3,29), new PIN("Y2",4,28), new PIN("Y3",10,6), new PIN("Y4",11,5)},
            new int[]{0,85,170,255}, new int[]{15,0,0,15});


    // CMOS QUAD 2-INPUT NOR GATE
    public static final IC _4001 = new IC("4001", "QUAD 2-INPUT NOR GATES",
            new PIN("VCC",14,1), new PIN("GND",7,25), 8, 4, 4,
            new PIN[]{new PIN("A1",1,31), new PIN("B1",2,30), new PIN("A2",5,27), new PIN("B2",6,26),
                      new PIN("A3",8,16), new PIN("B3",9,7), new PIN("A4",12,4), new PIN("B4",13,2)},
            new PIN[]{new PIN("Y1",3,29), new PIN("Y2",4,28), new PIN("Y3",10,6), new PIN("Y4",11,5)},
            new int[]{0,85,170,255}, new int[]{15,0,0,0});

    // CMOS QUAD 2-INPUT NAND GATE
    public static final IC _4011 = new IC("4011", "QUAD 2-INPUT NAND GATES",
            new PIN("VCC",14,1), new PIN("GND",7,25), 8, 4, 4,
            new PIN[]{new PIN("A1",1,31), new PIN("B1",2,30), new PIN("A2",5,27), new PIN("B2",6,26),
                      new PIN("A3",8,16), new PIN("B3",9,7), new PIN("A4",12,4), new PIN("B4",13,2)},
            new PIN[]{new PIN("Y1",3,29), new PIN("Y2",4,28), new PIN("Y3",10,6), new PIN("Y4",11,5)},
            new int[]{0,85,170,255}, new int[]{15,15,15,0});

    // CMOS HEX INVERTERS
    public static final IC _4069 = new IC("4069", "HEX INVERTERS (NOT GATES)",
            new PIN("VCC",14,1), new PIN("GND",7,25), 6, 6, 2,
            new PIN[]{new PIN("A1",1,31), new PIN("A2",3,29), new PIN("A3",5,27), new PIN("A4",9,7), new PIN("A5",11,5), new PIN("A6",13,2)},
            new PIN[]{new PIN("Y1",2,30), new PIN("Y2",4,28), new PIN("Y3",6,26), new PIN("Y4",8,16), new PIN("Y5",10,6), new PIN("Y6",12,4)},
            new int[]{0,63}, new int[]{63,0});

    // CMOS QUAD 2-INPUT XOR GATE
    public static final IC _4070 = new IC("4070", "QUAD 2-INPUT XOR GATE",
            new PIN("VCC",14,1), new PIN("GND",7,25), 8, 4, 4,
            new PIN[]{new PIN("A1",1,31), new PIN("B1",2,30), new PIN("A2",5,27), new PIN("B2",6,26),
                      new PIN("A3",8,16), new PIN("B3",9,7), new PIN("A4",12,4), new PIN("B4",13,2)},
            new PIN[]{new PIN("Y1",3,29), new PIN("Y2",4,28), new PIN("Y3",10,6), new PIN("Y4",11,5)},
            new int[]{0,85,170,255}, new int[]{0,15,15,0});

    // CMOS QUAD 2-INPUT OR GATE
    public static final IC _4071 = new IC("4071", "QUAD 2-INPUT OR GATE",
            new PIN("VCC",14,1), new PIN("GND",7,25), 8, 4, 4,
            new PIN[]{new PIN("A1",1,31), new PIN("B1",2,30), new PIN("A2",5,27), new PIN("B2",6,26),
                      new PIN("A3",8,16), new PIN("B3",9,7), new PIN("A4",12,4), new PIN("B4",13,2)},
            new PIN[]{new PIN("Y1",3,29), new PIN("Y2",4,28), new PIN("Y3",10,6), new PIN("Y4",11,5)},
            new int[]{0,85,170,255}, new int[]{0,15,15,15});

    // CMOS QUAD 2-INPUT XNOR GATE
    public static final IC _4077 = new IC("4077", "QUAD 2-INPUT XNOR GATE",
            new PIN("VCC",14,1), new PIN("GND",7,25), 8, 4, 4,
            new PIN[]{new PIN("A1",1,31), new PIN("B1",2,30), new PIN("A2",5,27), new PIN("B2",6,26),
                      new PIN("A3",8,16), new PIN("B3",9,7), new PIN("A4",12,4), new PIN("B4",13,2)},
            new PIN[]{new PIN("Y1",3,29), new PIN("Y2",4,28), new PIN("Y3",10,6), new PIN("Y5",11,5)},
            new int[]{0,84,170,255}, new int[]{15,0,0,15});

    // CMOS QUAD 2-INPUT AND GATE
    public static final IC _4081 = new IC("4081", "QUAD 2-INPUT AND GATE",
            new PIN("VCC",14,1), new PIN("GND",7,25), 8, 4, 4,
            new PIN[]{new PIN("A1",1,31), new PIN("B1",2,30), new PIN("A2",5,27), new PIN("B2",6,26),
                      new PIN("A3",8,16), new PIN("B3",9,7), new PIN("A4",12,4), new PIN("B4",13,2)},
            new PIN[]{new PIN("Y1",3,29), new PIN("Y2",4,28), new PIN("Y3",10,6), new PIN("Y4",11,5)},
            new int[]{0,85,170,255}, new int[]{0,0,0,15});


}
