package com.photoeditorsdk.android.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LoadingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Handler hand = new Handler();

        hand.postDelayed(new Runnable() {

            @Override
            public void run() {
                //TODO Auto-generated method stub
                Intent i = new Intent(LoadingActivity.this, HoloSystem.class);
                startActivity(i);
            }
        }, 2000); //로딩화면에서 딜레이 후 홀로그램화 화면으로 넘어가게 해놓음.
    }
}