package com.lektsoft.recordingscreen.Viewer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.lektsoft.recordingscreen.R;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoViewerActivity extends AppCompatActivity {

    private static final String TAG = "VideoViewerActivity";

    public static final String VIDEO_PATH = "Video_File_Path";
    @BindView(R.id.video_viewer)
    VideoView videoViewer;

    /*
    녹화한 영상을 보는 Player에 대한 화면입니다.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rs_activity_video_viewer);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String filePath = bundle.getString(VIDEO_PATH);

        MediaController mediaController = new MediaController(this);
        mediaController.setMediaPlayer(videoViewer);

        videoViewer.setMediaController(mediaController);
        videoViewer.setVideoURI(Uri.parse(filePath));
        videoViewer.start();
    }
}
