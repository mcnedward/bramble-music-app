package com.mcnedward.bramble.adapter.grid;

import android.content.Context;
import android.view.View;

import com.mcnedward.bramble.entity.media.Artist;
import com.mcnedward.bramble.utils.MusicUtil;
import com.mcnedward.bramble.view.card.MediaCard;

/**
 * Created by edward on 24/12/15.
 */
public class ArtistGridAdapter extends MediaGridAdapter<Artist> {

    public ArtistGridAdapter(Context context) {
        super(context);
    }

    @Override
    protected void doOnClickAction(Artist artist, MediaCard view) {
        MusicUtil.startArtistActivity(mContext, artist);
    }

}
