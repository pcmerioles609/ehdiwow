package com.ehdiwow.ble.digitalictester;

/**
 * This is a temporary global class to hold global static IC variables
 * until a proper SQLite Database system is implemented
 */

/*
       TESTER ZIF PIN CONFIG
             ___  ___
        1 --|   --   |-- 31
        2 --|        |-- 30
        3 --|        |-- 29
        4 --|  ICXX  |-- 28
        5 --|        |-- 27
        6 --|        |-- 26
        7 --|________|-- 25
*/


public class DeviceList {

    // TTL QUAD 2-INPUT NAND GATE
    public static final IC _7400 = new IC("7400", "TTL QUAD 2-INPUT NAND GATES", "TTL", 700,
            new PIN("VCC",14,31), new PIN("GND",7,7), 3483, 8, 4, 4, 2,
            new PIN[]{new PIN("Y3",8,25), new PIN("Y4",11,28)},
            new PIN[]{new PIN("A1",1,1), new PIN("B1",2,2), new PIN("A2",4,4), new PIN("B2",5,5),
                      new PIN("A3",9,26), new PIN("B3",10,27), new PIN("A4",12,29), new PIN("B4",13,30)},
            new PIN[]{new PIN("Y1",3,3), new PIN("Y2",6,6), new PIN("Y3",8,25), new PIN("Y4",11,28)},
            new int[]{0,85,170,255}, new int[]{15,15,15,0});

    // TTL QUAD 2-INPUT NOR GATE
    public static final IC _7402 = new IC("7402", "TTL QUAD 2-INPUT NOR GATES", "TTL", 702,
            new PIN("VCC",14,31), new PIN("GND",7,7), 1782, 8, 4, 4, 2,
            new PIN[]{new PIN("Y3",10,27), new PIN("Y4",13,30)},
            new PIN[]{new PIN("A1",2,2), new PIN("B1",3,3), new PIN("A2",5,5), new PIN("B2",6,6),
                      new PIN("A3",8,25), new PIN("B3",9,26), new PIN("A4",11,28), new PIN("B4",12,29)},
            new PIN[]{new PIN("Y1",1,1), new PIN("Y2",4,4), new PIN("Y3",10,27), new PIN("Y4",13,30)},
            new int[]{0,85,170,255}, new int[]{15,0,0,0});

    // TTL HEX INVERTERS
    public static final IC _7404 = new IC("7404", "TTL HEX INVERTERS (NOT GATES)", "TTL", 704,
            new PIN("VCC",14,31), new PIN("GND",7,7), 2709, 6, 6, 2, 3,
            new PIN[]{new PIN("Y4",8,25), new PIN("Y5",10,27), new PIN("Y6",12,29)},
            new PIN[]{new PIN("A1",1,1), new PIN("A2",3,3), new PIN("A3",5,5), new PIN("A4",9,26), new PIN("A5",11,28), new PIN("A6",13,30)},
            new PIN[]{new PIN("Y1",2,2), new PIN("Y2",4,4), new PIN("Y3",6,6), new PIN("Y4",8,25), new PIN("Y5",10,27), new PIN("Y6",12,29)},
            new int[]{0,63}, new int[]{63,0});

    // TTL QUAD 2-INPUT AND GATE
    public static final IC _7408 = new IC("7408", "TTL QUAD 2-INPUT AND GATES", "TTL", 708,
            new PIN("VCC",14,31), new PIN("GND",7,7), 4095, 8, 4, 4, 2,
            new PIN[]{new PIN("Y3",8,25), new PIN("Y4",11,28)},
            new PIN[]{new PIN("A1",1,1), new PIN("B1",2,2), new PIN("A2",4,4), new PIN("B2",5,5),
                      new PIN("A3",9,26), new PIN("B3",10,27), new PIN("A4",12,29), new PIN("B4",13,30)},
            new PIN[]{new PIN("Y1",3,3), new PIN("Y2",6,6), new PIN("Y3",8,25), new PIN("Y4",11,28)},
            new int[]{0,85,170,255}, new int[]{0,0,0,15});

