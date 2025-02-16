package com.funcablaze.codeworkshop.Function.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.funcablaze.codeworkshop.Activity.MainActivity;
import com.funcablaze.codeworkshop.Fragment.Editor.Base;
import com.funcablaze.codeworkshop.R;

import java.util.ArrayList;
import java.util.List;

public class ViewPage extends FrameLayout {

    protected final List<Base> pages = new ArrayList<>();
    private FragmentManager fragmentManager;
    private int showingPage = -1;

    public ViewPage(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ViewPage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewPage(@NonNull Context context) {
        super(context);
    }

    protected Base getCurrentPage() {
        if (showingPage == -1) {
            return null;
        } else {
            return pages.get(showingPage);
        }
    }

    protected void addPage(Base base) {
        pages.add(base);
        if (fragmentManager == null) {
            fragmentManager = MainActivity.getInstance().getSupportFragmentManager();
        }
        fragmentManager.beginTransaction()
                .add(getId(), base).commit();
        selectPage(pages.size() - 1);
    }

    protected Base removePage(int index) {
        Base page = pages.get(index);
        fragmentManager.beginTransaction()
                .remove(page).commit();
        pages.remove(index);
        return page;
    }

    protected void removePage(Base base) {
        for (int i = 0; i < pages.size(); i++)
            if (pages.get(i) == base) {
                removePage(i);
                break;
            }
    }

    protected void selectPage(int index) {
        fragmentManager.beginTransaction()
                .show(pages.get(index)).commit();
        if (showingPage != -1 & showingPage != index) {
            fragmentManager.beginTransaction()
                    .hide(pages.get(showingPage)).commit();
        }
        showingPage = index;
    }
}
