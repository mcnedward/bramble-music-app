package com.mcnedward.bramble.utils.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.view.AlbumPopupItem;

import java.util.List;

/**
 * Created by edward on 22/12/15.
 */
public class AlbumGridAdapter extends BaseListAdapter<Album> {
    final private static String TAG = "AlbumGridAdapter";

    public AlbumGridAdapter(List<Album> albums, Context context) {
        super(albums, context);
    }

    @Override
    protected void setOnClickListener(Album media, View view) {

    }

    @Override
    protected View getCustomView(int position) {
        AlbumPopupItem albumPopupItem = new AlbumPopupItem(getItem(position), context);
        return albumPopupItem;
    }

    @Override
    protected void setViewContent(int position, View view) {
        Album album = getItem(position);
        ((AlbumPopupItem) view).setAlbumName(album.getAlbumName());
    }

}
