package com.lektsoft.recordingscreen.Data;

import java.io.File;

public class Information {
    protected String pathName;
    protected String name;

    //파일에 대한 정보
    public Information(String pathName){
        this.pathName = pathName;
        File file = new File(pathName);
        name = file.getName();
    }

    public String getPathName(){
        return pathName;
    }

    public String getName() {
        return name;
    }
}
