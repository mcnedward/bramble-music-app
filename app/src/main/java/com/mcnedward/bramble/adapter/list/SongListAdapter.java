package com.mcnedward.bramble.adapter.list;

import android.content.Context;
import android.view.View;

import com.mcnedward.bramble.media.Song;

/**
 * Created by edward on 24/12/15.
 */
public class SongListAdapter extends MediaListAdapter<Song> {

    public SongListAdapter(Context context) {
        super(context);
    }

    @Override
    protected void setOnClickListener(Song song, View view) {
    }

}
