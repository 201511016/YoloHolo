package com.lektsoft.recordingscreen;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.lektsoft.recordingscreen.Utils.Controller;
import com.lektsoft.recordingscreen.Utils.CustomNotification;
import com.lektsoft.recordingscreen.Utils.ForegroundService;
import com.lektsoft.recordingscreen.Utils.AppSettings;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import butterknife.ButterKnife;

public class SettingsSystem extends AppCompatActivity {

    private static final String TAG = "SettingsSystem";

    private Context mContext= SettingsSystem.this;

    //Permission List
    private final String[] permissions = new String[] {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.SYSTEM_ALERT_WINDOW
    };


    private final int REQUEST_PERMISSION_CODE = 1001;

    private final int REQUEST_PERMISSION_OVERLAY_CODE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rs_activity_main);
        ButterKnife.bind(this);

        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) !=
                PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) !=
                        PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) !=
                        PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(permissions, REQUEST_PERMISSION_CODE);
        }
        else {
            initStorageFolder();
            startNotification();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int result : grantResults)
            if (result == PackageManager.PERMISSION_DENIED)
                finish();

        if (!Settings.canDrawOverlays(this)){
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_PERMISSION_OVERLAY_CODE);
        }
        else {
            initStorageFolder();
            startNotification();
        }
    }

    //Permission이 완료되면 저장소에 폴더를 생성합니다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_OVERLAY_CODE){
            initStorageFolder();
        }
    }


    //Android Q 부터 Service를 통해 MediaProjection을 제어해야합니다 따라서 Notification을 커스텀 한 화면을 생성하고 서비스를 시작합니다.
    private void startNotification() {
        CustomNotification notificationCustom = CustomNotification.getInstance(getApplication());
        notificationCustom.registerNotificationChanel();
        notificationCustom.show(notificationCustom.MAIN_LAYOUT);
        startService(new Intent(this, Controller.class));
        startService(new Intent(this, ForegroundService.class));
        finish();
    }

    //스크린샷과 녹화파일을 저장 할 폴더를 설정합니다.
    private void initStorageFolder() {
        AppSettings settingsApp = new AppSettings(this);
        File folder = new File(settingsApp.getImageStoragePath());

        if (!folder.exists()){
            if (folder.mkdirs())
                Log.d(TAG, "initStorageFolder: Create folder");
            else
                Log.d(TAG, "initStorageFolder: Fail to create folder");
        }
    }
}
