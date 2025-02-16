package com.funcablaze.codeworkshop.Function.View.Window;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;

public class Frame {
	private final WindowManager windowManager;
	private final Context context;
	private final WindowManager.LayoutParams params;
	private final View view;

	public Frame(Context context, @LayoutRes int layoutId) {
		this.context = context;
		params = new WindowManager.LayoutParams();
		params.gravity = Gravity.CENTER;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		view = View.inflate(context, layoutId, null);
		windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	}

	public View getView() {
		return view;
	}

	protected Context getContext() {
		return context;
	}

	public WindowManager.LayoutParams getParams() {
		return params;
	}

	public void show() {
		windowManager.addView(view, params);
	}

	public void del() {
		windowManager.removeView(view);
	}
}
