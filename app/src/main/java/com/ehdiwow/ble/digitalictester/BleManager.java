package com.ehdiwow.ble.digitalictester;

/**
 * Created by user on 1/1/2016.
 */
public class BleManager {
    private static BleManager ourInstance = new BleManager();

    public static BleManager getInstance() {
        return ourInstance;
    }

    private BleManager() {
    }
}
