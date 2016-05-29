package com.mcnedward.bramble.adapter.list;

import android.content.Context;

import com.mcnedward.bramble.entity.media.Album;
import com.mcnedward.bramble.utils.MusicUtil;
import com.mcnedward.bramble.view.mediaItem.MediaItem;

/**
 * Created by edward on 24/12/15.
 */
public class AlbumListAdapter extends MediaListAdapter<Album> {

    public AlbumListAdapter(Context context) {
        super(context);
    }

    @Override
    protected void doOnClickAction(Album album, MediaItem view) {
        MusicUtil.openAlbum(mContext, album);
    }

}
