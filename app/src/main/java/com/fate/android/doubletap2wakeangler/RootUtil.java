package com.fate.android.doubletap2wakeangler;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Grants root shell commands
 */
public class RootUtil {

    /**
     * isDeviceRooted() checks for root. Through the use of shell commands, ID is sent and
     * readLine() is used to get the output of shell using DataInputStream with BufferedReader.
     * If the output contains uid=0, root access is granted.
     *
     * @return is device rooted
     */
    static public boolean isDeviceRooted() {
        boolean isRooted = false;
        try {
            java.lang.Process p = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            DataInputStream is = new DataInputStream(p.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            if (os != null && is != null) {
                // Getting the id of the current user to check if this is root
                os.writeBytes("id\n");
                os.flush();

                // Get BufferedReader tor read a line of text
                String uid = br.readLine();
                boolean exitSu = false;
                if (uid == null) {
                    isRooted = false;
                    exitSu = false;
                    Log.d("ROOT", "Can't get root access or denied by user");
                } else if (uid.contains("uid=0") == true) {
                    isRooted = true;
                    exitSu = true;
                    Log.d("ROOT", "Root access granted");
                } else {
                    isRooted = false;
                    exitSu = true;
                    Log.d("ROOT", "Root access rejected: " + uid);
                }

                if (exitSu) {
                    os.writeBytes("exit\n");
                    os.flush();
                    os.close();
                    Log.d("SHELL", "EXIT SUCCESS");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Can't get root!
            // Probably broken pipe exception on trying to write to output stream (os) after su
            // failed, meaning that the device is not rooted

            isRooted = false;
            Log.d("ROOT", "Root access rejected [" + e.getClass().getName() + "] : " + e.getMessage());
        }
        return isRooted;
    }

    /**
     * Execute shell commands as root.
     *
     * @param commands
     */
    static public void runAsRoot(String commands) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            os.writeBytes(commands + "\n");
            os.writeBytes("exit\n");
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
