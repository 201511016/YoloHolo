package com.photoeditorsdk.android.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;


public class PlaySystem extends Activity {

    private Bitmap gallery;     //캐시디렉토리에 저장된 이미지를 가져올 비트맵
    private ImageView playImgView;  //화면에 띄울 이미지 뷰
    private View backBtn;
    private View checkBtn;

    //화면 터치시 배너 Visibility 설정용
    private RelativeLayout rl;
    private View banner;
    boolean gone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_screen);

        //뒤로가기 버튼 설정
        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //체크 버튼 설정
        checkBtn = findViewById(R.id.check_btn);
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent z = new Intent(PlaySystem.this, HoloSystem.class);
                startActivity(z);
            }
        });


        gallery = getBitmapFromCacheDir();      //캐시 디렉토리에 저장된 이미지를 비트맵으로 가져옴
        playImgView = (ImageView)findViewById(R.id.play_img);   //이미지뷰 연결

        //이미지뷰에 불러온 비트맵을 이미지로 변환
        playImgView.setImageBitmap(gallery);

        //배너 Visibility 설정
        banner = findViewById(R.id.banner);     //배너 연결
        rl = (RelativeLayout)findViewById(R.id.relativelayout1);
        rl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {   //화면을 터치했을 때
                    if (!gone) {
                        banner.setVisibility(View.GONE);        //배너가 보이는 상태면 배너를 안 보이게 함
                        gone = true;
                    } else {
                        banner.setVisibility(View.VISIBLE);     //배너가 안 보이면 보이게 함
                        gone = false;
                    }
                }
                return true;
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Bitmap getBitmapFromCacheDir() {    //캐시에 저장해두는거
        //blackJin 이 들어간 파일들을 저장할 배열 입니다.
        ArrayList<String> gallery = new ArrayList<>();

        File file = new File(getCacheDir().toString());

        File[] files = file.listFiles();

        for (File tempFile : files) {

            Log.d("MyTag", tempFile.getName());

            //blackJin 이 들어가 있는 파일명을 찾습니다.
            if (tempFile.getName().contains("gallery")) {
                gallery.add(tempFile.getName());
            }
        }
        Log.e("MyTag", "gallery size : " + gallery.size());
        if (gallery.size() > 0) {
            int randomPosition = new Random().nextInt(gallery.size());

            //blackJins 배열에 있는 파일 경로 중 하나를 랜덤으로 불러옵니다.
            String path = getCacheDir() + "/" + gallery.get(randomPosition);

            //파일경로로부터 비트맵을 생성합니다.
            //Bitmap oldImage =
            return BitmapFactory.decodeFile(path);
        } else {
            return null;
        }
    }
}