package com.mcnedward.bramble.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward on 24/12/15.
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {

    protected List<T> groups;
    protected Context context;
    protected LayoutInflater inflater;

    public BaseListAdapter(Context context) {
        this(new ArrayList<T>(), context);
    }

    public BaseListAdapter(List<T> groups, Context context) {
        this.groups = groups;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setGroups(List<T> groups) {
        this.groups = groups;
    }

    public void reset() {
        groups = new ArrayList<>();
    }

    protected abstract View getCustomView(int position);

    protected abstract void setViewContent(int position, View view);

    protected abstract void setOnClickListener(T media, View view);

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = getCustomView(position);
        }
        setViewContent(position, convertView);
//        final int pos = position;
//        final View view = convertView;
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setOnClickListener(getItem(pos), view);
//            }
//        });
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
