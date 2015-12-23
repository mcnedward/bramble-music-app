package com.mcnedward.bramble.utils.adapter;

import android.content.Context;
import android.view.View;

import com.mcnedward.bramble.media.Album;

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
        return null;
    }

    @Override
    protected void setViewContent(int position, View view) {

    }

}
