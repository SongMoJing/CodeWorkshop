package com.funcablaze.codeworkshop.Function.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.funcablaze.codeworkshop.Activity.MainActivity;
import com.funcablaze.codeworkshop.Fragment.Editor.Base;
import com.funcablaze.codeworkshop.Function.Manager.ProjectManager;
import com.funcablaze.codeworkshop.R;

import java.util.ArrayList;
import java.util.List;

public class Tab extends HorizontalScrollView {

	private LinearLayout tabContainer;
	private OnTabSelectedListener listener;
	private int selectedTabIndex = -1;
	private int text_sel, text_def, back_sel, back_def;

	public Tab(Context context) {
		super(context);
		init(context);
	}

	public Tab(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public Tab(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		text_def = getResources().getColor(R.color.tab_def_text, null);
		text_sel = getResources().getColor(R.color.tab_sel_text, null);
		back_def = getResources().getColor(R.color.tab_def_back, null);
		back_sel = getResources().getColor(R.color.tab_sel_back, null);
		tabContainer = new LinearLayout(context);
		tabContainer.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		tabContainer.setOrientation(LinearLayout.HORIZONTAL);
		addView(tabContainer);
	}

	// 添加一个 Tab
	protected void addTab(String title, Base base) {
		int position = tabContainer.getChildCount();
		LinearLayout tab = createTabView(title, base.getIcon());
		tab.setTag(base);
		tab.setOnClickListener(v -> MainActivity.getInstance().tabManager.change(position));
		tabContainer.addView(tab);
		selectTab(position);
	}

	// 创建 Tab 的视图
	private LinearLayout createTabView(String title, Bitmap icon) {
		// 文字
		TextView textView = new TextView(getContext());
		textView.setText(title);
		textView.setTextColor(text_def);
		textView.setPadding(10, 0, 0, 0);
		textView.setTypeface(ProjectManager.DefaultFont);
		// 图标
		ImageView iconView = new ImageView(getContext());
		iconView.setImageBitmap(icon);
		iconView.setLayoutParams(new LinearLayout.LayoutParams(40, 40));
		// Tab Item
		LinearLayout tabItem = new LinearLayout(getContext());
		tabItem.setOrientation(LinearLayout.HORIZONTAL);
		tabItem.setGravity(Gravity.CENTER);
		tabItem.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT
		));
		tabItem.setPadding(20, 0, 20, 0);
		tabItem.setBackgroundColor(back_def);
		tabItem.addView(iconView);
		tabItem.addView(textView);
		return tabItem;
	}

	// 选择指定位置的 Tab
	protected void selectTab(int index) {
		if (selectedTabIndex == index) {
			if (listener != null) listener.onTabSelectedSecond(index);
			return;
		}
		updateTabStyles(index);
		selectedTabIndex = index;
		if (listener != null) {
			listener.onTabSelected(index);
		}
	}

	// 重命名指定位置的 Tab
	protected void rename(int index, String newName) {
		((TextView) ((LinearLayout) tabContainer.getChildAt(index)).getChildAt(1)).setText(newName);
	}

	// 获得指定位置的 Tab 名称
	protected String getName(int index) {
		return ((TextView) ((LinearLayout) tabContainer.getChildAt(index)).getChildAt(1)).getText().toString();
	}

	// 更新 Tab 的选中和非选中样式
	private void updateTabStyles(int selectedIndex) {
		for (int i = 0; i < tabContainer.getChildCount(); i++) {
			LinearLayout tab = (LinearLayout) tabContainer.getChildAt(i);
			if (i == selectedIndex) {
				((TextView) tab.getChildAt(1)).setTextColor(text_sel);  // 设置选中颜色
				tab.setBackgroundColor(back_sel);  // 设置选中背景
			} else {
				((TextView) tab.getChildAt(1)).setTextColor(text_def);
				tab.setBackgroundColor(back_def);
			}
		}
	}

	// 删除指定位置的 Tab
	protected int removeTab(int index) {
		tabContainer.removeViewAt(index);
		// 调整选中索引
		if (selectedTabIndex >= index) {
			selectedTabIndex = Math.max(0, selectedTabIndex - 1);
		}
		selectTab(selectedTabIndex);
		return selectedTabIndex;
	}

	// 设置 Tab 选择监听器
	protected void setOnTabSelectedListener(OnTabSelectedListener listener) {
		this.listener = listener;
	}

	// 定义 Tab 选择监听器接口
	protected interface OnTabSelectedListener {
		void onTabSelected(int position);

		void onTabSelectedSecond(int position);
	}
}
