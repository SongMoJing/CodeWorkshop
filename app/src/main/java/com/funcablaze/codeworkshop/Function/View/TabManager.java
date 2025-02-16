package com.funcablaze.codeworkshop.Function.View;

import com.funcablaze.codeworkshop.Fragment.Editor.Base;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TabManager {

    private Tab tab;
    private ViewPage viewPage;
    private Map<String, String> list = new LinkedHashMap<>();

    public TabManager(Tab tab, ViewPage viewPage) {
        this.tab = tab;
        this.viewPage = viewPage;
    }

    public Base getCurrentPage() {
        return viewPage.getCurrentPage();
    }

    public void change(int position){
        tab.selectTab(position);
        viewPage.selectPage(position);
    }

    public void addPage(Base page){
        String path = page.getPath();
        if (list.containsKey(path)) {
            int index = 0;
            for (Map.Entry<String, String> item : list.entrySet()) {
                if (item.getKey().equals(path)) break;
                index ++;
            }
            change(index);
            return;
        }
        tab.addTab(page.getName(), page);
        viewPage.addPage(page);
        list.put(path, page.getName());
    }

    public int find(Base page) {
        return viewPage.pages.indexOf(page);
    }

    public void removePage(int index) {
        list.remove(viewPage.removePage(tab.removeTab(index)).getPath());
    }

    public void rename(Base base, String newName) {
        tab.rename(find(base), newName);
        list.put(base.getPath(), newName);
    }

    public String getName(Base base) {
        return tab.getName(find(base));
    }
}
