package com.lektsoft.recordingscreen.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.CamcorderProfile;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class ScreenCapture {

    private static final String TAG = "ScreenCapture";

    private MediaRecorder mediaRecorder;
    private MediaProjection mediaProjection;
    private MediaProjectionManager manager;

    private VirtualDisplay display;
    private final String DISPLAY_NAME = "record display";

    private DisplayMetrics metrics;

    private boolean isPause;

    private static ScreenCapture instance;

    private String imagePath;

    private Context context;

    //스크린샷과 화면에 대한 설정 및 각종 동작 이벤트에 대한 정의입니다.

    private ScreenCapture(AppCompatActivity activity){
        isPause = false;
        manager = (MediaProjectionManager) activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
    }

    //화면 녹화 시작
    public void prepareRecorder(AppCompatActivity activity, int quality, int fps, int bitrate,
                                int orientation, String absolutePath) {
        CamcorderProfile profile = CamcorderProfile.get(quality);
        profile.videoFrameWidth = metrics.widthPixels;
        profile.videoFrameHeight = metrics.heightPixels;
        profile.videoFrameRate = fps;
        profile.duration = 86400000;
        profile.videoBitRate = bitrate == 0 ? autoBitRate(quality, fps) : bitrate;

        String pathName = generateFileName(absolutePath, ".mp4");

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mediaRecorder.setProfile(profile);

        if (orientation == 100){
            mediaRecorder.setOrientationHint(autoOrientation(activity.getWindowManager()));
        }
        else mediaRecorder.setOrientationHint(orientation);

        mediaRecorder.setOutputFile(pathName);

        try{
            mediaRecorder.prepare();
        }
        catch (IOException e) {
            Log.d(TAG, "prepareRecorder Error: "+e.toString());
        }
    }

    //스크린샷 시 생성자
    public ScreenCapture(Context context, String imagePath){
        this.context = context;
        manager = (MediaProjectionManager) context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        metrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metrics);
        this.imagePath = imagePath;
    }

    private String generateFileName(String absolutePath, String fileType) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_hhmmss", Locale.US);
        String fileName = dateFormat.format(new Date());
        return absolutePath + "/" + fileName + fileType;
    }

    public Intent createScreenCaptureIntent(){
        return manager.createScreenCaptureIntent();
    }

    public void startRecord(int resultCode, Intent data){
        mediaProjection = manager.getMediaProjection(resultCode, data);
        display = mediaProjection.createVirtualDisplay(DISPLAY_NAME, metrics.widthPixels,
                metrics.heightPixels, metrics.densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mediaRecorder.getSurface(),
                null, null);

        mediaRecorder.start();
    }

    public void pauseRecord(){
        mediaRecorder.pause();
    }

    public void resumeRecord(){
        mediaRecorder.resume();
    }

    public void stopRecord(){
        mediaRecorder.stop();
        stopProjection();
    }

    public boolean getIsPause() {
        return isPause;
    }

    public void setIsPause(boolean pause) {
        isPause = pause;
    }

    public static ScreenCapture getInstance(AppCompatActivity activity){
        instance = new ScreenCapture(activity);
        return instance;
    }

    public static ScreenCapture getInstance(){
        return instance;
    }

    private int autoOrientation (WindowManager windowManager){
        int angle = 0;
        switch (windowManager.getDefaultDisplay().getRotation()){
            case Surface.ROTATION_90:
                angle = 90;
                break;
            case Surface.ROTATION_180:
                angle = 180;
                break;
            case Surface.ROTATION_270:
                angle = 270;
                break;
        }
        return angle;
    }

    private int autoBitRate(int quality, int fps){
        int bitRate = 0;
        switch (quality){
            case CamcorderProfile.QUALITY_LOW:
                bitRate = 1000000;
                break;
            case CamcorderProfile.QUALITY_CIF:
                bitRate = 1500000;
                break;
            case CamcorderProfile.QUALITY_480P:
                bitRate = 2500000;
                break;
            case CamcorderProfile.QUALITY_720P:
                bitRate = fps >= 60 ? 5000000 : 4000000;
                break;
            case CamcorderProfile.QUALITY_1080P:
            case CamcorderProfile.QUALITY_HIGH:
                bitRate = fps >= 60 ? 8000000 : 5000000;
                break;
            case CamcorderProfile.QUALITY_2160P:
                bitRate = 45000000;
                break;
        }
        return bitRate;
    }

    public void takeScreenshot(int resultCode, Intent data) {
        final int width = metrics.widthPixels;
        final int height = metrics.heightPixels;
        mediaProjection = manager.getMediaProjection(resultCode, data);
        ImageReader imageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888,
                2);

        display = mediaProjection.createVirtualDisplay(DISPLAY_NAME, width, height, metrics.densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY, imageReader.getSurface(),
                null, null);

        imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                Image image = reader.acquireLatestImage();

                if (image != null) {
                    final Image.Plane[] planes = image.getPlanes();
                    Buffer buffer = planes[0].getBuffer();

                    int imgWidth = image.getWidth();
                    int imgHeight = image.getHeight();

                    int pixelStride = planes[0].getPixelStride();
                    int rowStride = planes[0].getRowStride();
                    int rowPadding = rowStride - pixelStride * imgWidth;

                    Bitmap bitmap = Bitmap.createBitmap(imgWidth + rowPadding / pixelStride
                            , imgHeight, Bitmap.Config.ARGB_8888);
                    bitmap.copyPixelsFromBuffer(buffer);

                    FileOutputStream fileOutputStream =  null;
                    try {
                        fileOutputStream = new FileOutputStream(generateFileName(
                                imagePath, ".jpg"));

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    }
                    catch (IOException fe){
                        Log.d(TAG, "onImageAvailable Error: "+fe.toString());
                    }
                    finally {
                        try {
                            if (fileOutputStream != null) {
                                fileOutputStream.close();
                            }
                            stopProjection();
                            Toast.makeText(context, "스크린샷이 저장되었습니다.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        catch (IOException ioe){
                            Log.d(TAG, "onImageAvailable Error: "+ioe.toString());
                        }
                    }
                }
            }
        }, new Handler());
    }

    public void stopProjection(){
        mediaProjection.stop();
        display.release();
    }

}
