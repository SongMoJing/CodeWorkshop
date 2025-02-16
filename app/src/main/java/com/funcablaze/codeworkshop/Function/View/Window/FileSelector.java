package com.funcablaze.codeworkshop.Function.View.Window;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.funcablaze.codeworkshop.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

public class FileSelector extends Frame {

	private FileType fileType;
	private LinearLayout v_path;
	private RecyclerView v_list;
	private TextView v_selected;
	private Button v_ok;
	private final TreeSet<FileItem> files = new TreeSet<>();
	private final OnSelectListener listener;

	public enum FileType {
		File,
		Dir,
		All
	}

	public interface OnSelectListener {
		void onSelect(String path);
	}

	public FileSelector(Context context, int layoutId, FileType fileType, String path, OnSelectListener listener) {
		super(context, layoutId);
		getParams().width = 1200;
		getParams().height = 600;
		this.fileType = fileType;
		this.listener = listener;
		init(path);
	}

	private void init(String path) {
		v_path = getView().findViewById(R.id.win_fSelector_path);
		v_list = getView().findViewById(R.id.win_fSelector_list);
		v_selected = getView().findViewById(R.id.win_fSelector_selected);
		v_list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
		v_ok = getView().findViewById(R.id.win_fSelector_sure);
		v_ok.setOnClickListener(v -> {
			if (v_selected.getText().toString().isEmpty()) {
				listener.onSelect(getPath(v_path.getChildCount()));
			} else {
				listener.onSelect(getPath(v_path.getChildCount()) + "/" + v_selected.getText());
			}
			del();
		});
		getView().findViewById(R.id.win_fSelector_back).setOnClickListener(v -> {
			closeDir();
		});
		getView().findViewById(R.id.win_fSelector_close).setOnClickListener(v -> {
			del();
		});
		jumpDir(path, 0);
	}

	private void update() {
		File root = new File(getPath(v_path.getChildCount()));
		if (root.isDirectory()) {
			files.clear();
			File[] is = root.listFiles();
			if (is != null) {
				for (File cf : is) {
					if (cf.isFile()) {
						if (fileType == FileType.File || fileType == FileType.All) {
							files.add(new FileItem(cf.getName(), cf.isDirectory()));
						}
					} else {
						if (fileType == FileType.Dir || fileType == FileType.All) {
							files.add(new FileItem(cf.getName(), cf.isDirectory()));
						}
					}
				}
			}
			FileAdapter adapter = new FileAdapter(files);
			v_list.setAdapter(null);
			v_list.setAdapter(adapter);
		}
	}

	private void select(String name) {
		v_selected.setText(name);
	}

	private boolean openDir(String name) {
		View.inflate(getContext(), R.layout.xml_window_selector_file_path_item, v_path);
		v_selected.setText("");
		int childCount = v_path.getChildCount() - 1;
		((TextView) v_path.getChildAt(childCount).findViewById(R.id.win_fSelector_path_item)).setText(name);
		v_path.getChildAt(childCount).setOnClickListener(v -> {
			jumpDir(getPath(childCount), childCount);
		});
		update();
		return true;
	}

	private boolean closeDir() {
		if (v_path.getChildCount() > 1) {
			v_path.removeView(v_path.getChildAt(v_path.getChildCount() - 1));
			update();
			return true;
		} else {
			return false;
		}
	}

	private boolean jumpDir(String path, int index) {
		String[] split = path.split("/");
		if (split.length > 0) {
			split[0] = "/";
			for (int i = index; i < v_path.getChildCount(); i++) {
				v_path.removeView(v_path.getChildAt(index + 1));
			}
			for (int i = index; i < split.length; i++) {
				View.inflate(getContext(), R.layout.xml_window_selector_file_path_item, v_path);
				((TextView) v_path.getChildAt(i).findViewById(R.id.win_fSelector_path_item)).setText(split[i]);
				int j = i;
				v_path.getChildAt(i).setOnClickListener(v -> {
					jumpDir(getPath(j), j);
				});
			}
			update();
		}
		return false;
	}

	private String getPath(int index) {
		if (index <= v_path.getChildCount()) {
			StringBuilder sb = new StringBuilder().append('/');
			for (int i = 1; i < index; i++) {
				sb.append(((TextView) v_path.getChildAt(i).findViewById(R.id.win_fSelector_path_item)).getText()).append('/');
			}
			if (sb.length() > 1) sb.deleteCharAt(sb.length() - 1);
			return sb.toString();
		} else {
			return "null";
		}
	}

	public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {
		private ArrayList<FileItem> files;
		private FileItem selected;

		public FileAdapter(TreeSet<FileItem> files) {
			this.files = new ArrayList<>(files);
		}

		@NonNull
		@Override
		public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.xml_selector_file, parent, false);
			return new ViewHolder(view);
		}

		@Override
		public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
			FileItem item = files.get(position);
			holder.text.setText(item.name);
			holder.icon.setImageResource(item.isDir ? R.drawable.icon_filetype_dir : R.drawable.icon_filetype_file);
			holder.itemView.setOnClickListener(v -> {
				if (selected == item) {
					if (item.isDir) openDir(item.name);
				} else {
					selected = item;
					select(item.name);
				}
			});
		}

		@Override
		public int getItemCount() {
			return files.size();
		}

		public static class ViewHolder extends RecyclerView.ViewHolder {
			public TextView text;
			public ImageView icon;

			public ViewHolder(View itemView) {
				super(itemView);
				text = itemView.findViewById(R.id.win_fSelector_file_name);
				icon = itemView.findViewById(R.id.win_fSelector_file_icon);
			}
		}
	}

	public static class FileItem implements Comparable<FileItem> {
		public final String name;
		public final boolean isDir;

		public FileItem(String name, boolean isDir) {
			this.name = name;
			this.isDir = isDir;
		}

		@Override
		public int compareTo(FileItem o) {
			if (this.isDir != o.isDir) {
				return this.isDir ? -1 : 1;
			} else {
				return this.name.compareTo(o.name);
			}
		}
	}
}
