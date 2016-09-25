package com.fate.android.doubletap2wakeangler;

import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    // Initialize constants for isDT2WEnabled() SharedPreferences
    private static final String SAVED_STATE = "DT2W_STATE";

    private TextView deviceAngler;
    private TextView DT2W_STATE;
    private Button enableDT2W;
    private Button disableDT2W;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceAngler = (TextView) findViewById(R.id.device_angler);
        DT2W_STATE = (TextView) findViewById(R.id.dt2w_state);
        enableDT2W = (Button) findViewById(R.id.enable_DT2W);
        disableDT2W = (Button) findViewById(R.id.disable_DT2W);

        /**
         * Show buttons only on Angler devices.
         * /shruggie if device is not an Angler.
         */
        if (isAngler()) {
            enableDT2W.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DT2W(true);
                    DT2WState(true);
                    DT2W_STATE.setText(R.string.dt2w_state_enabled);
                }
            });
            disableDT2W.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DT2W(false);
                    DT2WState(false);
                    DT2W_STATE.setText(R.string.dt2w_state_disabled);
                }
            });
        } else {
            enableDT2W.setVisibility(View.INVISIBLE);
            disableDT2W.setVisibility(View.INVISIBLE);
            deviceAngler.setText(R.string.device_not_angler);
        }
    }

    /**
     * Checks if device is an Angler.
     *
     * @return boolean
     */
    private boolean isAngler() {
        return Build.DEVICE.equals("angler");
    }

    /**
     * Enable or disable double tap to wake for Angler.
     *
     * @param enable
     */
    private void DT2W(boolean enable) {
        String command = "echo " + (enable ? "1" : "0") +
                " > /sys/devices/soc.0/f9924000.i2c/i2c-2/2-0070/input/input0/wake_gesture";
        RootUtil.runAsRoot(command);
    }

    private boolean DT2WState(boolean isEnabled) {
        /**
         * Access the shared preferences file that's identified by the resource
         * string R.string.PREFERENCES_KEY and open it using the private mode so
         * the file is accessible by only this app.
         */
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        /**
         * Assign a boolean value into SAVED_STATE
         */
        prefs.edit().putBoolean(SAVED_STATE, isEnabled).apply();

        /**
         * Retrieve a boolean value from the SAVED_STATE
         * If SERVICE_STATUS_FLAG does not exist, set to false.
         */
        boolean savedState = prefs.getBoolean(SAVED_STATE, false);
        Log.d("MainActivity", "SAVED_STATE prefs: " + savedState);
        return savedState;
    }
}
