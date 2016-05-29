package com.mcnedward.bramble.adapter.list;

import android.content.Context;

import com.mcnedward.bramble.entity.media.Media;
import com.mcnedward.bramble.view.mediaItem.MediaItem;

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
    protected MediaItem getCustomView(T media) {
        return new MediaItem(media, mContext);
    }

}
