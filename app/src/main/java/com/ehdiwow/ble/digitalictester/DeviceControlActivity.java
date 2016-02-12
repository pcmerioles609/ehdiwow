package com.ehdiwow.ble.digitalictester;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.widget.Toast;

/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.  The Activity
 * communicates with {@code BluetoothLeService}, which in turn interacts with the
 * Bluetooth LE API.
 */

public class DeviceControlActivity extends Activity {
    private final static String TAG = DeviceControlActivity.class.getSimpleName();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    private TextView isSerial;
    private TextView mConnectionState;
    private TextView mDataField;
    private String mDeviceName;
    private String mDeviceAddress;
    //private ExpandableListView mGattServicesList;
    private BluetoothLeService mBluetoothLeService;
    private boolean mConnected = false;
    private BluetoothGattCharacteristic characteristicTX;
    private BluetoothGattCharacteristic characteristicRX;


    private String dataFromTester = "";
    private RadioButton rad_TTL;
    private RadioButton rad_CMOS;
    private Spinner spnr_DUT;
    private Button btn_TEST;
    private TextView txt_testResults;

    private TextView txt_testerStatus;
    private ScheduledExecutorService waitForTesterToRespond;

    public final static UUID HM_RX_TX =
            UUID.fromString(SampleGattAttributes.HM_RX_TX);

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    private boolean onStopExemption = true;

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                Toast.makeText(DeviceControlActivity.this, "Unable to initialize Bluetooth", Toast.LENGTH_SHORT).show();
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(R.string.connected);
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
                invalidateOptionsMenu();
                clearUI();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
                // this does not work with ACTION_GATT_CONNECTED
                sendCodeLoop(1);    // RDYSTRT?
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                decodeDataFromTester(intent.getStringExtra(mBluetoothLeService.EXTRA_DATA));
            }
        }
    };

    private void clearUI() {
        mDataField.setText(R.string.no_data);
        txt_testerStatus.setText("Not Responding");
        if (!waitForTesterToRespond.isShutdown())
            waitForTesterToRespond.shutdown();

        enableDisableUI(false);
        appendTestResults("", true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gatt_services_characteristics);

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        // Sets up UI references.
        ((TextView) findViewById(R.id.device_address)).setText(mDeviceAddress);
        mConnectionState = (TextView) findViewById(R.id.connection_state);
        // is serial present?
        isSerial = (TextView) findViewById(R.id.isSerial);
        mDataField = (TextView) findViewById(R.id.data_value);
     
        getActionBar().setTitle(mDeviceName);
        getActionBar().setDisplayHomeAsUpEnabled(false);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        rad_TTL = (RadioButton)findViewById(R.id.radTTL);
        rad_CMOS = (RadioButton)findViewById(R.id.radCMOS);
        spnr_DUT = (Spinner)findViewById(R.id.spnrDutName);
        btn_TEST = (Button)findViewById(R.id.btnTest);
        txt_testResults  = (TextView)findViewById(R.id.txtTestResults);

        enableDisableUI(false);

        onDeviceTypeChanged();
        rad_TTL.setChecked(true);
        txt_testerStatus = (TextView)findViewById(R.id.testerStatus);
        onStartDutTestClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
        clearUI();
    }

    @Override
    protected void onStop() {
        if (onStopExemption)
            sendCode("APPDISC" + "\n");
        else
            sendCode("RESULTS" + "\n");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        sendCode("APPDISC" + "\n");
        onStopExemption = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gatt_services, menu);
        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_connect:
                mBluetoothLeService.connect(mDeviceAddress);
                return true;
            case R.id.menu_disconnect:
                sendCode("APPDISC" + "\n");         // notifies the tester that the app disconnected
                mBluetoothLeService.disconnect();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConnectionState.setText(resourceId);
            }
        });
    }

    private void decodeDataFromTester(String data) {

        if (data != null) {
            dataFromTester = data;
            mDataField.setText(data);
            if (data.equals("STRTOK")) {
                if (!waitForTesterToRespond.isShutdown())
                    waitForTesterToRespond.shutdown();
                txt_testerStatus.setText(" Responding");
                appendTestResults("", true);
                enableDisableUI(true);
            } else if (data.equals("TESTOK")) {
                if (!waitForTesterToRespond.isShutdown())
                    waitForTesterToRespond.shutdown();
                appendTestResults("Tester Device Ready.", false);
                appendTestResults("Initializing test for " + spnr_DUT.getSelectedItem().toString(), false);
                sendCode(spnr_DUT.getSelectedItem().toString());
            } else if (data.equals("PINOK")) {
                appendTestResults("PinMode Setup Complete.", false);
            } else if (data.equals("PWROK")) {
                appendTestResults("Power Setup Complete.", false);
            } else if (data.equals("TSTOK")) {
                appendTestResults("Device Test completed.\nComparing results...", false);
            } else if (data.contains(",")) {
                String[] measurements = data.split(",");
                try {
                    List<Integer> resultList = new ArrayList<Integer>();
                    for (String res : measurements)
                        resultList.add(Integer.parseInt(res));
                    int[] resultArray = new int[resultList.size()];
                    for (int i=0; i<resultList.size(); i++)
                        resultArray[i] = resultList.get(i);
                    DeviceUnderTest.getInstance().set_readings(resultArray);

                    if (DeviceUnderTest.getInstance().is_result())
                        appendTestResults("Device Passed", false);
                    else
                        appendTestResults("Device Failed", false);
                } catch (NumberFormatException err) {
                    appendTestResults("An error occurred.", false);
                }

                onStopExemption = false;
                Intent intent = new Intent("com.ehdiwow.ble.digitalictester.TestResultActivity");
                startActivity(intent);

                enableDisableUI(true);
            } else if (data.equals("OFFLINE")) {
                appendTestResults("", true);
                appendTestResults("Tester found but is currently in offline mode.\n" +
                        "Restart tester to enter connect with this app.", false);
            }
        }
    }

    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();

 
        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));
            
            // If the service exists for HM 10 Serial, say so.
            if (SampleGattAttributes.lookup(uuid, unknownServiceString) == "HM 10 Serial") {
                isSerial.setText("Yes, serial :-)");
            } else {
                isSerial.setText("No, serial :-(");
            }
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

     		// get characteristic when UUID matches RX/TX UUID
    		 characteristicTX = gattService.getCharacteristic(BluetoothLeService.UUID_HM_RX_TX);
    		 characteristicRX = gattService.getCharacteristic(BluetoothLeService.UUID_HM_RX_TX);
        }
        
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }


