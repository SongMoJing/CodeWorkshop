package com.funcablaze.codeworkshop.Function.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.funcablaze.codeworkshop.Activity.MainActivity;
import com.funcablaze.codeworkshop.Function.Manager.ProjectManager;
import com.funcablaze.codeworkshop.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

public class TreeList extends RecyclerView.Adapter<TreeList.FileViewHolder> {
    private final Context context;
    private final List<FileItem> visibleItems; // 用于显示的平铺列表

    public TreeList(Context context, TreeSet<FileItem> rootItems) {
        this.context = context;
        this.visibleItems = new ArrayList<>();
        flattenTree(rootItems, 0); // 初始化可见项
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.xml_treelist, parent, false);
        return new FileViewHolder(view);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        FileItem item = visibleItems.get(position);
        // 显示文件或文件夹名称
        holder.textView.setText(item.getName());
        holder.textView.setTypeface(ProjectManager.DefaultFont);
        holder.textView.setSingleLine(true);
        // 根据层级调整左边距
        holder.back.setPadding(20 * item.getLevel(), 0, 0, 0);
        // 设置图标
        if (item.isFolder()) {
            holder.FileIconView.setImageResource(item.isExpanded() ? R.drawable.icon_files_open : R.drawable.icon_files_close);
        } else {
            AssetManager am = context.getAssets();
            try {
                holder.FileIconView.setImageDrawable(Drawable.createFromStream(switch (item.getName()) {
                    default -> am.open("Img/fileIcon/" + "unknown" + ".png");
                }, item.getName() + " icon"));
            } catch (IOException ignore) {
            }
        }
        holder.itemView.setOnClickListener(v -> {
            if (item.isFolder()) {
                if (item.isExpanded()) {
                    collapseItem(item);
                } else {
                    expandItem(item);
                }
            }
        });

        // 绑定长按事件
        holder.itemView.setOnLongClickListener(v -> {
            PopupMenu popup = new PopupMenu(context, v);
            if (item.isFolder()) {
                popup.getMenuInflater().inflate(R.menu.editor_directory, popup.getMenu());
            } else {
                popup.getMenuInflater().inflate(R.menu.editor_file, popup.getMenu());
            }
            popup.setOnMenuItemClickListener(i -> {
                switch (i.getItemId()) {
                    case R.id.menu_file_open:
                        MainActivity.getInstance().openEditor(ProjectManager.ProjectPath + item.getPath());
                        return true;
                    case R.id.menu_file_clone:

                        return true;
                    default:
                        return false;
                }
            });
            popup.show();
            if (longClickListener != null) longClickListener.onItemLongClick(item);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return visibleItems.size();
    }

    // 长按监听器
    private OnItemLongClickListener longClickListener;

    public interface OnItemLongClickListener {
        void onItemLongClick(FileItem item);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    // ViewHolder
    public static class FileViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView FileIconView;
        LinearLayout back;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            back = itemView.findViewById(R.id.tree_item_back);
            textView = itemView.findViewById(R.id.tree_item_name);
            FileIconView = itemView.findViewById(R.id.tree_item_icon);
        }
    }

    /**
     * 递归展开节点
     * @param item 节点
     */
    private void expandItem(FileItem item) {
        TreeSet<FileItem> children = item.reloadChildren();
        int position = visibleItems.indexOf(item);
        item.setExpanded(true);
        visibleItems.addAll(position + 1, children);
        notifyItemChanged(position);
        notifyItemRangeInserted(position + 1, children.size());
    }

    /**
     * 递归收起节点
     * @param item 节点
     */
    private boolean collapseItem(FileItem item) {
        int position = visibleItems.indexOf(item);
        if (position == -1) return false;
        int count = deleteChildren(item);
        item.setExpanded(false);
        notifyItemRangeRemoved(position + 1, count - 1);
        notifyItemChanged(position);
        return true;
    }

    /**
     * 向指定父节点添加子节点
     * @param parent 父节点
     * @param newItem 子节点
     */
    public void addItem(FileItem parent, FileItem newItem) {
        if (parent.isFolder()) {
            parent.getChildren().add(newItem); // 添加到子节点
            if (parent.isExpanded()) {
                // 找到 parent 的位置
                int position = visibleItems.indexOf(parent);
                int insertPosition = position + 1;

                // 计算插入位置
                for (FileItem sibling : parent.getChildren()) {
                    if (sibling.equals(newItem)) break;
                    insertPosition++;
                }

                // 更新 visibleItems 并通知适配器
                visibleItems.add(insertPosition, newItem);
                notifyItemInserted(insertPosition);
            }
        }
    }

