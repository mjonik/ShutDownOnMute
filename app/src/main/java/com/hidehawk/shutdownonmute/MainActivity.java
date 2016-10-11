package com.hidehawk.shutdownonmute;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            TextView counter = (TextView)findViewById(R.id.counter);
            counter.setText(""+intent.getLongExtra("muteTimeCounter", 0));

            //Log.i("BroadCastGuiReceiver","intent=" + intent);

            TextView musicPlaying = (TextView)findViewById(R.id.musicPlaying);
            musicPlaying.setText(""+intent.getBooleanExtra("musicActive", false));

        }
    };

    private IntentFilter intentFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // load muteTime
        SharedPreferences prefs = getSharedPreferences(Config.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        int muteTimeValue = prefs.getInt(Config.PREF_MUTE_TIME, Config.DEFAULT_MUTE_TIME);
        EditText muteTime = (EditText)findViewById(R.id.muteTime);
        muteTime.setText("" + muteTimeValue);


        // OK Button
        Button okButton =  (Button)findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // store mute time in preferences
                EditText muteTime = (EditText)findViewById(R.id.muteTime);
                SharedPreferences prefs = getSharedPreferences(Config.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = prefs.edit();
                String muteTimeValue = muteTime.getText().toString();
                if (muteTimeValue.length() > 0) {
                    prefsEditor.putInt(Config.PREF_MUTE_TIME, Integer.parseInt(muteTimeValue));
                    prefsEditor.commit();
                    Log.i("SOM/MainActivity", "store mute time = " + muteTimeValue);
                }
            }
        });

        // receive update-gui intents
        intentFilter = new IntentFilter();
        intentFilter.addAction("update-gui");
        registerReceiver(receiver, intentFilter);


        AlarmReceiver.register(this);

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}

