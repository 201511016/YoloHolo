package com.photoeditorsdk.android.app.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.photoeditorsdk.android.app.R;

public class NotificationReceiver extends BroadcastReceiver {

    //Status바에 위치한 Notification에서 일어나는 이벤트는 정의하는 곳이며
    //Service를 종료하거나 화면 녹화 일시정지, 중지의 이벤트를 관리하는 곳 입니다.

    @Override
    public void onReceive(Context context, Intent intent) {
        CustomNotification notificationCustom = CustomNotification.getInstance();
        ScreenCapture screenRecorder = ScreenCapture.getInstance();
        //종료
        if (intent.getAction().equalsIgnoreCase(notificationCustom.ACTION_EXIT)){

            Intent stopForegroundService = new Intent(context, ForegroundService.class);
            context.stopService(stopForegroundService);

            Intent stopControlBar = new Intent(context, Controller.class);
            context.stopService(stopControlBar);

            notificationCustom.cancel();
        }
        //녹화 중지
        else if (intent.getAction().equalsIgnoreCase(notificationCustom.ACTION_STOP)){
            TimeRecord timeRecord = TimeRecord.getInstance();
            timeRecord.stop();
            screenRecorder.stopRecord();
            notificationCustom.show(notificationCustom.MAIN_LAYOUT);

            Intent startControlBarIntent = new Intent(context, Controller.class);
            context.startService(startControlBarIntent);
            screenRecorder.setIsPause(false);
            Toast.makeText(context, "녹화한 화면이 저장되었습니다", Toast.LENGTH_SHORT).show();
        }//녹화 일시정지
        else if (intent.getAction().equalsIgnoreCase(notificationCustom.ACTION_PAUSE)){
            TimeRecord timeRecord = TimeRecord.getInstance();
            boolean isPause = screenRecorder.getIsPause();
            int drawableId;
            String text;
            if (isPause){
                drawableId = R.drawable.ic_pause_black_24dp;
                text = "일시정지";
                timeRecord.resume();
                screenRecorder.resumeRecord();
            }
            else {
                drawableId = R.drawable.ic_videocam_black_24dp;
                text = "재시작";
                timeRecord.pause();
                screenRecorder.pauseRecord();
            }
            screenRecorder.setIsPause(!isPause);

            notificationCustom.updateNotificationDrawableTopView(notificationCustom.PROGRESS_LAYOUT,
                    R.id.btPause, drawableId);
            notificationCustom.updateNotificationTextView(notificationCustom.PROGRESS_LAYOUT,
                    R.id.btPause, text);
            notificationCustom.show(notificationCustom.PROGRESS_LAYOUT);
        }
        else if (intent.getAction().equalsIgnoreCase(notificationCustom.ACTION_START_CONTROL_BAR)){
            Intent collapseNotification = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            context.sendBroadcast(collapseNotification);
            Intent startCtrlBarIntent = new Intent(context, Controller.class);
            context.startService(startCtrlBarIntent);
        }
    }
}
