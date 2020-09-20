package com.photoeditorsdk.android.app.Utils;

import com.photoeditorsdk.android.app.R;

import java.util.Timer;
import java.util.TimerTask;

public class TimeRecord {

    private static final String TAG = "TimeRecord";

    private static TimeRecord instance;
    private int hour;
    private int minute;
    private int second;

    private Timer timer;

    private CustomNotification customNotification;

    /**
     * 화면 녹화 시 돌아가는 타이머를 관리하는 파일입니다.
     * Text타입 변경 및 커스텀은 layout파일에서 in_progress_layout.xml파일을 수정해주세요.
     */

    private TimeRecord(){
        hour = 0;
        minute = 0;
        second = 0;
        this.customNotification = CustomNotification.getInstance();
    }

    public String getTime(){
        String strHour = hour < 10 ? "0" + hour : String.valueOf(hour);
        String strMinute = minute < 10 ? "0" + minute : String.valueOf(minute);
        String strSecond = second < 10 ? "0" + second : String.valueOf(second);
        return strHour + ":" + strMinute + ":" + strSecond;
    }

    public void start(){
        timer = new Timer();
        timer.schedule(new MyTimerTask(), 0, 1000);
    }

    public void pause(){
        timer.cancel();
    }

    public void resume(){
        timer = new Timer();
        timer.schedule(new MyTimerTask(), 0, 1000);
    }
    
    public void stop(){
        pause();
        hour = 0;
        minute = 0;
        second = 0;
    }

    public static TimeRecord getInstance(){
        if (instance == null) instance = new TimeRecord();
        return instance;
    }

    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            second++;
            if (second > 59){
                second = 0;
                minute++;
                if (minute > 59){
                    minute = 0;
                    hour++;
                    if (hour > 23){
                        hour = 0;
                    }
                }
            }
            customNotification.updateNotificationTextView(customNotification.PROGRESS_LAYOUT,
                    R.id.timerText, getTime());
            customNotification.show(customNotification.PROGRESS_LAYOUT);
        }
    }
}
