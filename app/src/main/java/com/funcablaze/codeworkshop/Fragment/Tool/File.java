package com.funcablaze.codeworkshop.Fragment.Tool;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funcablaze.codeworkshop.Activity.MainActivity;
import com.funcablaze.codeworkshop.Function.Manager.ProjectManager;
import com.funcablaze.codeworkshop.Function.View.TreeList;
import com.funcablaze.codeworkshop.Function.View.TreeList.FileItem;
import com.funcablaze.codeworkshop.databinding.FragmentToolFileBinding;

import java.util.TreeSet;

public class File extends Fragment {

	private FragmentToolFileBinding binding;
	public TreeList adapter;

	public File() {}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = FragmentToolFileBinding.inflate(inflater, container, false);
		_init();
		return binding.getRoot();
	}

	private void _init() {
		binding.fileList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//		binding.fileList.setHasFixedSize(true);
		// 初始化数据
		TreeSet<FileItem> rootItems = getSampleData();
		adapter = new TreeList(getContext(), rootItems);
		binding.fileList.setAdapter(adapter);
	}

	private TreeSet<FileItem> getSampleData() {
		TreeSet<FileItem> rootFiles = new TreeSet<>();
		java.io.File rootFile = new java.io.File(ProjectManager.ProjectPath);
		if (rootFile.exists()) {
			FileItem root = new FileItem(rootFile.getName(), 0, rootFile.isDirectory());
			rootFiles.add(root);
		}
		return rootFiles;
	}
}