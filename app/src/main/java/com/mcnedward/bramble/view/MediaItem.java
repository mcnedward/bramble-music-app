package com.mcnedward.bramble.view;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.media.Media;
import com.mcnedward.bramble.media.MediaType;
import com.mcnedward.bramble.media.Song;
import com.mcnedward.bramble.service.MediaService;
import com.mcnedward.bramble.utils.MediaCache;
import com.mcnedward.bramble.utils.PicassoUtil;
import com.mcnedward.bramble.utils.RippleUtil;

/**
 * Created by edward on 26/12/15.
 */
public class MediaItem<T extends Media> extends RelativeLayout {
    private final static String TAG = "MediaItem";

    private TextView mTxtTitle;
    private ImageView mImgIcon;
    private Context mContext;

    public MediaItem(Media media, Context context) {
        super(context);
        mContext = context;
        inflate(context, R.layout.item_media, this);
        mTxtTitle = (TextView) findViewById(R.id.media_title);
        mImgIcon = (ImageView) findViewById(R.id.media_now_playing_icon);
        setClickable(true);
        setFocusable(true);
        // TODO This can probably be done in the adapter
        RippleUtil.setRippleBackground(this, context);
        update(media);
    }

    public void update(Media media) {
        mTxtTitle.setText(media.getTitle());
        if (media.getMediaType().equals(MediaType.SONG)) {
            Song currentSong = MediaCache.getSong(mContext);
            if (currentSong.getId() == media.getId() && MediaService.isPlaying()) {
                PicassoUtil.getPicasso(mContext).with(mContext).load(R.drawable.now_playing_gif).into(mImgIcon);
            }
        }
    }
}
