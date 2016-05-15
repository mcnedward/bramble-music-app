package com.mcnedward.bramble.adapter.list;

import android.content.Context;
import android.view.View;

import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Artist;
import com.mcnedward.bramble.utils.MediaCache;

import java.util.List;

/**
 * Created by edward on 24/12/15.
 */
public class AlbumListAdapter extends MediaListAdapter<Album> {

    public AlbumListAdapter(Context context) {
        super(context);
    }

    @Override
    protected List<Album> getGroups() {
        return MediaCache.getAlbums();
    }

    @Override
    protected void setOnClickListener(Album album, View view) {
    }

}
