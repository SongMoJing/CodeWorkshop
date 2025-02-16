package com.funcablaze.codeworkshop.Function.View.CodeEditor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import androidx.annotation.NonNull;

import com.funcablaze.codeworkshop.R;

import io.github.rosemoe.sora.widget.component.EditorAutoCompletion;
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme;

public class CompletionLayout implements io.github.rosemoe.sora.widget.component.CompletionLayout {

	@Override
	public void onApplyColorScheme(@NonNull EditorColorScheme colorScheme) {

	}

	@Override
	public void setEditorCompletion(@NonNull EditorAutoCompletion completion) {

	}

	@NonNull
	@Override
	public View inflate(@NonNull Context context) {
		return View.inflate(context, R.layout.xml_completion, null);
	}

	@NonNull
	@Override
	public AdapterView getCompletionList() {
		return null;
	}

	@Override
	public void setLoading(boolean loading) {

	}

	@Override
	public void ensureListPositionVisible(int position, int incrementPixels) {

	}

	// TODO: 适配

	public static class Adapter extends AdapterView {

		public Adapter(Context context) {
			super(context);
		}

		public Adapter(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		public Adapter(Context context, AttributeSet attrs, int defStyleAttr) {
			super(context, attrs, defStyleAttr);
		}

		@Override
		public android.widget.Adapter getAdapter() {
			return null;
		}

		@Override
		public void setAdapter(android.widget.Adapter adapter) {

		}

		@Override
		public View getSelectedView() {
			return null;
		}

		@Override
		public void setSelection(int position) {

		}
	}
}
