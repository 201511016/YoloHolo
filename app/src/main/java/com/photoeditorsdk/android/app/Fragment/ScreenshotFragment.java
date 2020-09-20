package com.photoeditorsdk.android.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.photoeditorsdk.android.app.Adapter.ScreenshotAdapter;
import com.photoeditorsdk.android.app.Data.ImageInformation;
import com.photoeditorsdk.android.app.R;
import com.photoeditorsdk.android.app.Utils.AppSettings;
import com.photoeditorsdk.android.app.Viewer.ImageViewerActivity;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScreenshotFragment extends Fragment {

    private static final String TAG = "ScreenshotFragment";

    public static final String IMAGE_PATH = "image path";

    @BindView(R.id.image_recycler_view)
    RecyclerView imageRecyclerView;
    @BindView(R.id.refresh_image)
    SwipeRefreshLayout refreshImage;

    private ArrayList<ImageInformation> imageInformationArrayList;
    private ScreenshotAdapter screenshotAdapter;
    private AppSettings settingsApp;

    public ScreenshotFragment() {
    }

    //Viewpager에 있는 화면 중 하나로 촬영된 스크린샷을 보는 화면입니다.

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View screenshotView = inflater.inflate(R.layout.rs_fragment_screenshot, container, false);
        ButterKnife.bind(this, screenshotView);

        settingsApp = new AppSettings(getContext());
        imageInformationArrayList = new ArrayList<>();
        String pathName = settingsApp.getImageStoragePath();

        //저장되어 있는 사진 가져오기

        File file = new File(pathName);
        File[] images = file.listFiles();
        if (images.length > 0) {
            for (File image : images)
                imageInformationArrayList.add(new ImageInformation(image.getAbsolutePath()));

            screenshotAdapter = new ScreenshotAdapter(imageInformationArrayList, getContext());
            screenshotAdapter.setOnItemClickListener(new ScreenshotAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(ImageInformation imageInformation) {
                    Intent intent = new Intent(getContext(), ImageViewerActivity.class);
                    intent.putExtra(IMAGE_PATH, imageInformation.getPathName());
                    startActivity(intent);
                }
            });

            screenshotAdapter.setOnItemLongClickListener(new ScreenshotAdapter.OnItemLongClickListener() {
                @Override
                public void onItemLongClick(int position) {
                    screenshotAdapter.setItem(position);
                    screenshotAdapter.setIsVisible(true);
                    setHasOptionsMenu(true);
                }
            });
            imageRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
            imageRecyclerView.setAdapter(screenshotAdapter);

            refreshImage.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getImageData();
                    refreshImage.setRefreshing(false);
                }
            });
        }
        return screenshotView;
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_screenshot_library, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //LongClick시 ActionBar에 나타나는 버튼에 대한 정리
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_screenshot_item_select_all:
                screenshotAdapter.setIsSelectAll(!screenshotAdapter.getIsSelectAll());
                break;
            case R.id.menu_screenshot_item_delete:
                ArrayList<Integer> checkList = screenshotAdapter.getCheckList();
                if (checkList.size() > 0) {
                    for (int pos : checkList) {
                        ImageInformation imageInformation = imageInformationArrayList.get(pos);
                        File file = new File(imageInformation.getPathName());
                        if (file.delete())
                            Toast.makeText(getContext(), "이미지를 삭제 했습니다",
                                    Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getContext(), "이미지를 삭제하지 못했습니다",
                                    Toast.LENGTH_SHORT).show();
                    }
                    getImageData();
                    screenshotAdapter.clearCheckList();
                }
                else
                    Toast.makeText(getContext(), "이미지가 선택되지 않았습니다",
                            Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_screenshot_item_close:
                setHasOptionsMenu(false);
                screenshotAdapter.setIsSelectAll(false);
                screenshotAdapter.setIsVisible(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        getImageData();
        super.onResume();
    }

    private void getImageData(){
        imageInformationArrayList = new ArrayList<>();
        String pathName = settingsApp.getImageStoragePath();
        File file = new File(pathName);
        File[] images = file.listFiles();
        if (images.length > 0) {
            for (File image : images)
                imageInformationArrayList.add(new ImageInformation(image.getAbsolutePath()));
            screenshotAdapter.setData(imageInformationArrayList);
        }
    }
}