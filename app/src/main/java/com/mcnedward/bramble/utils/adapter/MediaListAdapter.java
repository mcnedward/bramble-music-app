package com.mcnedward.bramble.utils.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.media.Media;
import com.mcnedward.bramble.utils.Extension;

/**
 * Created by edward on 22/12/15.
 */
public abstract class MediaListAdapter<T extends Media> extends BaseListAdapter<T> {

    public MediaListAdapter(Context context) {
        super(context);
    }

    @Override
    protected View getCustomView(int position) {
        return inflater.inflate(R.layout.simple_text_item, null);
    }

    @Override
    protected void setViewContent(int position, View view) {
        T media = getItem(position);
        final TextView textView = ((TextView) view);
        textView.setText(media.toString());
        textView.setBackground(Extension.rippleDrawable(context));
    }

}
