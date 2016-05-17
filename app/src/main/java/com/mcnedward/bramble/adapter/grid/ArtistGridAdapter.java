package com.mcnedward.bramble.adapter.grid;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.mcnedward.bramble.activity.AlbumPopup;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Artist;
import com.mcnedward.bramble.utils.MediaCache;
import com.mcnedward.bramble.utils.MusicUtil;

import java.util.List;

/**
 * Created by edward on 24/12/15.
 */
public class ArtistGridAdapter extends MediaGridAdapter<Artist> {

    public ArtistGridAdapter(Context context) {
        super(context);
    }

    @Override
    protected void doOnClickAction(Artist artist, View view) {
        MusicUtil.startAlbumPopup(artist, mContext);
    }

}
