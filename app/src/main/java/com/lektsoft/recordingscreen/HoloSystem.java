package com.lektsoft.recordingscreen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

public class HoloSystem extends AppCompatActivity {

    private Bitmap gallery;     //캐시 디렉토리에 저장된 비트맵을 가져오기위해 선언
    private View backBtn;       //뒤로가기 버튼
    private View saveBtn;       //저장하기 버튼
    FrameLayout fl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //홀로그램 띄울 화면을 커스텀 뷰로 생성
        HoloCustomView customView = new HoloCustomView(this);

        setContentView(R.layout.holo_screen);
        fl = (FrameLayout)findViewById(R.id.play_img);
        fl.addView(customView);

        //뒤로가기 버튼 설정
        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //저장하기 버튼 설정
        saveBtn = findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OutputStream out = null;
                String exStorage = Environment.getExternalStorageDirectory().toString();
                File file = new File(exStorage, "yoloholo.JPEG");
                try{
                    out = new FileOutputStream(file);
                    gallery.compress(Bitmap.CompressFormat.JPEG, 100, out); //Bitmap to JPEG
                    out.flush();
                    out.close();

                    Toast.makeText(HoloSystem.this, "저장되었습니다", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    Log.e("FileNotFoundException", e.getMessage());
                } catch (IOException e) {
                    Log.e("IOException", e.getMessage());
                }
            }
        });

        //캐시디렉토리로부터 선택한 이미지를 비트맵으로 가져옴
        gallery = getBitmapFromCacheDir();
    }

    public class HoloCustomView extends View {
        public HoloCustomView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            Matrix matrix = new Matrix();
            matrix.postRotate(180);

            //원본 이미지에서 180도 뒤집힌 비트맵 이미지
            Bitmap gallery_reversal = Bitmap.createBitmap(gallery, 0, 0, gallery.getWidth(), gallery.getHeight(), matrix, true);

            Bitmap resultBmp = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);  //홀로그램화 된 이미지를 저장하기 위한 비트맵
            Canvas backBmp = new Canvas(resultBmp); //비트맵으로부터 캔버스 생성
            backBmp.drawColor(Color.BLACK); //캔버스 배경색을 검정색으로
            //super.onDraw(backBmp);
            float w = gallery.getWidth();   //이미지 너비
            float h = gallery.getHeight();  //이미지 높이

            float cx = getWidth() / 2;  //화면 중심점 x좌표
            float cy = getHeight() / 2; //화면 중심점 y좌표
            float imageRatio;   //이미지 비율
            float newHeight;    //이미지 크기 계산 후 바뀐 이미지의 높이
            float newWidth;     //이미지 크기 계산 후 바뀐 이미지의 너비

            if(w>h) {   //이미지 너비가 높이보다 클 경우의 원본 비율 계산
                imageRatio = w/h;
                newHeight = getWidth()/(2+imageRatio);
                newWidth = imageRatio * newHeight;
            } else if(w<h) {    //이미지 높이가 너비보다 클 경우의 원본 비율 계산
                imageRatio = h/w;
                newHeight = getWidth()/(2+(1/imageRatio));
                newWidth = 1/imageRatio * newHeight;
            } else {    //같을 때 비율 계산
                imageRatio = w/h;
                newHeight = getWidth()/(2+imageRatio);
                newWidth = imageRatio * newHeight;
            }

            //RectF 사용해서 회전하지 않은 이미지의 위치 및 크기 지정
            RectF dst1 = new RectF(newHeight, (cy - newWidth /2) - newHeight,newWidth + newHeight, cy - (newWidth /2));

            //backBmp에 이미지 그리기
            backBmp.drawBitmap(gallery_reversal, null, dst1, null);   //뒤집은 원본 이미지를 backBmp 캔버스에 그리기
            backBmp.rotate(90, cx, cy);                          // 90도 회전
            backBmp.drawBitmap(gallery_reversal, null, dst1, null);   //90도 회전한 이미지 backBmp 캔버스에 그리기

            backBmp.rotate(90, cx, cy);                          // 180도 회전
            backBmp.drawBitmap(gallery_reversal, null, dst1, null);   //180도 회전한 이미지 backBmp 캔버스에 그리기

            backBmp.rotate(90, cx, cy);                          // 270도 회전
            backBmp.drawBitmap(gallery_reversal, null, dst1, null);   //270도 회전한 이미지 backBmp 캔버스에 그리기

            /* 아시겠지만 canvas.rotate 가 캔버스 자체를 돌리는거라 좌표가 달라져서 계속 90도만 회전하면 180도, 270도 회전한 효과가 납니다
             * 처음 이미지 위치만 지정해주고 좌표를 돌려가면서 출력한거에요 */

            canvas.drawBitmap(resultBmp,0,0, null);
            // backBmp캔버스가 생성되었던 resultBmp를 canvas에 올려서 화면에 보이게 한 번 더 설정

        }
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