package com.photoeditorsdk.android.app.Utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.photoeditorsdk.android.app.R;
import com.photoeditorsdk.android.app.Viewer.RecordActivity;
import com.photoeditorsdk.android.app.Viewer.ScreenShotActivity;
import com.photoeditorsdk.android.app.Viewer.SettingActivity;

public class Controller extends Service {

    private static final String TAG = "Controller";

    private View controlBarView;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;

    private float deltaX;
    private float deltaY;
    private int lastAction;

    /**
     * 해당 파일은 Main화면이 뜨고 난 후 화면 중간에 띄워지는 컨트롤러의 서비스를 정의해놓았습니다.
     */


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        controlBarView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.rs_controller_layout, null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );
        } else {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );
        }

        controlBarView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return dragView(v, event);
            }
        });

        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 0;

        setListener();
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(controlBarView, params);
    }

    private void setListener() {
        final ImageButton btSettings = controlBarView.findViewById(R.id.controller_bt_settings);
        final ImageButton btRecord = controlBarView.findViewById(R.id.controller_bt_record);
        final ImageButton btScreenshot = controlBarView.findViewById(R.id.controller_bt_screenshot);
        final ImageButton btClose = controlBarView.findViewById(R.id.controller_bt_exit);

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(getApplicationContext(), SettingActivity.class);
                settingsIntent.putExtra(SettingActivity.TAB_SELECTED, SettingActivity.TAB_SETTINGS);
                settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(settingsIntent);
            }
        });

        btRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
                Intent newActivityIntent = new Intent(getApplicationContext(), RecordActivity.class);
                newActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newActivityIntent);
            }
        });

        btScreenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
                Intent takeScreenshotIntent = new Intent(getApplicationContext(),
                        ScreenShotActivity.class);
                takeScreenshotIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(takeScreenshotIntent);
            }
        });

        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
            }
        });
    }

    private boolean dragView(View view, MotionEvent motionEvent) {
        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                deltaX = Math.abs(motionEvent.getRawX() - params.x);
                deltaY = Math.abs(motionEvent.getRawY() - params.y);
                lastAction = motionEvent.getAction();
                return true;
            case MotionEvent.ACTION_UP:
                if (lastAction == MotionEvent.ACTION_DOWN){
                    if (view instanceof ImageButton){
                        ImageButton button = (ImageButton) view;
                        button.performClick();
                    }
                }
                windowManager.updateViewLayout(controlBarView, params);
                lastAction = motionEvent.getAction();
                return true;
            case MotionEvent.ACTION_MOVE:
                params.x = (int) (motionEvent.getRawX() - deltaX);
                params.y = (int) (motionEvent.getRawY() - deltaY);

                windowManager.updateViewLayout(controlBarView, params);
                lastAction = motionEvent.getAction();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (controlBarView != null)
            windowManager.removeView(controlBarView);
    }
}
