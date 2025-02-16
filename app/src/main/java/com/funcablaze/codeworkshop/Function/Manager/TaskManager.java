package com.funcablaze.codeworkshop.Function.Manager;

import java.util.ArrayList;
import java.util.List;

import android.view.View;

import androidx.annotation.NonNull;

public class TaskManager {

	private static final List<Task> tasks = new ArrayList<>();

	public static boolean newTask(Task task, String title) {
		tasks.add(task);
		return true;
	}

	public static boolean removeTask(int id) {
		tasks.remove(id);
		return true;
	}

	public static class Task extends Thread {
		private String title, info;
		private OnTaskListener onTaskListener;
		/**
		 * 进度，0到<code>max</code>，-1表示缓冲
		 */
		private int progress = -1, max;

		public Task(String title, String info, int max) {
			this.title = title;
			this.info = info;
			this.max = max;
		}

		public boolean changeProgress(int p) {
			if ((p + progress >= -1 & progress + p <= max)) {
				onTaskListener.onTaskUpdate(p);
				this.progress += p;
				return true;
			}
			return false;
		}

		public Task setOnTaskListener(OnTaskListener onTaskListener) {
			this.onTaskListener = onTaskListener;
			return this;
		}

		public interface OnTaskListener {
			void onTaskUpdate(int p);
		}

		public int getProgress() {
			return progress;
		}

		public int getMax() {
			return max;
		}

		public String getTitle() {
			return title;
		}

		public Task setTitle(String title) {
			this.title = title;
			return this;
		}

		public Task setMax(int max) {
			this.max = max;
			return this;
		}

		public String getInfo() {
			return info;
		}

		public Task setInfo(String info) {
			this.info = info;
			return this;
		}
	}
}
