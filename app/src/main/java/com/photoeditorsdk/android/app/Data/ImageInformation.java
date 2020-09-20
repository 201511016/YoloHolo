package com.photoeditorsdk.android.app.Data;

import android.graphics.drawable.Drawable;

public class ImageInformation extends Information {
    private Drawable imageContent;

    //Image Path에 대한 정보
    public ImageInformation(String pathName){
        super(pathName);
        imageContent = Drawable.createFromPath(pathName);
        imageContent.setBounds(0, 0, imageContent.getIntrinsicWidth() / 3,
                imageContent.getIntrinsicHeight() / 3);
    }

    public Drawable getImageContent(){
        return imageContent;
    }
}
