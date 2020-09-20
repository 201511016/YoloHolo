package com.photoeditorsdk.android.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.photoeditorsdk.android.app.Data.ImageInformation;
import com.photoeditorsdk.android.app.R;

import java.util.ArrayList;

public class ScreenshotAdapter extends RecyclerView.Adapter {

    private static final String TAG = "ScreenshotRecyclerAdapt";

    private ArrayList<ImageInformation> data;
    private Context context;

    private OnItemClickListener clickListener;
    private ScreenshotAdapter.OnItemLongClickListener longClickListener;

    private boolean isVisible;
    private boolean isSelectAll;
    private ArrayList<Integer> checkList;
    private int item;

    //촬영한 스크린샷 아이템을 클릭했을 시 또는 길게 클릭했을 시에 대한 설정도 하단에 있습니다.

    public ScreenshotAdapter(ArrayList<ImageInformation> data, Context context) {
        this.data = data;
        this.context = context;
        isVisible = false;
        isSelectAll = false;
        checkList = new ArrayList<>();
        item = -1;
    }

    public void setItem(int item){
        this.item = item;
    }

    public void setData(ArrayList<ImageInformation> data){
        this.data = data;
        notifyDataSetChanged();
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
        notifyDataSetChanged();
    }

    public void setIsSelectAll(boolean isSelected){
        this.isSelectAll = isSelected;
        notifyDataSetChanged();
    }

    public boolean getIsSelectAll() {
        return isSelectAll;
    }

    public ArrayList<Integer> getCheckList() {
        return checkList;
    }

    public void clearCheckList(){
        checkList.clear();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.rs_image_item_layout,
                parent, false);

        return new ImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ImageInformation imageInformation = data.get(position);
        ImageViewHolder imageHolder = (ImageViewHolder) holder;
        imageHolder.textViewImage.setText(imageInformation.getName());
        imageHolder.textViewImage.setCompoundDrawables(null,
                imageInformation.getImageContent(), null, null);

        imageHolder.checkBox_select.setVisibility(isVisible ? View.VISIBLE : View.GONE);

        if (item != position)
            imageHolder.checkBox_select.setChecked(isSelectAll);
        else {
            item = -1;
            imageHolder.checkBox_select.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnItemClickListener {
        void onItemClick(ImageInformation imageInformation);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public void setOnItemClickListener (ScreenshotAdapter.OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public void setOnItemLongClickListener (ScreenshotAdapter.OnItemLongClickListener listener){
        this.longClickListener = listener;
    }

    private class ImageViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener, CheckBox.OnCheckedChangeListener{

        CheckBox checkBox_select;
        TextView textViewImage;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewImage = itemView.findViewById(R.id.textView_ImageName);
            checkBox_select = itemView.findViewById(R.id.cb_select_image);
            checkBox_select.setOnCheckedChangeListener(this);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null){
                clickListener.onItemClick(data.get(getAdapterPosition()));
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (longClickListener != null){
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
            }
            else {
                if (checkList.size() > 0)
                    checkList.remove(Integer.valueOf(getAdapterPosition()));
            }
        }
    }
}
