package com.mcnedward.bramble.utils.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.mcnedward.bramble.media.Song;

/**
 * Created by edward on 25/12/15.
 */
public class AlbumPopupListAdapter extends MediaListAdapter<Song> {

    private int spaceHeight;

    public AlbumPopupListAdapter(int spaceHeight, Context context) {
        super(context);
        this.spaceHeight = spaceHeight;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @Override
    protected void setOnClickListener(Song media, View view) {

    }
}
