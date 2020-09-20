package com.lektsoft.recordingscreen.Utils;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import com.lektsoft.recordingscreen.R;
import com.lektsoft.recordingscreen.Viewer.RecordActivity;
import com.lektsoft.recordingscreen.Viewer.ScreenShotActivity;
import com.lektsoft.recordingscreen.Viewer.SettingActivity;

import java.util.Hashtable;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class CustomNotification {

    private static final String TAG = "CustomNotification";

    private static CustomNotification instance;

    private final int REQUEST_CODE_PENDING_VIDEO = 100;
    private final int REQUEST_CODE_PENDING_SETTINGS = 101;
    private final int REQUEST_CODE_PENDING_SCREENSHOT = 102;
    private final int REQUEST_CODE_PENDING_LIBRARY = 103;
    private final int REQUEST_CODE_PENDING_EXIT = 104;
    private final int REQUEST_CODE_PENDING_STOP = 105;
    private final int REQUEST_CODE_PENDING_PAUSE = 106;

    private final String CHANEL_ID = "chanel 1";
    public final int NOTIFICATION_ID = 1001;

    private NotificationManagerCompat manager;

    public final String ACTION_EXIT = "EXIT";

    public final String ACTION_STOP = "STOP";
    public final String ACTION_PAUSE = "PAUSE";

    public final String ACTION_START_CONTROL_BAR = "CONTROL_BAR";
    public final String MAIN_LAYOUT = "MAIN";
    public final String PROGRESS_LAYOUT = "PROGRESS";

    private Hashtable<String, RemoteViews> layouts;

    private Application application;

    //notification의 버튼 별 Action을 설정합니다.
    //만약 레이아웃 디자인을 변경하고 싶으시다면 layout폴더에서 main_record_layout을 수정해주세요 지금은 임시로 만들어둔거라 색상이 영...
    private CustomNotification(final Application application){
        this.application = application;

        //Remote뷰를 생성합니다 해당 영역부터는 메인으로 보이는 Notification의 영역입니다.
        RemoteViews mainRecordLayout = new RemoteViews(application.getPackageName(),
                R.layout.rs_main_record_layout);
        Intent settingsIntent = new Intent(application, SettingActivity.class);
        settingsIntent.putExtra(SettingActivity.TAB_SELECTED, SettingActivity.TAB_SETTINGS);

        Intent libraryIntent = new Intent(application, SettingActivity.class);
        libraryIntent.putExtra(SettingActivity.TAB_SELECTED, SettingActivity.TAB_VIDEO);

        Intent recordIntent = new Intent(application, RecordActivity.class);

        Intent screenshotIntent = new Intent(application, ScreenShotActivity.class);
        screenshotIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        screenshotIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainRecordLayout.setOnClickPendingIntent(R.id.noti_controll_setting,
                createPendingIntent(settingsIntent, REQUEST_CODE_PENDING_SETTINGS));

        mainRecordLayout.setOnClickPendingIntent(R.id.noti_controll_screenshot,
                createPendingIntent(screenshotIntent, REQUEST_CODE_PENDING_SCREENSHOT));

        mainRecordLayout.setOnClickPendingIntent(R.id.noti_controll_video,
                createPendingIntent(recordIntent, REQUEST_CODE_PENDING_VIDEO));


        mainRecordLayout.setOnClickPendingIntent(R.id.noti_controll_library,
                createPendingIntent(libraryIntent, REQUEST_CODE_PENDING_LIBRARY));

        mainRecordLayout.setOnClickPendingIntent(R.id.noti_controll_exit,
                createPendingIntent(ACTION_EXIT, REQUEST_CODE_PENDING_EXIT));



        //Remote뷰를 생성합니다 해당 영역부터는 화면 녹화 버튼을 눌렀을 때 보이는 Notification의 영역입니다.
        RemoteViews inProgressLayout = new RemoteViews(application.getPackageName(),
                R.layout.in_progress_layout);

        inProgressLayout.setOnClickPendingIntent(R.id.btStop,
                createPendingIntent(ACTION_STOP, REQUEST_CODE_PENDING_STOP));
        inProgressLayout.setOnClickPendingIntent(R.id.btPause,
                createPendingIntent(ACTION_PAUSE, REQUEST_CODE_PENDING_PAUSE));

        layouts = new Hashtable<>();
        layouts.put(MAIN_LAYOUT, mainRecordLayout);
        layouts.put(PROGRESS_LAYOUT, inProgressLayout);

        manager = NotificationManagerCompat.from(application);
    }

    public void registerNotificationChanel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel chanel =
                    new NotificationChannel(CHANEL_ID, "Notification main",
                            NotificationManager.IMPORTANCE_LOW);
            chanel.setSound(null, null);
            manager.createNotificationChannel(chanel);
        }
    }

    //해당 CreatePendingIntent 함수는 화면 이동 없이 녹화중지, 녹화 일시정지에 사용되는 PendingIntent를 생성합니다.
    private PendingIntent createPendingIntent(String action, int requestCode){
        Intent intent = new Intent(application, NotificationReceiver.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(application, requestCode, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }


    //해당 CreatePendingIntent 함수는 화면 이동이 필요한 PendingIntent를 생성합니다.
    private PendingIntent createPendingIntent(Intent intent, int requestCode){
        return PendingIntent.getActivity(application, requestCode, intent,
                0);
    }

    public Notification createNotification(String layout){
        Notification notification = new NotificationCompat.Builder(
                application.getApplicationContext(), CHANEL_ID)
                .setSmallIcon(R.drawable.ic_videocam_black_24dp)
                .setCustomBigContentView(layouts.get(layout))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setChannelId(CHANEL_ID)
                .setOngoing(true)
                .build();
        return notification;
    }

    public void show(String layout){
        manager.notify(NOTIFICATION_ID, createNotification(layout));
    }

    public void cancel(){
        manager.cancel(NOTIFICATION_ID);
    }

    public void unregisterNotificationChanel(){
        manager.deleteNotificationChannel(CHANEL_ID);
    }


    public static CustomNotification getInstance(Application activity){
        if (instance == null) instance = new CustomNotification(activity);
        return instance;
    }


    public void updateNotificationDrawableTopView(String layout, int viewId,
                                                  int resDrawableID){
        RemoteViews remoteViews = layouts.get(layout);
        remoteViews.setTextViewCompoundDrawables(viewId, 0, resDrawableID, 0, 0);
    }


    public void updateNotificationTextView(String layout, int viewId, String text){
        RemoteViews remoteViews = layouts.get(layout);
        remoteViews.setTextViewText(viewId, text);
    }

    public void updateVisibilityOfTimer (int visibility) {
        RemoteViews remoteViews = layouts.get(PROGRESS_LAYOUT);
        remoteViews.setViewVisibility(R.id.timerText, visibility);
    }

    public static CustomNotification getInstance(){
        return instance;
    }

}
