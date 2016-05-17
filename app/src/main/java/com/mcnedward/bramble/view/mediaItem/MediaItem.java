package com.mcnedward.bramble.view.mediaItem;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.media.Media;
import com.mcnedward.bramble.utils.RippleUtil;

/**
 * Created by edward on 26/12/15.
 */
public class MediaItem<T extends Media> extends RelativeLayout {
    private final static String TAG = "MediaItem";

    protected TextView mTxtTitle;
    protected GifView mGifView;
    protected Context mContext;
    protected T mMedia;

    public MediaItem(T media, Context context) {
        super(context);
        mContext = context;
        mMedia = media;
        inflate(context, R.layout.item_media, this);
        mTxtTitle = (TextView) findViewById(R.id.media_title);
        mGifView = (GifView) findViewById(R.id.media_now_playing_icon);
        setClickable(true);
        setFocusable(true);
        // TODO This can probably be done in the adapter
        RippleUtil.setRippleBackground(this, context);
        update(media);
    }

    public void update(T media) {
        mTxtTitle.setText(media.getTitle());
    }
}
