package com.lektsoft.recordingscreen.Utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class ForegroundService extends Service {

    private static final String TAG = "ForegroundService";

    private static CustomNotification notificationCustom;


    /**
     * 해당 파일은 Status바에 띄워지는 Notification의 서비스파일입니다.
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        notificationCustom = CustomNotification.getInstance(getApplication());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(notificationCustom.NOTIFICATION_ID,
                    notificationCustom.createNotification(notificationCustom.MAIN_LAYOUT),
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION);
        }
        else
            startForeground(notificationCustom.NOTIFICATION_ID,
                    notificationCustom.createNotification(notificationCustom.MAIN_LAYOUT));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static void startForegroundService(Context context) {
        Intent intent = new Intent(context, ForegroundService.class);
        context.startService(intent);
    }
}
