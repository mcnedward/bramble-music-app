package com.mcnedward.bramble.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by edward on 22/12/15.
 */
public abstract class BrambleBaseAdapter<T> extends BaseAdapter {

    protected List<T> groups;
    protected Context context;
    protected LayoutInflater inflater;

    public BrambleBaseAdapter(List<T> groups, Context context) {
        this.groups = groups;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    protected abstract View getCustomView(int position);

    protected abstract void setViewContent(int position, View view);

    public void addGroup(T group) {
        groups.add(group);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = getCustomView(position);
        }
        setViewContent(position, convertView);
        return convertView;
    }

    @Override
     public int getCount() {
        return groups.size();
    }

    @Override
    public T getItem(int position) {
        return groups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
