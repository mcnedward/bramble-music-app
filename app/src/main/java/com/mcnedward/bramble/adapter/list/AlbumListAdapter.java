package com.mcnedward.bramble.adapter.list;

import android.content.Context;
import android.view.View;

import com.mcnedward.bramble.adapter.BaseMediaAdapter;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.utils.MusicUtil;
import com.mcnedward.bramble.view.MediaItem;

/**
 * Created by edward on 24/12/15.
 */
public class AlbumListAdapter extends MediaListAdapter<Album> {

    public AlbumListAdapter(Context context) {
        super(context);
    }

    @Override
    protected void doOnClickAction(Album album, View view) {
        MusicUtil.openAlbum(album, mContext);
    }

}
