package com.photoeditorsdk.android.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.photoeditorsdk.android.app.Data.VideoInformation;
import com.photoeditorsdk.android.app.R;

import java.io.File;
import java.util.ArrayList;

public class VideoRecyclerAdapter extends RecyclerView.Adapter {
    private ArrayList<VideoInformation> data;
    private Context context;

    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;

    private boolean isCheckboxVisible;
    private boolean isSelectAll;
    private ArrayList<Integer> checkList;
    private int item;

    /*
    Screen Shot Adapter와 같은 방식입니다.
     */

    public VideoRecyclerAdapter(ArrayList<VideoInformation> data, Context context){
        this.data = data;
        this.context = context;
        isCheckboxVisible = false;
        isSelectAll = false;
        checkList = new ArrayList<>();
        item = -1;
    }

    public void setData (ArrayList<VideoInformation> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setCheckboxVisible(boolean checkboxVisible) {
        isCheckboxVisible = checkboxVisible;
        notifyDataSetChanged();
    }

    public void setIsSelectAll(boolean selectAll) {
        isSelectAll = selectAll;
        notifyDataSetChanged();
    }

    public boolean getIsSelectAll() {
        return isSelectAll;
    }

    public ArrayList<Integer> getCheckList() {
        return checkList;
    }


    public void clearCheckList() {
        checkList.clear();
    }

    public void setItem(int item) {
        this.item = item;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.rs_video_item_layout, parent, false);
        return new VideoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VideoInformation videoInformation = data.get(position);
        VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
        videoViewHolder.imgVideoPreview.setImageBitmap(videoInformation.getImagePreview());
        videoViewHolder.textVideoName.setText(videoInformation.getName());

        videoViewHolder.textVideoDuration.setText(videoInformation.getDuration());
        videoViewHolder.textVideoSize.setText(videoInformation.getSize());
        videoViewHolder.videoPath = videoInformation.getPathName();

        videoViewHolder.btDelete.setVisibility(isCheckboxVisible ? View.GONE : View.VISIBLE);
        videoViewHolder.cbSelectVideo.setVisibility(isCheckboxVisible ? View.VISIBLE : View.GONE);

        if (item != position)
            videoViewHolder.cbSelectVideo.setChecked(isSelectAll);
        else {
            item = -1;
            videoViewHolder.cbSelectVideo.setChecked(true);
        }
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnItemClickListener {
        void onItemClick(VideoInformation videoInformation);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public void setOnItemClickListener (OnItemClickListener listener){
        this.clickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }



    private class VideoViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener, CheckBox.OnCheckedChangeListener {
        ImageView imgVideoPreview;
        TextView textVideoName;

        TextView textVideoDuration;
        TextView textVideoSize;
        ImageButton btDelete;
        CheckBox cbSelectVideo;
        String videoPath;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);

            imgVideoPreview = itemView.findViewById(R.id.image_preview);
            textVideoName = itemView.findViewById(R.id.text_video_name);

            textVideoDuration = itemView.findViewById(R.id.text_video_duration);
            textVideoSize = itemView.findViewById(R.id.text_video_size);
            btDelete = itemView.findViewById(R.id.bt_delete_video);
            cbSelectVideo = itemView.findViewById(R.id.cb_select_video);
            btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File file = new File(videoPath);
                    if (file.delete())
                        Toast.makeText(context, "선택한 비디오를 삭제했습니다", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context, "선택한 비디오를 삭제하지 못했습니다", Toast.LENGTH_SHORT).show();
                    data.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            cbSelectVideo.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onItemClick(data.get(getAdapterPosition()));
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(getAdapterPosition());
                return true;
            }
            return false;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                int pos = getAdapterPosition();
                if (pos >= 0)
                    checkList.add(pos);
            } else {
                if (checkList.size() > 0)
                    checkList.remove(Integer.valueOf(getAdapterPosition()));
            }
        }
    }
}
