package com.hidehawk.shutdownonmute;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by jonik on 30.09.2016.
 */
public class ShutdownOnMuteService extends IntentService {

    public ShutdownOnMuteService() {
        super("ShutdownOnMuteService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
