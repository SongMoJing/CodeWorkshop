package com.funcablaze.codeworkshop.Fragment.Editor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funcablaze.codeworkshop.Function.Manager.FileManager;
import com.funcablaze.codeworkshop.R;

import java.io.File;

public class Zip extends Base {

    public Zip(FileManager.FileType fileType, File filePath) {
        super(filePath);
        setIcon("zip.png");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Root = inflater.inflate(R.layout.fragment_project_zip, container, false);
        return Root;
    }
}