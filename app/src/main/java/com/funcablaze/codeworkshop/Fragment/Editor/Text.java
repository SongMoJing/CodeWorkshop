package com.funcablaze.codeworkshop.Fragment.Editor;

import static android.os.FileObserver.DELETE;
import static android.os.FileObserver.MODIFY;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.funcablaze.codeworkshop.Activity.MainActivity;
import com.funcablaze.codeworkshop.Function.Manager.FileManager;
import com.funcablaze.codeworkshop.Function.Manager.ProjectManager;
import com.funcablaze.codeworkshop.Function.View.CodeEditor.CompletionLayout;
import com.funcablaze.codeworkshop.databinding.FragmentProjectTextBinding;

import java.io.File;

import io.github.rosemoe.sora.event.ContentChangeEvent;
import io.github.rosemoe.sora.event.EditorFocusChangeEvent;
import io.github.rosemoe.sora.langs.textmate.TextMateColorScheme;
import io.github.rosemoe.sora.langs.textmate.TextMateLanguage;
import io.github.rosemoe.sora.langs.textmate.registry.ThemeRegistry;
import io.github.rosemoe.sora.widget.CodeEditor;
import io.github.rosemoe.sora.widget.component.EditorAutoCompletion;
import io.github.rosemoe.sora.widget.style.CursorAnimator;

public class Text extends Base {

    private FragmentProjectTextBinding binding;
    private CodeEditor editor;
    private boolean isSaved = true;
    private boolean isReadOnly = false;

    public Text(File filePath) {
        super(filePath);
        setIcon("txt.png");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProjectTextBinding.inflate(getLayoutInflater());
        init();
        return binding.getRoot();
    }

    private void init() {
        editor = binding.Editor;
        EditorAutoCompletion editorAutoCompletion = editor.getComponent(EditorAutoCompletion.class);
        editorAutoCompletion.applyColorScheme();
//        editorAutoCompletion.setLayout(new CompletionLayout());
        // 设置字体
        editor.setNonPrintablePaintingFlags(CodeEditor.FLAG_DRAW_WHITESPACE_LEADING
                | CodeEditor.FLAG_DRAW_LINE_SEPARATOR
                | CodeEditor.FLAG_DRAW_WHITESPACE_IN_SELECTION);
        reloadConfig();
        loadLanguage();
        reloadContent();
        setInputListener();
    }

    private void loadLanguage() {
        try {
            var suffix = file.getPath().substring(file.getPath().lastIndexOf("."));
            if (!MainActivity.languages.containsKey(suffix)) {
                MainActivity.languages.put(suffix, TextMateLanguage.create(switch (suffix) {
                    case ".bat" -> "source.batchfile";
                    case ".c" -> "source.c";
                    case ".cpp" -> "source.cpp";
                    case ".cs" -> "source.cs";
                    case ".css" -> "source.css";
                    case ".go" -> "source.go";
                    case ".htm", ".html" -> "text.html.basic";
                    case ".ini" -> "source.ini";
                    case ".java" -> "source.java";
                    case ".js" -> "source.js";
                    case ".json" -> "source.json";
                    case ".kt" -> "source.kotlin";
                    case ".lua" -> "source.lua";
                    case ".py" -> "source.python";
                    case ".rs" -> "source.rust";
                    case ".sql" -> "source.sql";
                    case ".xml" -> "text.xml";
                    case ".yam" -> "source.yaml";
                    default -> "text";
                }, ProjectManager.AutoComplete));
            }
            editor.setColorScheme(TextMateColorScheme.create(ThemeRegistry.getInstance()));
            editor.setEditorLanguage(MainActivity.languages.get(suffix));
        } catch (Exception ignore) {}
    }

    @Override
    public void reloadConfig() {
        editor.setTypefaceText(ProjectManager.EditorFont);
        editor.setTypefaceLineNumber(ProjectManager.EditorFont);
        editor.setEditable(!isReadOnly);
        editor.setLineSpacing(ProjectManager.LineHeight, 1);
        editor.setWordwrap(ProjectManager.AutoWrap);
        editor.setLineNumberEnabled(ProjectManager.ShowLine);
        editor.setPinLineNumber(ProjectManager.FixedLine);
    }

    private void setInputListener() {
        editor.subscribeEvent(ContentChangeEvent.class, ((event, unsubscribe) -> {
            isSaved = false;
            rename("*" + getName());
            unsubscribe.unsubscribe();
        }));
    }

    @Override
    public boolean save() {
        if (!isSaved) {
            rename(getName());
            FileManager.WriteFile(file.getPath(), editor.getText().toString());
            isSaved = true;
            setInputListener();
            return true;
        } else {
            return false;
        }
    }

    public boolean isSaved() {
        return isSaved;
    }

    @Override
    public void reloadContent() {
        super.reloadContent();
        if (isSaved) {
            editor.setText(FileManager.ReadFile(file.getPath()));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        save();
        if (editor != null) {
            editor.release();
        }
    }
}