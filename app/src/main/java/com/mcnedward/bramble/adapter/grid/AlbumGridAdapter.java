package com.mcnedward.bramble.adapter.grid;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.mcnedward.bramble.activity.AlbumActivity;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.utils.MediaCache;
import com.mcnedward.bramble.utils.MusicUtil;

import java.util.List;

/**
 * Created by Edward on 5/14/2016.
 */
public class AlbumGridAdapter extends MediaGridAdapter<Album> {

    public AlbumGridAdapter(Context context) {
        super(context);
    }

    public AlbumGridAdapter(Context context, List<Album> albums) {
        super(context, albums);
    }

    @Override
    protected void doOnClickAction(Album album, View view) {
        MusicUtil.openAlbum(mContext, album);
    }

}