    // TTL QUAD 2-INPUT OR GATE
    public static final IC _7432 = new IC("7432", "TTL QUAD 2-INPUT OR GATES", "TTL", 732,
            new PIN("VCC",14,31), new PIN("GND",7,7), 4095, 8, 4, 4, 2,
            new PIN[]{new PIN("Y3",8,25), new PIN("Y4",11,28)},
            new PIN[]{new PIN("A1",1,1), new PIN("B1",2,2), new PIN("A2",4,4), new PIN("B2",5,5),
                      new PIN("A3",9,26), new PIN("B3",10,27), new PIN("A4",12,29), new PIN("B4",13,30)},
            new PIN[]{new PIN("Y1",3,3), new PIN("Y2",6,6), new PIN("Y3",8,25), new PIN("Y4",11,28)},
            new int[]{0,85,170,255}, new int[]{0,15,15,15});

    // TTL QUAD 2-INPUT XOR GATE
    public static final IC _7486 = new IC("7486", "TTL QUAD 2-INPUT XOR GATES", "TTL", 786,
            new PIN("VCC",14,31), new PIN("GND",7,7), 3483, 8, 4, 4, 2,
            new PIN[]{new PIN("Y3",8,25), new PIN("Y4",11,28)},
            new PIN[]{new PIN("A1",1,1), new PIN("B1",2,2), new PIN("A2",4,4), new PIN("B2",5,5),
                      new PIN("A3",9,26), new PIN("B3",10,27), new PIN("A4",12,29), new PIN("B4",13,30)},
            new PIN[]{new PIN("Y1",3,3), new PIN("Y2",6,6), new PIN("Y3",8,25), new PIN("Y4",11,28)},
            new int[]{0,85,170,255}, new int[]{0,15,15,0});

    // TTL QUAD 2-INPUT XNOR GATE (OPEN COLLECTOR OUTPUT)
    public static final IC _74266 = new IC("74266", "TTL QUAD 2-INPUT XNOR GATES (OPEN COLLECTOR)", "TTL", 7266,
            new PIN("VCC",14,31), new PIN("GND",7,7), 4095, 8, 4, 4, 2,
            new PIN[]{new PIN("Y3",10,27), new PIN("Y4",11,28)},
            new PIN[]{new PIN("A1",1,1), new PIN("B1",2,2), new PIN("A2",5,5), new PIN("B2",6,6),
                      new PIN("A3",8,25), new PIN("B3",9,26), new PIN("A4",12,29), new PIN("B4",13,30)},
            new PIN[]{new PIN("Y1",3,3), new PIN("Y2",4,4), new PIN("Y3",10,27), new PIN("Y4",11,28)},
            new int[]{0,85,170,255}, new int[]{15,0,0,15});


    // CMOS QUAD 2-INPUT NOR GATE
    public static final IC _4001 = new IC("4001", "CMOS QUAD 2-INPUT NOR GATES", "CMOS", 401,
            new PIN("VCC",14,31), new PIN("GND",7,7), 3315, 8, 4, 4, 2,
            new PIN[]{new PIN("Y3",10,27), new PIN("Y4",11,28)},
            new PIN[]{new PIN("A1",1,1), new PIN("B1",2,2), new PIN("A2",5,5), new PIN("B2",6,6),
                      new PIN("A3",8,25), new PIN("B3",9,26), new PIN("A4",12,29), new PIN("B4",13,30)},
            new PIN[]{new PIN("Y1",3,3), new PIN("Y2",4,4), new PIN("Y3",10,27), new PIN("Y4",11,28)},
            new int[]{0,85,170,255}, new int[]{15,0,0,0});

    // CMOS QUAD 2-INPUT NAND GATE
    public static final IC _4011 = new IC("4011", "CMOS QUAD 2-INPUT NAND GATES", "CMOS", 411,
            new PIN("VCC",14,31), new PIN("GND",7,7), 3315, 8, 4, 4, 2,
            new PIN[]{new PIN("Y3",10,27), new PIN("Y4",11,28)},
            new PIN[]{new PIN("A1",1,1), new PIN("B1",2,2), new PIN("A2",5,5), new PIN("B2",6,6),
                      new PIN("A3",8,25), new PIN("B3",9,26), new PIN("A4",12,29), new PIN("B4",13,30)},
            new PIN[]{new PIN("Y1",3,3), new PIN("Y2",4,4), new PIN("Y3",10,27), new PIN("Y4",11,28)},
            new int[]{0,85,170,255}, new int[]{15,15,15,0});

