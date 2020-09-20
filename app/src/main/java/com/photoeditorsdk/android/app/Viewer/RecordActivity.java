package com.photoeditorsdk.android.app.Viewer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.photoeditorsdk.android.app.Utils.AppSettings;
import com.photoeditorsdk.android.app.Utils.Controller;
import com.photoeditorsdk.android.app.Utils.CustomNotification;
import com.photoeditorsdk.android.app.Utils.ScreenCapture;
import com.photoeditorsdk.android.app.Utils.TimeRecord;

public class RecordActivity extends AppCompatActivity {
    
    private static final String TAG = "RecordActivity";

    private final int REQUEST_CODE_RECORD = 120;
    private ScreenCapture screenCapture;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenCapture = ScreenCapture.getInstance(this);
    }
    @Override
    protected void onStart() {
        super.onStart();
        Intent collapseNotification = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        sendBroadcast(collapseNotification);

        startActivityForResult(screenCapture.createScreenCaptureIntent(), REQUEST_CODE_RECORD);
    }


    //화면 녹화 버튼을 누른 후 일어나는 이벤트들

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RECORD) {
            if (resultCode == RESULT_OK) {
                AppSettings settings = new AppSettings(this);
                CustomNotification notificationCustom = CustomNotification.getInstance(getApplication());
                notificationCustom.updateVisibilityOfTimer(settings.isDisplayTimer() ?
                        View.VISIBLE : View.GONE);
                notificationCustom.show(notificationCustom.PROGRESS_LAYOUT);

                Intent intent = new Intent(this, Controller.class);
                stopService(intent);

                TimeRecord timeRecord = TimeRecord.getInstance();
                timeRecord.start();


                screenCapture.prepareRecorder(this, settings.getQuality(),
                        settings.getFps(), settings.getBitrate(),
                        settings.getOrientation(), settings.getVideoStoragePath());

                screenCapture.startRecord(resultCode, data);

            } else
                startService(new Intent(this, Controller.class));
            finish();
        }
    }
}
