package indi.aljet.myleafpic_dev.delete;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import horaapps.org.liz.ThemeHelper;
import horaapps.org.liz.ThemedViewHolder;
import horaapps.org.liz.ui.ThemedTextView;
import indi.aljet.myleafpic_dev.R;
import indi.aljet.myleafpic_dev.adapters.BaseAdapter;

/**
 * Created by PC-LJL on 2017/11/21.
 */

public class FolderAdapter extends BaseAdapter<Folder,
        FolderAdapter.ViewHolder> {

    public FolderAdapter(Context context, ArrayList<Folder> items) {
        super(context, items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.delete_folder_dialog_item,
                        parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.refreshTheme(getThemeHelper());
        Folder f = getElement(position);
        holder.folderName.setText(f.getName());
        if(f.getProgress() == -1 && -1 == f.getCount()){
            holder.count.setText(null);
        }else{
            holder.count.setText(String.format("%d/%d",
                    f.getProgress(), f.getCount()));
        }
    }

    static class ViewHolder extends ThemedViewHolder{

        @BindView(R.id.folder_name)
        ThemedTextView folderName;
        @BindView(R.id.file_count)
        ThemedTextView count;
        @BindView(R.id.text_dialog_rl)
        RelativeLayout llItemBackground;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }

        @Override
        public void refreshTheme(ThemeHelper themeHelper) {
            folderName.refreshTheme(themeHelper);
            folderName.refreshTheme(themeHelper);
        }
    }
}