/******************     ESTABLISH CONNECTION WITH THE TESTER    ********************/

    //  RESPOND REQUEST CODES //
    //  0 - reserved
    //  1 - startUp         RDYSTRT?
    //  2 - beginTest       RDYTEST?
    //  3 - endTest         ENDTEST?

    private void sendCodeLoop(final int code) {

        String message = "";
        switch (code) {
            case 0:
                    break;
            case 1: txt_testerStatus.setText("Waiting for response...");
                    message = "RDYSTRT?" + "\n";
                    break;
            case 2: message = "RDYTEST?" + "\n";
                    break;
            case 3: message = "ENDTEST?" + "\n";
                    break;
        }

        final String request = message;
        waitForTesterToRespond = Executors.newSingleThreadScheduledExecutor();
        waitForTesterToRespond.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                sendCode(request);
            }
        }, 0, 2000, TimeUnit.MILLISECONDS);
    }

/*****************      SEND CODE MESSAGE TO TESTER      ***********************/

    private void sendCode(String msg) {
        final byte[] tx = msg.getBytes();
        if (mConnected) {
            characteristicTX.setValue(tx);
            mBluetoothLeService.writeCharacteristic(characteristicTX);
            mBluetoothLeService.setCharacteristicNotification(characteristicRX, true);
        }
    }

