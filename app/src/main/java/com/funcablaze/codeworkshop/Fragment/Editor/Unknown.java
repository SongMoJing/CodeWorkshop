package com.funcablaze.codeworkshop.Fragment.Editor;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funcablaze.codeworkshop.R;

import java.io.File;

public class Unknown extends Base {

    public Unknown(File file) {
        super(file);
        setIcon("unknown.png");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_project_unknown, container, false);
    }
}