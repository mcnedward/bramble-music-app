package com.mcnedward.bramble.adapter.grid;

import android.content.Context;
import android.view.View;

import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Song;
import com.mcnedward.bramble.utils.MediaCache;

import java.util.List;

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
