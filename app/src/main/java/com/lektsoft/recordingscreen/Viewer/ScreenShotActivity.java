package com.lektsoft.recordingscreen.Viewer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.lektsoft.recordingscreen.R;
import com.lektsoft.recordingscreen.Utils.AppSettings;
import com.lektsoft.recordingscreen.Utils.Controller;
import com.lektsoft.recordingscreen.Utils.ScreenCapture;

public class ScreenShotActivity extends AppCompatActivity {

    private static final String TAG = "ScreenShotActivity";

    public final int REQUEST_CODE_SCREENSHOT = 1110;
    public ScreenCapture screenCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rs_activity_screen_shot);
        AppSettings appSettings = new AppSettings(this);
        screenCapture = new ScreenCapture(this, appSettings.getImageStoragePath());
    }
    @Override
    protected void onResume() {
        super.onResume();
        startActivityForResult(screenCapture.createScreenCaptureIntent(), REQUEST_CODE_SCREENSHOT);
    }

    //스크린샷 버튼을 눌렀을 때 나오는 화면
    //권한 문제 때문에 계속 화면이 살짝 어둡게 나와서 postDelayed로 시간차를 주고 스크린샷을 촬영합니다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCREENSHOT) {
            if (resultCode == RESULT_OK) {
                stopService(new Intent(this, Controller.class));
                Handler handler=new Handler(getMainLooper());
                final Intent datas=data;
                final int result=resultCode;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        screenCapture.takeScreenshot(result, datas);
                    }
                },1000);//시간은 여기서 수정해주세요.
            }
            else {
                startService(new Intent(this, Controller.class));
            }
        }
        finish();
    }
}
