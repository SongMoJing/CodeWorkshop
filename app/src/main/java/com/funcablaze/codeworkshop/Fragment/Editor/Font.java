package com.funcablaze.codeworkshop.Fragment.Editor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funcablaze.codeworkshop.R;

import java.io.File;

public class Font extends Base {

    public Font(File filePath) {
        super(filePath);
        setIcon("font.png");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Root = inflater.inflate(R.layout.fragment_project_font, container, false);
        return Root;
    }
}