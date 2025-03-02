package com.funcablaze.codeworkshop.Fragment.Editor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.funcablaze.codeworkshop.R;

import java.io.File;

public class Web extends Base {

    private String title;

    public Web(File filePath) {
        super(filePath);
        setIcon("link.png");
//        title = (String) tabItem.getText();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Root = inflater.inflate(R.layout.fragment_project_web, container, false);
        init();
        return Root;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        WebView webView = Root.findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
//        settings.setSupportZoom(true);
//        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        webView.loadUrl(file.getPath());
    }

    public String getTitle() {
        return title;
    }
}