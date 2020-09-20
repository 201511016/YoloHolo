package com.photoeditorsdk.android.app;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity implements View.OnClickListener {

    private MainActivity context;
    private View createBtn;
    private View playBtn;
    private View editBtn;
    private View settingsBtn;

    //선택한 버튼이 어떤 메뉴를 선택했는지 확인하기 위한 변수
    //create와 play에서 선택한 이미지가 캐시디렉토리에 저장되어 올바른 액티비티로 넘어가게 하기 위함
    public int menuflag = 0;
    //각 메뉴 번호
    public static final int ACTION_REQUEST_CREATE = 6;
    public static final int ACTION_REQUEST_PLAY = 7;
    public static final int ACTION_REQUEST_EDIT = 8;
    public static final int ACTION_REQUEST_SETTINGS = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView(); //뷰 초기화
    }

    //뷰 초기화
    private void initView() {
        context = this;

        //메인메뉴의 각 버튼을 연결
        createBtn = (ImageButton)findViewById(R.id.create_btn);
        playBtn = (ImageButton)findViewById(R.id.play_btn);
        editBtn = (ImageButton)findViewById(R.id.edit_btn);
        settingsBtn = (ImageButton)findViewById(R.id.settings_btn);

        //클릭 리스너 설정
        createBtn.setOnClickListener(this);
        playBtn.setOnClickListener(this);
        editBtn.setOnClickListener(this);
        settingsBtn.setOnClickListener(this);
    }

    //버튼을 클릭하면 크리에이트, 플레이는 갤러리에서 사진을 선택할 수 있게 됨
    //Edit은 따로 설정 필요
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_btn:
                menuflag = ACTION_REQUEST_CREATE;   //인텐트를 넘길 고유번호 설정
                selectMenu();                  //인텐트 넘기기
                break;
            case R.id.play_btn:
                menuflag = ACTION_REQUEST_PLAY;     //인텐트를 넘길 고유번호 설정
                selectMenu();                  //인텐트 넘기기
                break;
            case R.id.edit_btn:
                menuflag = ACTION_REQUEST_EDIT;
                selectMenu();                       //인텐트 설정
                break;
            case R.id.settings_btn:
                menuflag = ACTION_REQUEST_SETTINGS;
                selectMenu();                       //인텐트 설정
                break;
        }
    }

    //클릭한 버튼이 어떤 메뉴인지 확인하고 해당 액티비티로 넘어가게 함
    private void selectMenu() {
        switch (menuflag) {
            case ACTION_REQUEST_CREATE:
                Intent intent1 = new Intent(getApplicationContext(), CallGallery.class);
                intent1.putExtra("createCode", ACTION_REQUEST_CREATE);
                startActivity(intent1);
                break;
            case ACTION_REQUEST_PLAY:
                //갤러리 오픈으로 넘어감
                Intent intent2 = new Intent(getApplicationContext(), CallGallery.class);
                intent2.putExtra("playCode", ACTION_REQUEST_PLAY);
                startActivity(intent2);
                break;
            case ACTION_REQUEST_EDIT:
                //편집화면으로 넘어감
                Intent intent3 = new Intent(getApplicationContext(), EditSystem.class);
                startActivity(intent3);
                break;
            case ACTION_REQUEST_SETTINGS:
                //셋팅화면으로 넘어감
                Intent intent4 = new Intent(getApplicationContext(), SettingsSystem.class);
                startActivity(intent4);
                break;
        }
    }
}