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
import android.os.Handler;
import android.os.IBinder;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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


    private Button btn_TEST;                 // this is the test button
    private TextView txt_testResults;        // the scrolling textview that displays data
    private TextView txt_testerStatus;                          // indicates tester status
    private TextView txt_serialBuffer;
    private ScheduledExecutorService waitForTesterToRespond;    // used for repeating functions


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
                clearUI();          // clears the user interface
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
                // this does not work with ACTION_GATT_CONNECTED
                sendCodeLoop(1);    // RDYSTRT?

            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                if (intent.getStringExtra(mBluetoothLeService.EXTRA_DATA) != null)
                    decodeDataFromTester(intent.getStringExtra(mBluetoothLeService.EXTRA_DATA));
            }
        }
    };

    private void clearUI() {
        mDataField.setText(R.string.no_data);
        txt_testerStatus.setText("Not Responding");
        if (!waitForTesterToRespond.isShutdown())
            waitForTesterToRespond.shutdown();

        btn_TEST.setEnabled(false);         // makes the user interface inaccessible
        //appendTestLogs("", true);    // clears the testResults TextView
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

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

        btn_TEST = (Button) findViewById(R.id.btnTest);                  // the Test Button
        txt_testResults = (TextView) findViewById(R.id.txtTestResults); // the testResults Textview
        txt_serialBuffer = (TextView) findViewById(R.id.txtSerialBuffer);

        btn_TEST.setEnabled(false);                             // makes the UI inaccessible at startup

        txt_testerStatus = (TextView) findViewById(R.id.testerStatus);  // the testerStatus Textview
        onStartDutTestClick();      // this is the method implemented when the user clicks the Test Button


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
        clearUI();              // makes the UI inaccessible on Pause
    }

    @Override
    protected void onStop() {               // TODO -- verify if connection terminates when viewing TestResultsActivity
        if (onStopExemption)
            sendCode("APPDISC");
        else
            sendCode("RESULTS");
        super.onStop();
    }

    @Override
    protected void onRestart() {            // TODO -- verify what this function does
        super.onRestart();
        sendCode("APPDISC");
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
        switch (item.getItemId()) {
            case R.id.menu_connect:
                mBluetoothLeService.connect(mDeviceAddress);
                return true;
            case R.id.menu_disconnect:
                sendCode("APPDISC");         // notifies the tester that the app disconnected
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


    /******************
     * ESTABLISH CONNECTION WITH THE TESTER
     ********************/

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
            case 1:
                txt_testerStatus.setText("Waiting for response...");
                message = "RDYSTRT?";
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

    /*****************
     * SEND CODE MESSAGE TO TESTER
     ***********************/

    // This method accepts a string, adds a terminating character and sends it over Bluetooth
    private void sendCode(String msg) {
        final byte[] tx = (msg + "\n").getBytes();      // the terminating char is newline '\n'
        if (mConnected) {
            characteristicTX.setValue(tx);
            mBluetoothLeService.writeCharacteristic(characteristicTX);
            mBluetoothLeService.setCharacteristicNotification(characteristicRX, true);
        }
    }

    /******************
     * ON CLICKING APP TEST BUTTON
     ******************************/

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
                                btn_TEST.setEnabled(false);                                 // disables UI first to prevent unnecessary user clicks
                                appendTestLogs("", true);
                                appendTestLogs("Waiting for tester...", false);
                                sendCode("RDYTEST?");
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

    /******************
     * ADDING TEXT TO THE TEST LOGS
     ******************************/

    private void appendTestLogs(String appendLine, boolean clear) {
        if (clear)
            txt_testResults.setText("");
        else {
            String results = txt_testResults.getText().toString();
            results += appendLine + "\n";

            txt_testResults.setMovementMethod(new ScrollingMovementMethod());
            txt_testResults.setText(results);
        }
    }

    /******************
     * ADDING TEXT TO THE SERIAL BUFFER LOGS
     ******************************/

    private void appendSerialBuffer(String appendLine, boolean clear) {
        if (clear)
            txt_serialBuffer.setText("");
        else {
            String results = txt_serialBuffer.getText().toString();
            results += appendLine + "\n";

            txt_serialBuffer.setMovementMethod(new ScrollingMovementMethod());
            txt_serialBuffer.setText(results);
        }
    }

    /******************
     * CATEGORIZING IC BETWEEN TTL AND CMOS
     ******************************/

    /** This method determines if the IC being tested is TTL, CMOS or FAIL based on the IC's output voltage levels.
     It first analyzes all analog LOW and analog HIGH readings, then it finds the range of these low and high values.
     Then it compares it with the set voltage limit (TODO -- this voltage limit should be modifiable by the user)
     TTL devices output low usually ranges from 0V - 1.5V and output high from 2V - 5V.
     CMOS are more precise with output low ranges from 0V - 0.5V and output high from 4.95V - 5V **/

    private void icCategorizer() {
        appendTestLogs("Categorizing device...", false);
        List<Integer> lows = new ArrayList<Integer>();
        List<Integer> highs = new ArrayList<Integer>();

        for (IcUnderTest icUT : DevicesUnderTest.getInstance().get_dutICs()) {
            for (Integer val : icUT.get_analogLow())
                lows.add(val);
            for (Integer val : icUT.get_analogHigh())
                highs.add(val);
        }

        int highestLow = 0;
        int lowestHigh = 1023;

        for (Integer i : lows) {
            if (i > highestLow)
                highestLow = i;
        }
        for (Integer i : highs) {
            if (i < lowestHigh)
                lowestHigh = i;
        }

        appendTestLogs("LOW : " + highestLow + " = " + String.format("%.2f", highestLow * (5.0 / 1023.0))  + "V", false);
        appendTestLogs("HIGH : " + lowestHigh + " = " + String.format("%.2f", lowestHigh * (5.0 / 1023.0)) + "V", false);

        // TODO -- change these limit values into modifiable variable
        double lowTTL = 306.9;   // this is equivalent to 1.5V -- acceptable TTL output low level
        double highTTL = 409.2;  // this is equivalent to 2V -- acceptable TTL output high level
        double lowCMOS = 102.3;  // this is equivalent to 0.5V -- acceptable CMOS output low level
        double highCMOS = 1012.77;
        if (((double)highestLow <= lowTTL) && ((double)lowestHigh >= highTTL)) {
            DevicesUnderTest.getInstance().set_icType("TTL");
            if (((double)highestLow <= lowCMOS) && ((double)lowestHigh >= highCMOS))
                DevicesUnderTest.getInstance().set_icType("CMOS");
        } else
            DevicesUnderTest.getInstance().set_icType("FAIL");
    }


    /*  The following method is fired everytime the Android app receives serial data (from Arduino tester)
        It decodes the received string and splits the result into valuable data.
     */

    private void decodeDataFromTester(String data) {

        mDataField.setText(data);
        appendSerialBuffer(data,false);

        if (data.equals("STRTOK")) {
            if (!waitForTesterToRespond.isShutdown())
                waitForTesterToRespond.shutdown();
            txt_testerStatus.setText(" Responding");
            //appendTestLogs("", true);
            btn_TEST.setEnabled(true);
        } else if ((data.equals("TESTOK")) || (data.equals("TESTSW"))) {
            appendTestLogs("Tester Device Ready.", false);
            appendTestLogs("Auto-Detecting Device...", false);
            sendCode("FTPRNT");
        } else if (data.contains("FP")) {
            DevicesUnderTest.getInstance().clear_dutICs();
            DevicesUnderTest.getInstance().reset_dutIndex();
            int footPrint = Integer.parseInt(data.replace("FP", ""));
            appendTestLogs("Foot Print : " + String.valueOf(footPrint), false);

            for (IC ic : DeviceList.supportedDevices) {
                if (ic.get_detect() == footPrint)
                    DevicesUnderTest.getInstance().add_dutIc(new IcUnderTest(ic));
            }
            appendTestLogs(DevicesUnderTest.getInstance().get_dutICs().size() + " matches found.", false);

            final List<String> icTestID = new ArrayList<String>();
            icTestID.add("IDC" + String.valueOf(DevicesUnderTest.getInstance().get_dutICs().size()));
            for (IcUnderTest icUT : DevicesUnderTest.getInstance().get_dutICs())
                icTestID.add("ID" + String.valueOf(icUT.get_icUT().get_id()));
            icTestID.add("IDOK");

            int index=0;
            for(final String spec : icTestID) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        appendTestLogs("Sending : " + spec, false);
                        sendCode(spec);
                    }
                }, index*100);  // 100 milliseconds interval between send
                index++;
            }
        } else if (data.equals("TESTRT")) {
            appendTestLogs("Testing " + DevicesUnderTest.getInstance().getCurrent_dutIc().get_icUT().get_name() + "...", false);
        } else if (data.contains("DIG")) {
            int result = Integer.parseInt(data.replace("DIG", ""));
            DevicesUnderTest.getInstance().getCurrent_dutIc().add_digital(result);
        } else if (data.contains("ANL")) {
            int result = Integer.parseInt(data.replace("ANL", ""));
            DevicesUnderTest.getInstance().getCurrent_dutIc().add_analogLow(result);
        } else if (data.contains("ANH")) {
            int result = Integer.parseInt(data.replace("ANH", ""));
            DevicesUnderTest.getInstance().getCurrent_dutIc().add_analogHigh(result);
        } else if (data.equals("ICOK")) {
            String result = "Digital Readings : ";
            for (Integer val : DevicesUnderTest.getInstance().getCurrent_dutIc().get_digital())
                result += val + "  ";
            appendTestLogs(result, false);
            appendTestLogs(DevicesUnderTest.getInstance().getCurrent_dutIc().get_icUT().get_name() + " test finished.", false);
            DevicesUnderTest.getInstance().next_dutIc();
        } else if (data.equals("TESTFIN")) {

            /** This block of code searches through all footprint-matched ICs and compare their reading from its reference values.
             If it doesn't match, it removes the IC from the List. Usually there should only be one IC remaining except for those
             ICs that has the same logic function (a TTL and a CMOS with the same logic and IO pins mapping) **/
            if (!DevicesUnderTest.getInstance().get_dutICs().isEmpty())
                appendTestLogs("Comparing results...",false);
            Iterator<IcUnderTest> iter = DevicesUnderTest.getInstance().get_dutICs().iterator();
            while (iter.hasNext()) {
                IcUnderTest icUT = iter.next();
                if (icUT.get_icUT().get_reference().length == icUT.get_digital().size()) {
                    for (int i=0; i<icUT.get_icUT().get_reference().length; i++) {
                        if (icUT.get_icUT().get_reference()[i] != icUT.get_digital().get(i)) {
                            iter.remove();
                            break;
                        }
                    }
                } else
                    iter.remove();
            }

            /** This displays the logic-matched ICs on the Log Screen (displays None if nothing matched) **/
            String logicMatch = "Logic Matches : ";
            if (DevicesUnderTest.getInstance().get_dutICs().isEmpty()) {
                logicMatch += " None";
                appendTestLogs(logicMatch, false);
                DevicesUnderTest.getInstance().set_icType("NONE");
            } else {
                for (IcUnderTest icUT : DevicesUnderTest.getInstance().get_dutICs())
                    logicMatch += icUT.get_icUT().get_name() + "  ";
                appendTestLogs(logicMatch, false);
                icCategorizer();
            } appendTestLogs("Type : " + DevicesUnderTest.getInstance().get_icType(), false);

            /** This block of code searches through all logic-matched ICs and removes all ICs that is not of matching type (CMOS or TTL)
             After this process, there should only be 1 remaining IC in the list and if not, there is a problem with the IC database **/
            Iterator<IcUnderTest> filterType = DevicesUnderTest.getInstance().get_dutICs().iterator();
            while (filterType.hasNext()) {
                IcUnderTest icUT = filterType.next();
                if (!icUT.get_icUT().get_type().equals(DevicesUnderTest.getInstance().get_icType()))
                    filterType.remove();
            }

            /** This sends the RGB indicator code that sets the behavior of the Arduino's RGB LED indicator
             No IC in the list means that the device failed or is not supported. One IC means the device passed.
             Any other result (probably more than one) is caused by a possible duplicate or error in the database **/
            String rgbCode = "RGB";
            switch (DevicesUnderTest.getInstance().get_dutICs().size()) {
                case 0 :    rgbCode += "2";     // 2 is the Arduino's RGB code for device failed (RED)
                            break;
                case 1 :    rgbCode += "1";     // 1 is the Arduino's RGB code for device passed (GREEN)
                            break;
                default:    rgbCode += "5";     // 5 is the Arduino's RGB code for fatal error (FAST BLINKING RED)
                            break;
            } sendCode(rgbCode);

            /** Displays the TEST RESULT window in another activity. onStopExemption prevents the app from
             losing connection since opening a new activity initiates onPause() causing the app to disconnect */
            onStopExemption = false;
            Intent intent = new Intent("com.ehdiwow.ble.digitalictester.TestResultActivity");
            startActivity(intent);

            btn_TEST.setEnabled(true);      // enables the TEST button after closing the TEST RESULTS window
        } else if (data.contains("OFFL")) {
            appendTestLogs("", true);
            appendTestLogs("Tester found but is currently in offline mode.\n" +
                    "Restart tester to connect with this app.", false);
        }
    }
}


