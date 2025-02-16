package com.funcablaze.codeworkshop.Function.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.funcablaze.codeworkshop.Activity.MainActivity;
import com.funcablaze.codeworkshop.Fragment.Tool.File;
import com.funcablaze.codeworkshop.Fragment.Tool.Task;
import com.funcablaze.codeworkshop.Fragment.Tool.Terminal;
import com.funcablaze.codeworkshop.R;

import java.util.ArrayList;
import java.util.List;

public class Splitter extends LinearLayout {

	private final LayoutParams leftPosition = new LayoutParams(480, LayoutParams.MATCH_PARENT);
	private View sliceEdge;
	private FrameLayout operationalLayer;
	private int showingSelection = -1;
	private final List<Fragment> pages = new ArrayList<>();
	private FragmentManager fragmentManager;

	public Splitter(Context context) {
		super(context);
		init();
	}

	public Splitter(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public Splitter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@SuppressLint("ResourceType")
	private void init() {
		operationalLayer = new FrameLayout(getContext());
		operationalLayer.setId(1001);
		operationalLayer.setLayoutParams(leftPosition);
		addView(operationalLayer);
		sliceEdge = new View(getContext());
		sliceEdge.setId(1002);
		sliceEdge.setLayoutParams(new LayoutParams(25, LayoutParams.MATCH_PARENT));
		sliceEdge.setBackgroundColor(getResources().getColor(R.color.tab_def_back));
		addView(sliceEdge);
		sliceEdge.setOnTouchListener(new OnTouchListener() {
			private LayoutParams layoutParams;

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						sliceEdge.setBackgroundColor(getResources().getColor(R.color.tab_sel_back));
						layoutParams = (LayoutParams) operationalLayer.getLayoutParams();
						break;
					case MotionEvent.ACTION_MOVE:
						int[] location = new int[2];
						operationalLayer.getLocationOnScreen(location);
						layoutParams.width = (int) event.getRawX() - location[0];
						if (layoutParams.width < 0) layoutParams.width = 0;
						getChildAt(0).setLayoutParams(layoutParams);
						break;
					case MotionEvent.ACTION_UP:
						sliceEdge.setBackgroundColor(getResources().getColor(R.color.tab_def_back));
						if (layoutParams.width < 60) {
							layoutParams.width = 0;
							closeLeftView();
						}
						break;
				}
				return true;
			}
		});
		sliceEdge.setOnHoverListener((v, event) -> {
			switch (event.getAction()) {
				case MotionEvent.ACTION_HOVER_ENTER:
					sliceEdge.setBackgroundColor(getResources().getColor(R.color.tab_sel_back));
					break;
				case MotionEvent.ACTION_HOVER_EXIT:
					sliceEdge.setBackgroundColor(getResources().getColor(R.color.tab_def_back));
					break;
			}
			return false;
		});
		closeLeftView();
	}

	public void _init() {
		FragmentManager fragmentManager = MainActivity.getInstance().getSupportFragmentManager();
		File file = new File();
		fragmentManager.beginTransaction().add(operationalLayer.getId(), file).commit();
		fragmentManager.beginTransaction().hide(file).commit();
		pages.add(file);
		Terminal terminal = new Terminal();
		fragmentManager.beginTransaction().add(operationalLayer.getId(), terminal).commit();
		fragmentManager.beginTransaction().hide(terminal).commit();
		pages.add(terminal);
		Task task = new Task();
		fragmentManager.beginTransaction().add(operationalLayer.getId(), task).commit();
		fragmentManager.beginTransaction().hide(task).commit();
		pages.add(task);
	}

	private boolean selectView(int index) {
		if (pages == null) return false;
		if (pages.size() <= index) return false;
		if (fragmentManager == null)
			fragmentManager = MainActivity.getInstance().getSupportFragmentManager();
		fragmentManager.beginTransaction().show(pages.get(index)).commit();
		if (showingSelection != -1 & showingSelection != index) {
			fragmentManager.beginTransaction().hide(pages.get(showingSelection)).commit();
		}
		showingSelection = index;
		return true;
	}

	public void closeLeftView() {
		if (showingSelection != -1)
			fragmentManager.beginTransaction().hide(pages.get(showingSelection)).commit();
		showingSelection = -1;
		operationalLayer.setVisibility(View.GONE);
		sliceEdge.setVisibility(View.GONE);
	}

	public void showLeftView(int index) {
		if (index == showingSelection) {
			closeLeftView();
			return;
		}
		if (selectView(index)) {
			if (leftPosition.width == 0) {
				leftPosition.width = 480;
			}
			operationalLayer.setVisibility(View.VISIBLE);
			sliceEdge.setVisibility(View.VISIBLE);
		}
	}
}
