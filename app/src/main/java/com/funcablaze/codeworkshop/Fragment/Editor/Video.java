package com.funcablaze.codeworkshop.Fragment.Editor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funcablaze.codeworkshop.R;

import java.io.File;

public class Video extends Base {

    public Video(File filePath) {
        super(filePath);
        setIcon("video.png");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Root = inflater.inflate(R.layout.fragment_project_video, container, false);
        return Root;
    }
}