/******************         DEVICE UNDER TEST       ******************************/

    private void enableDisableUI(boolean isEnab) {
        TextView dut_Name = (TextView)findViewById(R.id.txtDutType);
        TextView dut_Type = (TextView)findViewById(R.id.txtDutName);

        dut_Name.setEnabled((isEnab));
        dut_Type.setEnabled(isEnab);
        rad_TTL.setEnabled(isEnab);
        rad_CMOS.setEnabled(isEnab);
        spnr_DUT.setEnabled(isEnab);
        btn_TEST.setEnabled(isEnab);
    }

    private void onDeviceTypeChanged() {
        RadioGroup rdGrp_dutType = (RadioGroup)findViewById(R.id.radGrpDutType);
        rdGrp_dutType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radTTL:
                        changeDutList(R.array.ttlList);
                        break;
                    case R.id.radCMOS:
                        changeDutList(R.array.cmosList);
                        break;
                }
            }
        });
    }

    private void changeDutList(int id) {
        ArrayAdapter<CharSequence> dutAdapter = ArrayAdapter.createFromResource(
                this, id, android.R.layout.simple_spinner_item);

        dutAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_DUT.setAdapter(dutAdapter);
    }

    private void onStartDutTestClick() {                                // starts testing process
        btn_TEST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder testRemDialog = new AlertDialog.Builder(DeviceControlActivity.this);
                LayoutInflater inflater = (DeviceControlActivity.this).getLayoutInflater();
                testRemDialog.setTitle("TEST REMINDERS");
                //testRemDialog.setCancelable(true);
                testRemDialog.setView(inflater.inflate(R.layout.test_reminders_dialog, null))
                             .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialog, int id) {
                                     enableDisableUI(false);                                 // disables UI first to prevent unnecessary user clicks
                                     appendTestResults("", true);
                                     tempSetDutIC(spnr_DUT.getSelectedItem().toString());
                                     appendTestResults("Waiting for tester...", false);
                                     sendCodeLoop(2);  // RDYTEST?
                                 }
                             })
                             .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialog, int id) {
                                     // TODO : no action for when the user click CANCEL
                                 }
                             });
                testRemDialog.create();
                testRemDialog.show();
            }
        });
    }

    private void appendTestResults(String appendLine, boolean clear) {
        if (clear)
            txt_testResults.setText("");
        else {
            String results = txt_testResults.getText().toString();
            results += appendLine + "\n";

            txt_testResults.setMovementMethod(new ScrollingMovementMethod());
            txt_testResults.setText(results);
        }
    }

    // TODO : this is a temporary method for identifying DUT ICs until a proper ID method is created

    private void tempSetDutIC(String spnrSel) {
        IC currentDutIC = null;
        if (spnrSel.equals("74LS00 (NAND)"))
            currentDutIC = DeviceList._7400;
        else if (spnrSel.equals("74LS02 (NOR)"))
            currentDutIC = DeviceList._7402;
        else if (spnrSel.equals("74LS04 (NOT)"))
            currentDutIC = DeviceList._7404;
        else if (spnrSel.equals("74LS08 (AND)"))
            currentDutIC = DeviceList._7408;
        else if (spnrSel.equals("74LS32 (OR)"))
            currentDutIC = DeviceList._7432;
        else if (spnrSel.equals("74LS86 (XOR)"))
            currentDutIC = DeviceList._7486;
        else if (spnrSel.equals("74LS266 (XNOR)"))
            currentDutIC = DeviceList._74266;

        else if (spnrSel.equals("CD4001 (NOR)"))
            currentDutIC = DeviceList._4001;
        else if (spnrSel.equals("CD4011 (NAND)"))
            currentDutIC = DeviceList._4011;
        else if (spnrSel.equals("CD4069 (NOT)"))
            currentDutIC = DeviceList._4069;
        else if (spnrSel.equals("CD4070 (XOR)"))
            currentDutIC = DeviceList._4070;
        else if (spnrSel.equals("CD4071 (OR)"))
            currentDutIC = DeviceList._4071;
        else if (spnrSel.equals("CD4077 (XNOR)"))
            currentDutIC = DeviceList._4077;
        else if (spnrSel.equals("CD4081 (AND)"))
            currentDutIC = DeviceList._4081;

        DeviceUnderTest.getInstance().set_dutIC(currentDutIC);
    }
}