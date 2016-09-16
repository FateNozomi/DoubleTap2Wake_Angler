package com.fate.android.doubletap2wakeangler;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView deviceAngler;
    private Button enableDT2W;
    private Button disableDT2W;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceAngler = (TextView) findViewById(R.id.device_angler);
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
                }
            });
            disableDT2W.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DT2W(false);
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
}
