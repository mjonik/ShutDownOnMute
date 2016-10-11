package com.hidehawk.shutdownonmute;

/**
 * Created by jonik on 10.10.2016.
 */
public class Config {

    public static final String SHARED_PREFS_FILE = "ShutdownOnMute";
    public static final String PREF_MUTE_TIME = "muteTime";
    public static final int DEFAULT_MUTE_TIME = 10;
    public static final String PREF_ENABLED = "enabled";
    public static final boolean DEFAULT_ENABLED = false;


    public static final String INTENT_ACTION_GUI_UPDATE = "com.hidehawk.action.GUI_UPDATE";
    public static final String INTENT_FIELD_MUSIC_ACTIVE = "musicActive";
    public static final String INTENT_FIELD_ERROR_MESSAGE = "errorMessage";
    public static final String INTENT_FIELD_MUTE_TIME_COUNTER = "muteTimeCounter";


}
