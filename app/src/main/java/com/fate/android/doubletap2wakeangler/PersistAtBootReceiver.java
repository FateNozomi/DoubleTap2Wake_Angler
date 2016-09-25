package com.fate.android.doubletap2wakeangler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;


/**
 * Persists action upon detecting Intent.ACTION_BOOT_COMPLETED
 */
public class PersistAtBootReceiver extends BroadcastReceiver {

    // Initialize constants for isDT2WEnabled() SharedPreferences
    private static final String SAVED_STATE = "DT2W_STATE";
    private static final String PREFERENCES_KEY = "com.fate.android.doubletap2wakeangler";

    public void onReceive(Context context, Intent intent) {

        final SharedPreferences prefs = context
                .getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);

        Log.d("PersistAtBoot", "PersistAtBootReceiver: " + intent.getAction());

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d("PersistAtBoot", "BOOT_COMPLETED received!");
            (new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    if (prefs.getBoolean(SAVED_STATE, false)) {
                        DT2W(true);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    Log.d("PersistAtBoot", "DT2W enabled through PersistAtBootReceiver");
                }
            }).execute();
        }
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
