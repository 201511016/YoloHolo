package com.lektsoft.recordingscreen.Fragment;

import android.os.Bundle;

import com.lektsoft.recordingscreen.R;

import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static final String KEY_RESOLUTION = "Resolution";
    public static final String KEY_FPS = "Fps";
    public static final String KEY_BITRATE = "BitRate";
    public static final String KEY_ORIENTATION = "Orientation";
    public static final String KEY_DISPLAY_TIMER = "DisplayTimer";
    public static final String KEY_STORAGE = "edit_storage";

    public SettingsFragment(){

    }@Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
