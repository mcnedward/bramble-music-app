package com.mcnedward.bramble.adapter.grid;

import android.content.Context;
import android.view.View;

import com.mcnedward.bramble.media.Song;

/**
 * Created by edward on 24/12/15.
 */
public class SongGridAdapter extends MediaGridAdapter<Song> {

    public SongGridAdapter(Context context) {
        super(context);
    }

    @Override
    protected void setOnClickListener(Song song, View view) {
    }

}
