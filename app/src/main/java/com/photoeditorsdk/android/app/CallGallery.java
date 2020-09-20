package com.photoeditorsdk.android.app;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.photoeditorsdk.android.app.LoadingActivity;
import com.photoeditorsdk.android.app.PlaySystem;
import com.photoeditorsdk.android.app.R;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CallGallery extends Activity {
    ImageView iv;       //화면에 보이는 이미지
    private View backBtn;   //뒤로가기 버튼
    private View checkBtn;  //체크 버튼

    //메인에서 클릭한 버튼이 어떤 버튼인지 확인하기 위한 변수
    private int menuNum;
    private int createCode = 6;
    private int playCode = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_gallery);
        checkSelfPermission();

        //갤러리에서 이미지 선택
        Intent intent = new Intent();
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE); //구글 갤러리 접근
        intent.setType("image/*" + "video/*");  //기기 기본 갤러리 접근 //갤러리 안에 동영상까지 뜨게 할 수는 있는데 동영상 가져오는걸 못해서..fail
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,101);

        //화면 설정
        iv = findViewById(R.id.show_img);
        backBtn = findViewById(R.id.back_btn);
        checkBtn = findViewById(R.id.check_btn);

        //메인에서 클릭한 버튼 값 받아오기
        Intent checkIntent = getIntent();   //데이터 받아옴
        if(checkIntent.getExtras().getInt("createCode") == createCode) {
            menuNum = createCode;   //create 버튼을 클릭했다면 menuNum을 create 버튼 값으로 설정
        }
        else if(checkIntent.getExtras().getInt("playCode") == playCode) {
            menuNum = playCode;     //play 버튼을 클릭했다면 menuNum을 play 버튼 값으로 설정
        }

        //뒤로가기 버튼 설정
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //체크 버튼을 누르면 다음 액티비티로 진행됨
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menuNum == createCode){   //메뉴 넘버가 크리에이트면 로딩액티비티로 넘어감
                    Intent intentC = new Intent(getApplicationContext(), LoadingActivity.class);
                    startActivity(intentC);
                }
                else if(menuNum == playCode){   //메뉴 넘버가 플레이면 플레이액티비티로 넘어감
                    Intent intentP = new Intent(getApplicationContext(), PlaySystem.class);
                    startActivity(intentP);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 101 && resultCode == RESULT_OK){
            try{
                InputStream is = getContentResolver().openInputStream(data.getData());
                Bitmap bm = BitmapFactory.decodeStream(is);

                iv.setImageBitmap(bm);  // 선택한 이미지 화면에 출력
                saveBitmapToJpeg(bm, "gallery");    // 선택한 갤러리 이미지를 캐시에 저장
                is.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        } else if(requestCode == 101 && resultCode == RESULT_CANCELED){
            Toast.makeText(this,"취소", Toast.LENGTH_SHORT).show();
        }
    }

    //권한 확인하기 위한 함수
    public void checkSelfPermission() {
        String temp = "";
        //파일 읽기 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }
        //파일 쓰기 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }
        if (TextUtils.isEmpty(temp) == false) {
            // 권한 요청
            ActivityCompat.requestPermissions(this, temp.trim().split(" "),1);
        }else {
            // 모두 허용 상태
            Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show();
        }
    }

    //권한에 대한 응답이 있을때 작동하는 함수
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //권한을 허용 했을 경우
        if (requestCode == 1) {
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    // 동의
                    Log.d("MainActivity", "권한 허용 : " + permissions[i]);
                }
            }
        }
    }

    //비트맵을 jpeg로 저장
    private void saveBitmapToJpeg(Bitmap bitmap, String name) {
        //내부저장소 캐시 경로를 받아옵니다.
        File storage = getCacheDir();

        //저장할 파일 이름
        String fileName = name + ".jpg";

        //storage 에 파일 인스턴스를 생성합니다.
        File tempFile = new File(storage, fileName);

        try {
            // 자동으로 빈 파일을 생성합니다.
            tempFile.createNewFile();

            // 파일을 쓸 수 있는 스트림을 준비합니다.
            FileOutputStream out = new FileOutputStream(tempFile);

            // compress 함수를 사용해 스트림에 비트맵을 저장합니다.
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            // 스트림 사용후 닫아줍니다.
            out.close();
        } catch (FileNotFoundException e) {
            Log.e("MyTag","FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            Log.e("MyTag","IOException : " + e.getMessage());
        }
    }
}