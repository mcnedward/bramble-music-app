package com.mcnedward.bramble.utils.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.mcnedward.bramble.activity.AlbumActivity;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.view.AlbumPopupItem;

import java.util.List;

/**
 * Created by edward on 22/12/15.
 */
public class AlbumGridAdapter extends BrambleBaseAdapter<Album> {
    final private static String TAG = "AlbumGridAdapter";

    public AlbumGridAdapter(List<Album> albums, Context context) {
        super(albums, context);
    }

    @Override
    protected View getCustomView(final int position) {
        AlbumPopupItem albumPopupItem = new AlbumPopupItem(getItem(position), context);
        return albumPopupItem;
    }

    @Override
    protected void setViewContent(int position, View view) {
        Album album = getItem(position);
        ((AlbumPopupItem) view).setAlbumName(album.getAlbumName());
    }

}
