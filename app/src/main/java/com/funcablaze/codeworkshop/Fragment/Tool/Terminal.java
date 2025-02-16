package com.funcablaze.codeworkshop.Fragment.Tool;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.funcablaze.codeworkshop.databinding.FragmentToolTerminalBinding;

public class Terminal extends Fragment {

    private FragmentToolTerminalBinding binding;

    public Terminal() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentToolTerminalBinding.inflate(inflater, container, false);
        _init();
        return binding.getRoot();
    }

    private void _init() {
//        binding.console;
    }
}