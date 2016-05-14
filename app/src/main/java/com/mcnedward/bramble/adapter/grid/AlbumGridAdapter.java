package com.mcnedward.bramble.adapter.grid;

import android.content.Context;
import android.view.View;

import com.mcnedward.bramble.media.Album;

/**
 * Created by Edward on 5/14/2016.
 */
public class AlbumGridAdapter extends MediaGridAdapter<Album> {

    public AlbumGridAdapter(Context context) {
        super(context);
    }

    @Override
    protected void setOnClickListener(Album media, View view) {

    }

    @Override
    protected String getMediaTitleText(Album media) {
        return media.getAlbumName();
    }

    @Override
    protected String getMediaIconPath(Album media) {
        return media.getAlbumArt();
    }

}
