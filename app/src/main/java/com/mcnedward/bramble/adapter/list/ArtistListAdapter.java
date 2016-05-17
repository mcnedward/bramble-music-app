package com.mcnedward.bramble.adapter.list;

import android.content.Context;
import android.view.View;

import com.mcnedward.bramble.adapter.BaseMediaAdapter;
import com.mcnedward.bramble.media.Artist;
import com.mcnedward.bramble.utils.MusicUtil;
import com.mcnedward.bramble.view.MediaItem;

/**
 * Created by edward on 24/12/15.
 */
public class ArtistListAdapter extends MediaListAdapter<Artist> {

    public ArtistListAdapter(Context context) {
        super(context);
    }

    @Override
    protected void doOnClickAction(Artist artist, View view) {
        MusicUtil.startAlbumPopup(artist, mContext);
    }

}
