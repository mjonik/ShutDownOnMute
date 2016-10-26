package com.hidehawk.shutdownonmute;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.BatteryManager;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class AlarmReceiver extends BroadcastReceiver {

    public static void register(Context context) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000, pendingIntent);
        Log.i(AlarmReceiver.class.getName(), "AlarmReceiver registered");
    }


    // this static variable will be reset to zero every time the user closes the MainActivity.
    private static long muteTimeCounter = 0;

    @Override
    public void onReceive(Context context, Intent intent) {

        BatteryManager batteryManager = (BatteryManager)context.getSystemService(Context.BATTERY_SERVICE);

        AudioManager audioManger = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);

        boolean musicActive = audioManger.isMusicActive();

        // read muteTime from prefs
        SharedPreferences prefs = context.getSharedPreferences(Config.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        int muteTime = prefs.getInt(Config.PREF_MUTE_TIME, Config.DEFAULT_MUTE_TIME);
        boolean enabled = prefs.getBoolean(Config.PREF_ENABLED, Config.DEFAULT_ENABLED);

        // create intent to pass to UI
        Intent guiIntent = new Intent();

        if (musicActive) {
            // if music is active, reset muteTimeCounter
            muteTimeCounter = 0;
        }
        else if (enabled){
            // count
            muteTimeCounter++;

            if (muteTimeCounter >= muteTime) {
                // configured muteTime exceeded

                try {
                    Log.i(AlarmReceiver.class.getName(), "MuteTime exceeded. Powering off...");

                    // Run su command and pipe "svc power shutdown"
                    Process p = Runtime.getRuntime().exec(new String[] {"su"});
                    OutputStream out = p.getOutputStream();
                    DataOutputStream dataOut = new DataOutputStream(out);
                    dataOut.writeBytes("svc power shutdown\n");
                    dataOut.flush();

                } catch (Exception e) {
                    Log.e(AlarmReceiver.class.getName(), e.getMessage());

                    // pass error message to UI
                    guiIntent.putExtra(Config.INTENT_FIELD_ERROR_MESSAGE, e.getMessage());
                }

            }
        }

        // log the status
        Log.i(AlarmReceiver.class.getName(), "musicActive=" + musicActive + ", muteTimeCounter=" + muteTimeCounter + ", muteTime=" + muteTime + ", enabled=" + enabled);


        // send intent to gui
        guiIntent.setAction(Config.INTENT_ACTION_GUI_UPDATE);
        guiIntent.putExtra(Config.INTENT_FIELD_MUSIC_ACTIVE, audioManger.isMusicActive());
        guiIntent.putExtra(Config.INTENT_FIELD_MUTE_TIME_COUNTER, muteTimeCounter);
        context.sendBroadcast(guiIntent);


    }
}
