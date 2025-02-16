package com.funcablaze.codeworkshop.Fragment.Editor;

import static android.os.FileObserver.DELETE;
import static android.os.FileObserver.MODIFY;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.funcablaze.codeworkshop.Activity.MainActivity;
import com.funcablaze.codeworkshop.Function.Manager.FileManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public abstract class Base extends Fragment {
	protected File file;
	protected View Root;
	protected Bitmap icon;

	public Base(File file) {
		this.file = file;
		setIcon("unknown.png");
//		new FileManager.Observer(file, (event, path) -> {
//			switch (event) {
//				case MODIFY -> {
//					Toast.makeText(requireContext(), "文件被修改", Toast.LENGTH_SHORT).show();
//					reloadContent();
//				}
//				case DELETE -> {
//					Toast.makeText(requireContext(), "文件被删除", Toast.LENGTH_SHORT).show();
//				}
//			}
//		});
	}

	protected final void setIcon(String path) {
		try {
			if (path.startsWith("/")) {
				icon = BitmapFactory.decodeFile(path);
			} else {
				InputStream is = MainActivity.getInstance().getAssets().open("Img/fileIcon/" + path);
				icon = BitmapFactory.decodeStream(is);
				is.close();
			}
		} catch (IOException ignored) {
		}
	}

	public final int getIndex() {
		return MainActivity.getInstance().tabManager.find(this);
	}

	public final void rename(String newName) {
		MainActivity.getInstance().tabManager.rename(this, newName);
	}

	public void close() {
		MainActivity.getInstance().tabManager.removePage(getIndex());
	}

	public boolean save() {
		return false;
	}

	public void reloadConfig() {
	}

	public void reloadContent() {
	}

	public final String getName() {
		return file.getName();
	}

	public final String getPath() {
		return file.getPath();
	}

	public final File getFile() {
		return file;
	}

	public Bitmap getIcon() {
		return icon;
	}
}
