package indi.aljet.myleafpic_dev.adapters;

import android.content.Context;

import java.security.Policy;
import java.util.ArrayList;
import java.util.List;

import horaapps.org.liz.ThemeHelper;
import horaapps.org.liz.ThemedAdapter;
import horaapps.org.liz.Themed;
import horaapps.org.liz.ThemedViewHolder;

/**
 * Created by PC-LJL on 2017/11/17.
 */

public abstract class BaseAdapter<T,
        VH extends ThemedViewHolder> extends ThemedAdapter
        <VH> implements Themed {

    private List<T> items;

    private Context context;

    public BaseAdapter(Context context) {
        super(context);
        items = new ArrayList<T>();
        this.context = context;
    }

    public BaseAdapter(Context context, List<T> items) {
        super(context);
        this.items = items;
    }

    public Context getContext() {
        return context;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public void setitems(List<T> items){
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void add(T item){
        items.add(item);
        notifyDataSetChanged();
    }

    public void add(int position,T item){
        items.add(position,item);
        notifyItemInserted(position);
    }

    public void removeItem(T item){
        items.remove(item);
        notifyDataSetChanged();
    }

    public void addAll(List<T> items){
        int start = this.items.size();
        this.items.addAll(items);
        notifyItemRangeInserted(start,items.size());
    }

    public void clear(){
        items.clear();
        notifyDataSetChanged();
    }

    public T getElement(int pos){
        return items.get(pos);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void refreshTheme(ThemeHelper theme) {
        setThemeHelper(theme);
        notifyDataSetChanged();
    }
}
