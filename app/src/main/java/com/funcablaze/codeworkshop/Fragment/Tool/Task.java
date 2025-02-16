package com.funcablaze.codeworkshop.Fragment.Tool;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.funcablaze.codeworkshop.Function.Manager.TaskManager;
import com.funcablaze.codeworkshop.Function.View.TaskList;
import com.funcablaze.codeworkshop.databinding.FragmentToolTaskBinding;

import java.util.ArrayList;
import java.util.List;

public class Task extends Fragment {

    private FragmentToolTaskBinding binding;
    public TaskList adapter;

    public Task() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentToolTaskBinding.inflate(inflater, container, false);
        _init();
        return binding.getRoot();
    }

    private void _init() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.taskList.setLayoutManager(linearLayoutManager);
        binding.taskList.setHasFixedSize(true);
        // 初始化数据
        List<TaskManager.Task> items = getSampleData();
        adapter = new TaskList(getContext(), items);
        binding.taskList.setAdapter(adapter);
    }

    private List<TaskManager.Task> getSampleData() {
        List<TaskManager.Task> tasks = new ArrayList<>();
//        tasks.add(new TaskManager.Task("标题", "消息", 100));
//        tasks.add(new TaskManager.Task("标题", "234", -1));
        return tasks;
    }
}