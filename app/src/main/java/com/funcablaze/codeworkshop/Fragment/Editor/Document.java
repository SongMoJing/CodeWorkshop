package com.funcablaze.codeworkshop.Fragment.Editor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.funcablaze.codeworkshop.Function.Manager.FileManager;
import com.funcablaze.codeworkshop.R;

import java.io.File;

public class Document extends Base {

    private FileManager.FileType fileType;
    private WebView webView;

    public Document(FileManager.FileType fileType, File filePath) {
        super(filePath);
        this.fileType = fileType;
        setIcon("doc.png");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Root = inflater.inflate(R.layout.fragment_project_document, container, false);
        init();
        return Root;
    }

    private void init() {

    }
}