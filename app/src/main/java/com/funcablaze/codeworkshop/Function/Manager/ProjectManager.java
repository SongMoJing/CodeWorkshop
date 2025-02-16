package com.funcablaze.codeworkshop.Function.Manager;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;

import com.funcablaze.codeworkshop.Activity.MainActivity;
import com.jakewharton.processphoenix.ProcessPhoenix;

public class ProjectManager {
	/**
	 * 全局软件设置
	 */
	//临时文件位置
	public static String TempPath = "/cache/TempFile/";
	// 默认项目位置
//		public static String ProjectPath = "/storage/emulated/0/Code/Project/";
	public static String ProjectPath = "/storage/emulated/0/Code/";
	// 默认Git位置
	public static String GitPath = "/storage/emulated/0/Code/Git/";
	// 目标暂存位置，基于项目路径的相对路径
	public static String TargetPath = "../.target/";
	// 文件选择器默认起始位置
	public static String FileSelectorPath = "/storage/emulated/0/Code/";
	// 启用外设键盘快捷键
	public static boolean AllowedExternalKeyboardShortcuts = true;
	/**
	 * 代码编辑器设置
	 */
	// 编辑器字体
	public static Typeface EditorFont = Typeface.createFromAsset(MainActivity.getInstance().getAssets(), "Font/jet_brains_mono.ttf");
	// 默认字体
	public static Typeface DefaultFont = Typeface.createFromAsset(MainActivity.getInstance().getAssets(), "Font/roboto_regular.ttf");
	// 默认编码
	public static String DefaultEncoding = "UTF-8";
	// 显示行号
	public static boolean ShowLine = true;
	// 固定行号
	public static boolean FixedLine = true;
	// 自动换行
	public static boolean AutoWrap = false;
	// 行高
	public static int LineHeight = 8;
	// 自动补全
	public static boolean AutoComplete = true;
	// 主题
	public static String[] Theme = {"Abyss", "darkGreen", "keen"};
	public static int themeSelect = 0;

	public static void getExternalStoragePermission() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			if (!Environment.isExternalStorageManager()) {
				Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
				intent.setData(Uri.parse("package:" + MainActivity.getInstance().getPackageName()));
				MainActivity.getInstance().startActivity(intent);
				Toast.makeText(MainActivity.getInstance(), "读取文件，需要使用文件管理权限", Toast.LENGTH_SHORT).show();
				ProcessPhoenix.triggerRebirth(MainActivity.getInstance());
			}
		}
	}
}
