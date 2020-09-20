package com.lektsoft.recordingscreen.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lektsoft.recordingscreen.Adapter.VideoRecyclerAdapter;
import com.lektsoft.recordingscreen.Data.VideoInformation;
import com.lektsoft.recordingscreen.R;
import com.lektsoft.recordingscreen.Utils.AppSettings;
import com.lektsoft.recordingscreen.Viewer.VideoViewerActivity;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoFragment extends Fragment {

    private static final String TAG = "VideoFragment";

    @BindView(R.id.recycler_view_video)
    RecyclerView recyclerViewVideo;
    @BindView(R.id.refresh_video)
    SwipeRefreshLayout refreshVideo;

    private ArrayList<VideoInformation> videoInformationArrayList;
    private VideoRecyclerAdapter videoRecyclerAdapter;
    private AppSettings settings;

    public VideoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rs_fragment_video, container, false);
        ButterKnife.bind(this, view);

        settings = new AppSettings(getContext());
        String filePath = settings.getVideoStoragePath();
        videoInformationArrayList = new ArrayList<>();
        File file = new File(filePath);
        File[] listVideo = file.listFiles();

        if (listVideo.length > 1) {
            for (File video : listVideo)
                if (!video.isDirectory())
                    videoInformationArrayList.add(new VideoInformation(video.getAbsolutePath()));


            videoRecyclerAdapter = new VideoRecyclerAdapter(videoInformationArrayList, getContext());
            videoRecyclerAdapter.setOnItemClickListener(new VideoRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(VideoInformation videoInformation) {
                    Intent intent = new Intent(getContext(), VideoViewerActivity.class);
                    intent.putExtra(VideoViewerActivity.VIDEO_PATH, videoInformation.getPathName());
                    startActivity(intent);
                }
            });

            videoRecyclerAdapter.setOnItemLongClickListener(new VideoRecyclerAdapter.OnItemLongClickListener() {
                @Override
                public void onItemLongClick(int position) {
                    videoRecyclerAdapter.setItem(position);
                    videoRecyclerAdapter.setCheckboxVisible(true);
                    setHasOptionsMenu(true);
                }
            });
            recyclerViewVideo.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerViewVideo.setAdapter(videoRecyclerAdapter);
        }

        refreshVideo.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetVedioData();
                refreshVideo.setRefreshing(false);
            }
        });
        return view;
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_video_library, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_video_item_select_all:
                videoRecyclerAdapter.setIsSelectAll(!videoRecyclerAdapter.getIsSelectAll());
                break;
            case R.id.menu_video_item_delete:
                ArrayList<Integer> checkList = videoRecyclerAdapter.getCheckList();
                if (checkList.size() > 0) {
                    for (int pos : checkList) {
                        VideoInformation videoInformation = videoInformationArrayList.get(pos);
                        File file = new File(videoInformation.getPathName());
                        if (file.delete())
                            Toast.makeText(getContext(), "선택한 비디오를 삭제했습니다",
                                    Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getContext(), "선택한 비디오를 삭제하지 못했습니다",
                                    Toast.LENGTH_SHORT).show();
                    }
                    GetVedioData();
                    videoRecyclerAdapter.clearCheckList();
                }
                else
                    Toast.makeText(getContext(), "비디오가 선택되지 않았습니다.",
                            Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_video_item_close:
                setHasOptionsMenu(false);
                videoRecyclerAdapter.setIsSelectAll(false);
                videoRecyclerAdapter.setCheckboxVisible(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void GetVedioData() {
        videoInformationArrayList = new ArrayList<>();
        String pathName = settings.getVideoStoragePath();
        File file = new File(pathName);
        File[] videos = file.listFiles();
        if (videos.length > 0) {
            for (File video : videos)
                if (!video.isDirectory())
                    videoInformationArrayList.add(new VideoInformation(video.getAbsolutePath()));
            videoRecyclerAdapter.setData(videoInformationArrayList);
        }
    }
}
