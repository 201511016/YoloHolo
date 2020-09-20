package com.photoeditorsdk.android.app.Viewer;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.photoeditorsdk.android.app.Fragment.ScreenshotFragment;
import com.photoeditorsdk.android.app.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageViewerActivity extends AppCompatActivity {

    private static final String TAG = "ImageViewerActivity";

    @BindView(R.id.image_view)
    ImageView imageView;
    @BindView(R.id.back_button)
    ImageButton backButton;
    @BindView(R.id.btDelete_Image)
    ImageButton btDeleteImage;
    @BindView(R.id.action_view_bar)
    LinearLayout actionViewBar;

    boolean isCollapse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getIntent().getExtras();
        final String pathName = data.getString(ScreenshotFragment.IMAGE_PATH);
        setContentView(R.layout.rs_activity_image_viewer);
        ButterKnife.bind(this);
        isCollapse = true;


        /** 이미지를 클릭했을 때 나오는 버튼에 대한 이벤트**/
        btDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(pathName);
                if (file.delete())
                    Toast.makeText(ImageViewerActivity.this,
                            "이미지를 삭제 했습니다", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ImageViewerActivity.this,
                            "이미지를 삭제하지 못했습니다", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imageView.setImageBitmap(BitmapFactory.decodeFile(pathName));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition((ViewGroup)backButton.getRootView());
                int visibility=isCollapse?View.VISIBLE:View.GONE;
                backButton.setVisibility(visibility);
                btDeleteImage.setVisibility(visibility);
                isCollapse=!isCollapse;
            }
        });
    }
}
