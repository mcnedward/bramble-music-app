package com.mcnedward.bramble.adapter.grid;

import android.content.Context;
import android.view.View;

import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.utils.MediaCache;

import java.util.List;

/**
 * Created by Edward on 5/14/2016.
 */
public class AlbumGridAdapter extends MediaGridAdapter<Album> {

    public AlbumGridAdapter(Context context) {
        super(context);
    }

    @Override
    protected List<Album> getGroups() {
        return MediaCache.getAlbums();
    }

    @Override
    protected void setOnClickListener(Album media, View view) {

    }

}
