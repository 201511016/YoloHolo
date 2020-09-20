package com.lektsoft.recordingscreen.Viewer;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.lektsoft.recordingscreen.Adapter.MainPagerAdapter;
import com.lektsoft.recordingscreen.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends AppCompatActivity {

    private static final String TAG = "SettingActivity";

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private int tab;

    public static final String TAB_SELECTED = "TabSelect";
    public static final int TAB_SETTINGS = 0;
    public static final int TAB_VIDEO= 1;
    public static final int TAB_SCREENSHOT= 2;

    /*
    설정에 대한 Tablayout과 Viewpager설정에 대한 파일입니다.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rs_activity_setting);
        ButterKnife.bind(this);
        
        tabLayout.addTab(createNewTab(R.drawable.ic_settings_black_24dp, R.string.SettingsNameTitle));
        tabLayout.addTab(createNewTab(R.drawable.ic_videocam_black_24dp, R.string.VideoNameTitle));
        tabLayout.addTab(createNewTab(R.drawable.ic_camera_alt_black_24dp, R.string.ScreenshotNameTitle));
        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(),
                MainPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, tabLayout.getTabCount());

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setAdapter(mainPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                setLabelOfTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Intent intent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        sendBroadcast(intent);

        Bundle bundle = getIntent().getExtras();
        tab = bundle.getInt(TAB_SELECTED);
        viewPager.setCurrentItem(tab);
        setLabelOfTab(tab);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();
        tab = bundle.getInt(TAB_SELECTED);
        viewPager.setCurrentItem(tab);
        setLabelOfTab(tab);
        Intent collapseNotification = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        sendBroadcast(collapseNotification);
    }

    private TabLayout.Tab createNewTab(int resIDIcon, int resText){
        return tabLayout.newTab()
                .setIcon(resIDIcon);
    }

    private void setLabelOfTab(int tab){
        switch (tab){
            case TAB_SETTINGS:
                setTitle("설정");
                break;
            case TAB_VIDEO:
                setTitle("화면 녹화");
                break;
            case TAB_SCREENSHOT:
                setTitle("스크린샷");
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
