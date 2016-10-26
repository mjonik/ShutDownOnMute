package com.hidehawk.shutdownonmute;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BootupReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        // register AlarmReceiver after Bootup, if application is enabled

        SharedPreferences prefs = context.getSharedPreferences(Config.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        boolean enabled = prefs.getBoolean(Config.PREF_ENABLED, Config.DEFAULT_ENABLED);
        boolean lockEnabled = prefs.getBoolean(Config.PREF_LOCK_AFTER_BOOT, false);
        boolean launchSpotify = prefs.getBoolean(Config.PREF_LAUNCH_SPOTIFY_AFTER_BOOT, false);

        Log.i(BootupReceiver.class.getName(), "onReceive(): BOOT_COMPLETED, enabled=" + enabled + ", lockAfterBoot=" + lockEnabled + ", launchSpotify=" + launchSpotify);

        if (enabled) {
            AlarmReceiver.register(context);
        }

        if (lockEnabled) {
            PackageManager pm = context.getPackageManager();
            Intent startIntent = pm.getLaunchIntentForPackage("com.spotify.music");
            context.startActivity(startIntent);
        }

        if (launchSpotify) {
            try {
                Process p = Runtime.getRuntime().exec(new String[]{"su"});
                OutputStream out = p.getOutputStream();
                DataOutputStream dataOut = new DataOutputStream(out);
                dataOut.writeBytes("input keyevent 26\n");
                dataOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
