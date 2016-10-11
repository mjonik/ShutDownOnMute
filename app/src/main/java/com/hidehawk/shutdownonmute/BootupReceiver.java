package com.hidehawk.shutdownonmute;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootupReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        // register AlarmReceiver after Bootup

        Log.i(BootupReceiver.class.getName(), "onReceive(): BOOT_COMPLETED");

        AlarmReceiver.register(context);
    }
}
