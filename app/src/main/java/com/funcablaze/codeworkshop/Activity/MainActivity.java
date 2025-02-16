package com.funcablaze.codeworkshop.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.funcablaze.codeworkshop.Fragment.Editor.*;
import com.funcablaze.codeworkshop.Function.Manager.FileManager;
import com.funcablaze.codeworkshop.Function.Manager.ProjectManager;
import com.funcablaze.codeworkshop.Function.View.Splitter;
import com.funcablaze.codeworkshop.Function.View.TabManager;
import com.funcablaze.codeworkshop.Function.View.Window.FileSelector;
import com.funcablaze.codeworkshop.Function.View.Window.Frame;
import com.funcablaze.codeworkshop.R;
import com.funcablaze.codeworkshop.databinding.ActivityMainBinding;
import com.jakewharton.processphoenix.ProcessPhoenix;

import org.eclipse.tm4e.core.registry.IThemeSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import io.github.rosemoe.sora.langs.textmate.TextMateLanguage;
import io.github.rosemoe.sora.langs.textmate.registry.FileProviderRegistry;
import io.github.rosemoe.sora.langs.textmate.registry.GrammarRegistry;
import io.github.rosemoe.sora.langs.textmate.registry.ThemeRegistry;
import io.github.rosemoe.sora.langs.textmate.registry.model.ThemeModel;
import io.github.rosemoe.sora.langs.textmate.registry.provider.AssetsFileResolver;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

	private ActivityMainBinding binding;
	private static MainActivity main;
	public static Map<String, TextMateLanguage> languages = new LinkedHashMap<>();
	public TabManager tabManager;
	public Splitter splitter;
	public int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //虚拟按键
			| View.SYSTEM_UI_FLAG_FULLSCREEN //状态栏
			| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION // 不介入布局
			| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY; //保持总是

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EdgeToEdge.enable(this);
		binding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		getWindow().getDecorView().setSystemUiVisibility(uiOptions);
		main = this;
		tabManager = new TabManager(binding.editor.topBar, binding.editor.viewpage);
		launchSettings();
		_init();
	}

	private void _init() {
		ProjectManager.getExternalStoragePermission();
		splitter = binding.splitter;
		splitter._init();
		binding.toolbar.toolMenu.setOnClickListener(this);
		binding.toolbar.toolFolder.setOnClickListener(this);
		binding.toolbar.toolTerminal.setOnClickListener(this);
		binding.toolbar.toolWindow.setOnClickListener(this);
		binding.toolbar.toolSetting.setOnClickListener(this);
//		TaskManager.newTask(0, new TaskManager.Window(500, 500, 0, 0, false));
	}

	private void launchSettings() {
		FileProviderRegistry.getInstance().addFileProvider(new AssetsFileResolver(getApplicationContext().getAssets()));
		var themeAssetsPath = "Editor/theme/" + ProjectManager.Theme[ProjectManager.themeSelect] + ".json";
		ThemeModel themeModel = new ThemeModel(IThemeSource.fromInputStream(Objects.requireNonNull(FileProviderRegistry.getInstance().tryGetInputStream(themeAssetsPath)), themeAssetsPath, null), ProjectManager.Theme[ProjectManager.themeSelect]);
		themeModel.setDark(true);
		try {
			ThemeRegistry.getInstance().loadTheme(themeModel);
			ThemeRegistry.getInstance().setTheme(ProjectManager.Theme[ProjectManager.themeSelect]);
		} catch (Exception ignore) {
		}
		ThemeRegistry.getInstance().setTheme("Dark");
		GrammarRegistry.getInstance().loadGrammars("Editor/language.json");
	}

	public void openEditor(String path) {
		Base page;
		FileManager.FileType fileType = FileManager.getType(path);
		File file = new File(path);
		if (fileType != null) {
			page = switch (fileType) {
				case TXT -> new Text(file);
				case JPEG, PNG, GIF, TIFF -> new Image(fileType, file);
				case DOC, PPT, XLS, PDF -> new Document(fileType, file);
				case DOCX, PPTX, XLSX, ZIP -> FileManager.find_504B0304_inFile(fileType, path);
				case RAR -> new Zip(fileType, file);
				case WAV, MP3 -> new Audio(file);
				case AVI, MP4 -> new Video(file);
				case TTF -> new Font(file);
			};
		} else {
			page = new Unknown(file);
		}
		tabManager.addPage(page);
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		if (v.equals(binding.toolbar.toolMenu)) {
			PopupMenu popup = new PopupMenu(this, v);
			popup.getMenuInflater().inflate(R.menu.menu_nav, popup.getMenu());
			popup.setOnMenuItemClickListener(this);
			popup.show();
		} else if (v.equals(binding.toolbar.toolFolder)) {
			splitter.showLeftView(0);
		} else if (v.equals(binding.toolbar.toolTerminal)) {
			splitter.showLeftView(1);
		} else if (v.equals(binding.toolbar.toolWindow)) {
			splitter.showLeftView(2);
		} else if (v.equals(binding.toolbar.toolSetting)) {
			File file = new File(getFilesDir(), "setting.json");
			if (!file.exists())
				try {
					if (file.createNewFile()) {
						FileOutputStream outputStream = new FileOutputStream(file);
						InputStream open = getAssets().open("Files/Setting.json");
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
							outputStream.write(open.readAllBytes());
						}
						open.close();
						outputStream.close();
					}
				} catch (IOException ignored) {
				}
			openEditor(file.getAbsolutePath());
		}
	}

	public static MainActivity getInstance() {
		return main;
	}

	@SuppressLint("NonConstantResourceId")
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_nav_file_open:
				Frame selectFile = new FileSelector(this, R.layout.window_selector_file, FileSelector.FileType.All, ProjectManager.ProjectPath, p -> {
					openEditor(p);
				});
				selectFile.show();
				return true;
			case R.id.menu_nav_file_save:
				if (tabManager.getCurrentPage() != null) {
					Base editor = tabManager.getCurrentPage();
					editor.save();
				}
				return true;
			default:
				return false;
		}
	}

	/**
	 * 关闭项目
	 */
	private void closeProject() {
		// 保存项目
		getSharedPreferences("Project", MODE_PRIVATE).edit()
				.putString("route", ProjectManager.ProjectPath)
				.apply();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		closeProject();
	}
}