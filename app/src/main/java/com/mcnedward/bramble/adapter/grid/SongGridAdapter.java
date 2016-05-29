package com.mcnedward.bramble.adapter.grid;

import android.content.Context;
import android.view.View;

import com.mcnedward.bramble.entity.media.Song;

/**
 * Created by edward on 24/12/15.
 */
public class SongGridAdapter extends MediaGridAdapter<Song> {

    public SongGridAdapter(Context context) {
        super(context);
    }

    @Override
    protected void doOnClickAction(Song song, View view) {
    }

}
