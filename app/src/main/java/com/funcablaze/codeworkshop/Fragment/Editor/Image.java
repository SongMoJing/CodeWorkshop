package com.funcablaze.codeworkshop.Fragment.Editor;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.funcablaze.codeworkshop.Function.Manager.FileManager;
import com.funcablaze.codeworkshop.databinding.FragmentProjectImageBinding;

import java.io.File;

public class Image extends Base {

	private FileManager.FileType fileType;
	private FragmentProjectImageBinding binding;

	public Image(FileManager.FileType fileType, File filePath) {
		super(filePath);
		this.fileType = fileType;
		setIcon("image.png");
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = FragmentProjectImageBinding.inflate(inflater, container, false);
		init();
		reloadContent();
		return binding.getRoot();
	}

	public void init() {
	}

	@Override
	public void reloadContent() {
		super.reloadContent();
		binding.image.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
	}
}