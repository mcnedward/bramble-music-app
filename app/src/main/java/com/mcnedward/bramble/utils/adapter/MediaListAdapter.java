package com.mcnedward.bramble.utils.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.media.Media;

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
        if (position != 0) {
            final T media = getItem(position);
            if (view instanceof TextView)
                ((TextView) view).setText(media.toString());
        }
    }

}
