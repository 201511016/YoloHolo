package com.lektsoft.recordingscreen.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

import com.lektsoft.recordingscreen.Fragment.SettingsFragment;
import com.lektsoft.recordingscreen.R;

public class AppSettings {

    private static final String TAG = "AppSettings";

    private int quality;
    private int fps;
    private int bitrate;
    private int orientation;
    private String videoStoragePath;
    private String imageStoragePath;
    private boolean isDisplayTimer;

    //앱의 전체적인 설정을 관리합니다.
    //레이아웃 수정은 xml폴더에서 preferences를 수정해주세요.
    public AppSettings(Context context) {
        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        //화면 해상도
        quality = Integer.parseInt(
                sharedPref.getString(SettingsFragment.KEY_RESOLUTION,  ""));

        //화면 프레임을 설정합니다.
        fps = Integer.parseInt(
                sharedPref.getString(SettingsFragment.KEY_FPS,  ""));

        //비트레이트
        bitrate = Integer.parseInt(
                sharedPref.getString(SettingsFragment.KEY_BITRATE,  ""));

        //화면 방향
        orientation = Integer.parseInt(
                sharedPref.getString(SettingsFragment.KEY_ORIENTATION,  ""));

        //화면 녹화 저장공간
        videoStoragePath = sharedPref.getString(SettingsFragment.KEY_STORAGE,  "");

        //스크린샷은 화면 녹화와 분리하기 위해 뒤에 /Img를 추가했습니다.
        imageStoragePath = videoStoragePath.concat("/Img");

        //화면 녹화중인 시간을 나타냅니다.
        isDisplayTimer = sharedPref.getBoolean(SettingsFragment.KEY_DISPLAY_TIMER, false);
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public int getFps() {
        return fps;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public int getBitrate() {
        return bitrate;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public String getVideoStoragePath() {
        return videoStoragePath;
    }

    public void setVideoStoragePath(String videoStoragePath) {
        this.videoStoragePath = videoStoragePath;
    }

    public String getImageStoragePath() {
        return imageStoragePath;
    }

    public void setImageStoragePath(String imageStoragePath) {
        this.imageStoragePath = imageStoragePath;
    }

    public boolean isDisplayTimer() {
        return isDisplayTimer;
    }

    public void setDisplayTimer(boolean displayTimer) {
        isDisplayTimer = displayTimer;
    }
}
