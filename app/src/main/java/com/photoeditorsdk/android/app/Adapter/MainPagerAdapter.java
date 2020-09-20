package com.photoeditorsdk.android.app.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.photoeditorsdk.android.app.Fragment.ScreenshotFragment;
import com.photoeditorsdk.android.app.Fragment.SettingsFragment;
import com.photoeditorsdk.android.app.Fragment.VideoFragment;

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "MainPagerAdapter";

    private int numOfTab;
    public MainPagerAdapter(@NonNull FragmentManager fm, int behavior, int numOfTab) {
        super(fm, behavior);
        this.numOfTab = numOfTab;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                /*return new SettingsFragment();*/
            case 1:
                return new VideoFragment();
            case 2:
                return new ScreenshotFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTab;
    }
}
