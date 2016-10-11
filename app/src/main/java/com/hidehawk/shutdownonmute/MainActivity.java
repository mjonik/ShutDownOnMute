package com.hidehawk.shutdownonmute;

import android.app.Activity;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends Activity {

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // receive UI Intent and update fields with the values the AlarmReceiver has prepared

            TextView counter = (TextView)findViewById(R.id.muteTimeCounter);
            counter.setText(""+intent.getLongExtra(Config.INTENT_FIELD_MUTE_TIME_COUNTER, 0));

            TextView musicPlaying = (TextView)findViewById(R.id.musicPlaying);
            musicPlaying.setText(""+intent.getBooleanExtra(Config.INTENT_FIELD_MUSIC_ACTIVE, false));

            TextView errorMessage = (TextView)findViewById(R.id.errorMessage);
            errorMessage.setText(intent.getStringExtra(Config.INTENT_FIELD_ERROR_MESSAGE));



        }
    };

    private IntentFilter intentFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // load preferences
        SharedPreferences prefs = getSharedPreferences(Config.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        int muteTimeValue = prefs.getInt(Config.PREF_MUTE_TIME, Config.DEFAULT_MUTE_TIME);
        boolean enabled = prefs.getBoolean(Config.PREF_ENABLED, Config.DEFAULT_ENABLED);

        // set enabled
        Switch enabledSwitch = (Switch)findViewById(R.id.enabledSwitch);
        enabledSwitch.setChecked(enabled);

        // set mute time seekbar and label
        final TextView muteTime = (TextView) findViewById(R.id.muteTime);
        muteTime.setText("" + muteTimeValue);
        SeekBar muteTimeSeekBar = (SeekBar)findViewById(R.id.muteTimeSeekBar);
        muteTimeSeekBar.setProgress(muteTimeValue);


        // EnabledSwitch actions
        enabledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences prefs = getSharedPreferences(Config.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = prefs.edit();
                prefsEditor.putBoolean(Config.PREF_ENABLED, isChecked);
                prefsEditor.commit();
            }
        });


        // Seekbar actions
        muteTimeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView muteTime = (TextView)findViewById(R.id.muteTime);
                muteTime.setText("" + (progress+1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // store mute time in preferences

                SharedPreferences prefs = getSharedPreferences(Config.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = prefs.edit();
                int muteTimeValue = seekBar.getProgress() + 1;
                prefsEditor.putInt(Config.PREF_MUTE_TIME, muteTimeValue);
                prefsEditor.commit();
                Log.i(MainActivity.class.getName(), "store mute time = " + muteTimeValue);
            }
        });

        // receive update-gui intents
        intentFilter = new IntentFilter();
        intentFilter.addAction(Config.INTENT_ACTION_GUI_UPDATE);
        registerReceiver(receiver, intentFilter);

        // register AlarmReceiver
        AlarmReceiver.register(this);

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}

