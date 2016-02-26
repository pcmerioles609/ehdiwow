package com.ehdiwow.ble.digitalictester;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class TestResultActivity extends Activity {

    private boolean showArrayValues = false;            // FOR TESTING PURPOSES -- to view the array content
    private String s = "";
    private LinearLayout parentLayout;
    private ImageView imgFail;
    private Button btn_OK;
    private int testCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);
        getActionBar().setDisplayHomeAsUpEnabled(false);


        TextView txt_deviceName = (TextView)findViewById(R.id.txtResDutName);
        txt_deviceName.setTypeface(null, Typeface.BOLD);
        TextView txt_deviceDesc = (TextView)findViewById(R.id.txtResDutDesc);
        txt_deviceDesc.setTypeface(null,Typeface.ITALIC);
        TextView txt_passFail = (TextView)findViewById(R.id.txtPassFail);
        txt_passFail.setTypeface(null,Typeface.BOLD);

        TextView txt_test = (TextView)findViewById(R.id.txtTest);

        btn_OK = (Button)findViewById(R.id.btnOK);
        parentLayout = (LinearLayout)findViewById(R.id.linear_results);
        imgFail = (ImageView)findViewById(R.id.image_fail);
        onConfirmTestResults();

        /** This switch statement tells what will the RESULT ACTIVITY window displays based on the number of IC in the DUT List **/
        switch (DevicesUnderTest.getInstance().get_dutICs().size()) {
            // 0 means no IC left in the DUT List which means the device either failed or is not supported
            case 0 :    txt_deviceName.setText("FAILED");
                        txt_deviceDesc.setText("OR DEVICE NOT SUPPORTED");
                        txt_passFail.setText(R.string.txt_resultFAIL);
                        txt_passFail.setTextColor(Color.RED);
                        imgFail.setImageResource(R.drawable.imgfail);
                        break;
            // 1 means there is only one IC in the DUT List which means the device passed
            case 1 :    txt_deviceName.setText((DevicesUnderTest.getInstance().get_dutICs().get(0).get_icUT().get_name()));
                        txt_deviceDesc.setText((DevicesUnderTest.getInstance().get_dutICs().get(0).get_icUT().get_description()));
                        txt_passFail.setText(R.string.txt_resultPASS);
                        txt_passFail.setTextColor(Color.GREEN);

                        int inputCount = DevicesUnderTest.getInstance().get_dutICs().get(0).get_icUT().get_inputCount();
                        int outputCount = DevicesUnderTest.getInstance().get_dutICs().get(0).get_icUT().get_outputCount();
                        testCount = DevicesUnderTest.getInstance().get_dutICs().get(0).get_icUT().get_testCount();

                        byte[][] decodedTestInputs = new byte[testCount][inputCount];
                        byte[][] decodedMeasuredOutputs = new byte[testCount][outputCount];
                        int[] testInputs = DevicesUnderTest.getInstance().get_dutICs().get(0).get_icUT().get_testInputs();
                        List<Integer> measuredOutList = DevicesUnderTest.getInstance().get_dutICs().get(0).get_digital();

                        int[] measuredOutputs = new int[measuredOutList.size()];
                        for(int i=0; i<measuredOutList.size(); i++)
                            measuredOutputs[i] = measuredOutList.get(i);

                        decodedTestInputs = decoder(testInputs, inputCount);
                        decodedMeasuredOutputs = decoder(measuredOutputs, outputCount);

                        // FOR TESTING PURPOSES -- shows the array values for testing purposes
                        if (showArrayValues) {
                            for (byte[] x : decodedTestInputs) {
                                for (byte y : x) {
                                    s += String.valueOf(y);
                                } s += "\n";
                            }
                            for (byte[] x : decodedMeasuredOutputs) {
                                for (byte y : x) {
                                    s += String.valueOf(y);
                                } s += "\n";
                            }
                            txt_test.setText(s);
                        }

                        waveFormPainter(DevicesUnderTest.getInstance().get_dutICs().get(0).get_icUT().get_inputPins(), decodedTestInputs);
                        waveFormPainter(DevicesUnderTest.getInstance().get_dutICs().get(0).get_icUT().get_outputPins(), decodedMeasuredOutputs);
                        break;
            // any value other than 0 or 1 means there is a problem in the database
            default:    txt_deviceName.setText("FATAL ERROR");
                        txt_deviceDesc.setText("DUPLICATE FOUND IN THE DATABASE");
                        String dup = "FOUND : ";
                        for (IcUnderTest icUT : DevicesUnderTest.getInstance().get_dutICs())
                            dup += icUT.get_icUT().get_name() + " ";
                        txt_passFail.setText(dup);
                        break;
        }
    }

    private void onConfirmTestResults() {                           // closes this Activity when OK is pressed
        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private byte[][] decoder(int[] enCoded, int bitCount) {         // this method converts an array of int to a
        byte decodedValues[][] = new byte[testCount][bitCount];     // two-dimensional array of bytes (0s and 1s)
        for (int i=0; i<enCoded.length; i++) {
            byte decodedBits[] = new byte[bitCount];
            int ctr = 0;
            int temp = enCoded[i];
            do {
                decodedBits[(bitCount-1)-ctr] = (byte)(temp%2);
                temp /= 2;
                ctr++;
            } while (ctr < bitCount);
            decodedValues[i] = decodedBits;
        } 
        return decodedValues;
    }

    private void waveFormPainter(PIN[] pins, byte[][] values) {         // this method creates the PIN NAMES and their
        LinearLayout pinLayouts[] = new LinearLayout[pins.length];      // respective binary waveforms
        for (int i=0; i<pins.length; i++) {
            pinLayouts[i] = new LinearLayout(this);
            pinLayouts[i].setId(i);
            pinLayouts[i].setOrientation(LinearLayout.HORIZONTAL);

            TextView pinName = new TextView(this);
            pinName.setText(pins[i].get_name());
            pinName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            pinName.setPadding(20,20,0,0);       // top padding to align with the waveform graph
            pinLayouts[i].addView(pinName);

            byte[] pinBits = new byte[values.length];
            for (int j=0; j<values.length; j++)
                pinBits[j] = values[j][i];

            if (showArrayValues) {              // shows each pin waveform in binary for test purposes
                String str = "";                // to make sure the binary distribution is accurate
                for (byte x : pinBits) {
                    str += String.valueOf(x);
                }

                TextView pinValues = new TextView(this);
                pinValues.setText(str);
                pinValues.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                pinValues.setPadding(20, 0, 0, 0);
                pinLayouts[i].addView(pinValues);
            }

            LineChart wave = new LineChart(this);               // creates a new line chart
            wave.setDescription("");                            // hides the description
            wave.setDrawGridBackground(false);                  // hides background
            wave.setTouchEnabled(false);                        // disables any user interaction with the chart
            wave.setMinimumWidth(335);                          // TODO -- SET WIDTH DYNAMICALLY
            wave.setPadding(20,0,0,0);                          // left padding set for allowance

            wave.getXAxis().setEnabled(false);                  // removes all axes,grid lines and labels
            wave.getAxisLeft().setEnabled(false);
            wave.getAxisRight().setEnabled(false);
            wave.getLegend().setEnabled(false);                 // hides legend

            ArrayList<Entry> entries = new ArrayList<Entry>();
            ArrayList<String> labels = new ArrayList<String>();
            for (int k=0; k<pinBits.length; k++) {
                entries.add(new Entry(pinBits[k],k));
                labels.add("");
                entries.add(new Entry(pinBits[k],k+1));
                labels.add("");
            }
            LineDataSet dataSet = new LineDataSet(entries,"");
            dataSet.setDrawCircles(false);
            dataSet.setColor(Color.BLUE);
            dataSet.setLineWidth(3);
            dataSet.setDrawValues(false);
            LineData data = new LineData(labels,dataSet);
            wave.setData(data);
            pinLayouts[i].addView(wave);

            parentLayout.addView(pinLayouts[i]);
        }
    }
}
