package com.mcnedward.bramble.adapter.list;

import android.content.Context;

import com.mcnedward.bramble.entity.media.Artist;
import com.mcnedward.bramble.utils.MusicUtil;
import com.mcnedward.bramble.view.mediaItem.MediaItem;

/**
 * Created by edward on 24/12/15.
 */
public class ArtistListAdapter extends MediaListAdapter<Artist> {

    public ArtistListAdapter(Context context) {
        super(context);
    }

    @Override
    protected void doOnClickAction(Artist artist, MediaItem view) {
        MusicUtil.startAlbumPopup(mContext, artist);
    }

}
