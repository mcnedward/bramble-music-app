package com.mcnedward.bramble.adapter.list;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.adapter.BaseMediaAdapter;
import com.mcnedward.bramble.media.Media;
import com.mcnedward.bramble.utils.RippleUtil;
import com.mcnedward.bramble.view.MediaItem;

import java.util.List;

/**
 * Created by edward on 22/12/15.
 */
public abstract class MediaListAdapter<T extends Media> extends BaseMediaAdapter<T> {

    public MediaListAdapter(Context context) {
        super(context);
    }

    public MediaListAdapter(List<T> groups, Context context) {
        super(groups, context);
    }

    @Override
    protected View getCustomView(T media) {
        return new MediaItem(media, mContext);
    }

    @Override
    protected void setViewContent(T media, View view) {
        MediaItem mediaView = ((MediaItem) view);
        mediaView.update(media);
    }

}
