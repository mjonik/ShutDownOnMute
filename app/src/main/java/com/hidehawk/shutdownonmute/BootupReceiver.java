package com.hidehawk.shutdownonmute;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class BootupReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        // register AlarmReceiver after Bootup, if application is enabled

        SharedPreferences prefs = context.getSharedPreferences(Config.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        boolean enabled = prefs.getBoolean(Config.PREF_ENABLED, Config.DEFAULT_ENABLED);

        Log.i(BootupReceiver.class.getName(), "onReceive(): BOOT_COMPLETED, enabled=" + enabled);

        if (enabled)
            AlarmReceiver.register(context);
    }
}
