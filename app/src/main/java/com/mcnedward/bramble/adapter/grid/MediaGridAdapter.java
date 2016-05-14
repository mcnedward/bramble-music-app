package com.mcnedward.bramble.adapter.grid;

import android.content.Context;
import android.view.View;

import com.mcnedward.bramble.adapter.BaseMediaAdapter;
import com.mcnedward.bramble.media.Media;
import com.mcnedward.bramble.utils.Extension;
import com.mcnedward.bramble.utils.PicassoUtil;
import com.mcnedward.bramble.utils.RippleUtil;
import com.mcnedward.bramble.view.grid.MediaGridItem;
import com.squareup.picasso.Picasso;

/**
 * Created by Edward on 5/14/2016.
 */
public abstract class MediaGridAdapter<T extends Media> extends BaseMediaAdapter<T> {

    public MediaGridAdapter(Context context) {
        super(context);
    }

    @Override
    protected View getCustomView(int position) {
        return new MediaGridItem(context);
    }

    @Override
    protected void setViewContent(int position, View view) {
        T media = getItem(position);
        final MediaGridItem mediaGridItem = (MediaGridItem) view;
        mediaGridItem.setMediaTitleText(getMediaTitleText(media));
        mediaGridItem.setMediaIconPath(getMediaIconPath(media));
        RippleUtil.setRippleBackground(mediaGridItem, context);
    }

    protected abstract String getMediaTitleText(T media);

    protected abstract String getMediaIconPath(T media);
}