    // CMOS HEX INVERTERS
    public static final IC _4069 = new IC("4069", "CMOS HEX INVERTERS (NOT GATES)", "CMOS", 469,
            new PIN("VCC",14,31), new PIN("GND",7,7), 2709, 6, 6, 2, 3,
            new PIN[]{new PIN("Y4",8,25), new PIN("Y5",10,27), new PIN("Y6",12,29)},
            new PIN[]{new PIN("A1",1,1), new PIN("A2",3,3), new PIN("A3",5,5), new PIN("A4",9,26), new PIN("A5",11,28), new PIN("A6",13,30)},
            new PIN[]{new PIN("Y1",2,2), new PIN("Y2",4,4), new PIN("Y3",6,6), new PIN("Y4",8,25), new PIN("Y5",10,27), new PIN("Y6",12,29)},
            new int[]{0,63}, new int[]{63,0});

    // CMOS QUAD 2-INPUT XOR GATE
    public static final IC _4070 = new IC("4070", "CMOS QUAD 2-INPUT XOR GATE", "CMOS", 470,
            new PIN("VCC",14,31), new PIN("GND",7,7), 3315, 8, 4, 4, 2,
            new PIN[]{new PIN("Y3",10,27), new PIN("Y4",11,28)},
            new PIN[]{new PIN("A1",1,1), new PIN("B1",2,2), new PIN("A2",5,5), new PIN("B2",6,6),
                      new PIN("A3",8,25), new PIN("B3",9,26), new PIN("A4",12,29), new PIN("B4",13,30)},
            new PIN[]{new PIN("Y1",3,3), new PIN("Y2",4,4), new PIN("Y3",10,27), new PIN("Y4",11,28)},
            new int[]{0,85,170,255}, new int[]{0,15,15,0});

    // CMOS QUAD 2-INPUT OR GATE
    public static final IC _4071 = new IC("4071", "CMOS QUAD 2-INPUT OR GATE", "CMOS", 471,
            new PIN("VCC",14,31), new PIN("GND",7,7), 4095, 8, 4, 4, 2,
            new PIN[]{new PIN("Y3",10,27), new PIN("Y4",11,28)},
            new PIN[]{new PIN("A1",1,1), new PIN("B1",2,2), new PIN("A2",5,5), new PIN("B2",6,6),
                      new PIN("A3",8,25), new PIN("B3",9,26), new PIN("A4",12,29), new PIN("B4",13,30)},
            new PIN[]{new PIN("Y1",3,3), new PIN("Y2",4,4), new PIN("Y3",10,27), new PIN("Y4",11,28)},
            new int[]{0,85,170,255}, new int[]{0,15,15,15});

    // CMOS QUAD 2-INPUT XNOR GATE
    public static final IC _4077 = new IC("4077", "CMOS QUAD 2-INPUT XNOR GATE", "CMOS", 477,
            new PIN("VCC",14,31), new PIN("GND",7,7), 4095, 8, 4, 4, 2,
            new PIN[]{new PIN("Y3",10,27), new PIN("Y4",11,28)},
            new PIN[]{new PIN("A1",1,1), new PIN("B1",2,2), new PIN("A2",5,5), new PIN("B2",6,6),
                      new PIN("A3",8,25), new PIN("B3",9,26), new PIN("A4",12,29), new PIN("B4",13,30)},
            new PIN[]{new PIN("Y1",3,3), new PIN("Y2",4,4), new PIN("Y3",10,27), new PIN("Y4",11,28)},
            new int[]{0,84,170,255}, new int[]{15,0,0,15});

    // CMOS QUAD 2-INPUT AND GATE
    public static final IC _4081 = new IC("4081", "CMOS QUAD 2-INPUT AND GATE", "CMOS", 481,
            new PIN("VCC",14,31), new PIN("GND",7,7), 4095, 8, 4, 4, 2,
            new PIN[]{new PIN("Y3",10,27), new PIN("Y4",11,28)},
            new PIN[]{new PIN("A1",1,1), new PIN("B1",2,2), new PIN("A2",5,5), new PIN("B2",6,6),
                      new PIN("A3",8,25), new PIN("B3",9,26), new PIN("A4",12,29), new PIN("B4",13,30)},
            new PIN[]{new PIN("Y1",3,3), new PIN("Y2",4,4), new PIN("Y3",10,27), new PIN("Y4",11,28)},
            new int[]{0,85,170,255}, new int[]{0,0,0,15});

    public static IC[] supportedDevices = { _7400, _7402, _7404, _7408, _7432, _7486, _74266,
                                            _4001, _4011, _4069, _4070, _4071, _4077, _4081 };

}
