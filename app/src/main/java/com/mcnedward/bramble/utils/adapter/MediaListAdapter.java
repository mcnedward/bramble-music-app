package com.mcnedward.bramble.utils.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.media.Media;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward on 22/12/15.
 */
public abstract class MediaListAdapter<T extends Media> extends BaseListAdapter<T> {

    public MediaListAdapter(Context context) {
        super(context);
    }

    @Override
    protected View getCustomView(int position) {
        return inflater.inflate(R.layout.simple_list_item, null);
    }

    @Override
    protected void setViewContent(int position, View view) {
        final T media = getItem(position);
        ((TextView) view).setText(media.toString());
    }

}