    /**
     * 删除节点
     * @param item 节点
     */
    public void deleteItem(FileItem item) {
        int position = visibleItems.indexOf(item);
        if (position == -1) return;
        int removedCount = deleteChildren(item);
        visibleItems.remove(item);
        notifyItemRangeRemoved(position, removedCount + 1);
        FileItem parent = item.getParent();
        if (parent != null) {
            parent.getChildren().remove(item);
        }
    }

    /**
     * 递归删除子节点(不直接使用)
     * @param item 父节点
     * @return 删除的子节点数量
     */
    private int deleteChildren(FileItem item) {
        int count = 1;
        if (item.isExpanded()) {
            for (FileItem child : item.getChildren()) {
                count += deleteChildren(child);
                visibleItems.remove(child);
            }
        }
        return count;
    }

    /**
     * 重命名指定节点
     * @param item 节点
     * @param newName 新名称
     */
    public void renameItem(FileItem item, String newName) {
        int position = visibleItems.indexOf(item);
        item.setName(newName); // 更新名称
        notifyItemChanged(position); // 仅刷新指定位置
    }

    /**
     * 将树状结构平铺为可见列表
     * @param items 父元素
     * @param level 父元素的层级
     */
    private void flattenTree(TreeSet<FileItem> items, int level) {
        for (FileItem item : items) {
            visibleItems.add(item);
            if (item.isExpanded() && item.isFolder()) {
                flattenTree(item.getChildren(), level + 1);
            }
        }
    }

    public static class FileItem implements Comparable<FileItem> {
        private String name;
        private boolean isFolder, expanded;
        private final int level;
        private FileItem parent;
        private final TreeSet<FileItem> children = new TreeSet<>();

        public FileItem(String name, int level, boolean isFolder, boolean expanded) {
            this.name = name;
            this.level = level;
            this.isFolder = isFolder;
            this.expanded = expanded;
        }

        public FileItem(String name, int level, boolean isFolder) {
            this.name = name;
            this.level = level;
            this.isFolder = isFolder;
            this.expanded = false;
        }

        public FileItem setName(String name) {
            this.name = name;
            return this;
        }

        public FileItem setFolder(boolean isFolder) {
            this.isFolder = isFolder;
            return this;
        }

        public FileItem setParent(FileItem parent) {
            this.parent = parent;
            return this;
        }

        public FileItem setExpanded(boolean expanded) {
            this.expanded = expanded;
            return this;
        }

        public String getName() {
            return name;
        }

        public boolean isFolder() {
            return isFolder;
        }

        public FileItem getParent() {
            return parent;
        }

        public boolean isExpanded() {
            return expanded;
        }

        public TreeSet<FileItem> getChildren() {
            return children;
        }

        public TreeSet<FileItem> reloadChildren() {
            children.clear();
            File root = new File(ProjectManager.ProjectPath + getPath());
            if (root.exists() && root.isDirectory()) {
                for (File file : Objects.requireNonNull(root.listFiles()))
                    children.add(new FileItem(file.getName(), level + 1, file.isDirectory()).setParent(this));
            }
            return children;
        }

        public int getLevel() {
            return level;
        }

        public String getPath() {
            StringBuilder s = new StringBuilder();
            FileItem fi = this;
            for (int i = level; i > 0; i--) {
                s.insert(0, fi.name + '/');
                fi = fi.parent;
            }
            if (s.length() > 0) s.deleteCharAt(s.length() - 1);
            return s.toString();
        }

        @Override
        public int compareTo(FileItem other) {
            // 按名称排序（区分文件夹和文件）
            if (this.isFolder != other.isFolder) {
                return this.isFolder ? -1 : 1; // 文件夹排在文件之前
            }
            return this.name.compareTo(other.name); // 大小写排序
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof FileItem fileItem)) return false;
            return name.equals(fileItem.name) & isFolder == fileItem.isFolder & level == fileItem.level;
        }

        @Override
        public int hashCode() {
            return name.hashCode() + (isFolder ? 1 : 0);
        }
    }
}
