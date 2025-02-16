package com.funcablaze.codeworkshop.Function.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.funcablaze.codeworkshop.Activity.MainActivity;
import com.funcablaze.codeworkshop.Function.Manager.ProjectManager;
import com.funcablaze.codeworkshop.Function.Manager.TaskManager;
import com.funcablaze.codeworkshop.R;

import java.util.List;

public class TaskList extends RecyclerView.Adapter<TaskList.TaskViewHolder> {
	private final Context context;
	private final List<TaskManager.Task> TaskItems; // 用于显示的平铺列表

	public TaskList(Context context, List<TaskManager.Task> TaskItems) {
		this.context = context;
		this.TaskItems = TaskItems;
	}

	@NonNull
	@Override
	public TaskList.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.xml_task, parent, false);
		return new TaskViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull TaskList.TaskViewHolder holder, int position) {
		TaskManager.Task item = TaskItems.get(position);
		// 显示进度
		if (item.getMax() > 0) {
			holder.progress.setIndeterminate(false);
			holder.progress.setMax(item.getMax());
			holder.progress.setProgress(60);
		} else {
			holder.progress.setIndeterminate(true);
		}
		// 显示名称
		holder.taskTitle.setText(item.getTitle());
		holder.taskTitle.setTypeface(ProjectManager.DefaultFont);
		// 显示信息
		holder.taskInfo.setText(item.getInfo());
		holder.taskInfo.setTypeface(ProjectManager.DefaultFont);
		// 绑定长按事件
		holder.itemView.setOnLongClickListener(v -> {
//			Toast.makeText(context, "长按元素" + item.getName(), Toast.LENGTH_SHORT).show();
//			if (longClickListener != null) longClickListener.onItemLongClick(item);
			return true;
		});
	}

	// 长按监听器
	private OnItemLongClickListener longClickListener;

	public interface OnItemLongClickListener {
		void onItemLongClick(TaskManager.Task item);
	}

	public void setOnItemLongClickListener(OnItemLongClickListener listener) {
		this.longClickListener = listener;
	}

	@Override
	public int getItemCount() {
		return TaskItems.size();
	}

	public static class TaskViewHolder extends RecyclerView.ViewHolder {

		TextView taskTitle, taskInfo;
		ProgressBar progress;

		public TaskViewHolder(@NonNull View itemView) {
			super(itemView);
			taskTitle = itemView.findViewById(R.id.taskTitle);
			taskInfo = itemView.findViewById(R.id.taskInfo);
			progress = itemView.findViewById(R.id.taskProgress);
		}
	}
}
