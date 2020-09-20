package com.photoeditorsdk.android.app.Fragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import android.preference.PreferenceFragment;

import com.photoeditorsdk.android.app.R;

public class SettingsFragment extends PreferenceFragment {

    public static final String KEY_RESOLUTION = "Resolution";
    public static final String KEY_FPS = "Fps";
    public static final String KEY_BITRATE = "BitRate";
    public static final String KEY_ORIENTATION = "Orientation";
    public static final String KEY_DISPLAY_TIMER = "DisplayTimer";
    public static final String KEY_STORAGE = "edit_storage";

    public SettingsFragment(){

    }@Override
    public void onCreate(Bundle savedInstanceState, String rootKey) {
        super.onCreate(savedInstanceState);
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
