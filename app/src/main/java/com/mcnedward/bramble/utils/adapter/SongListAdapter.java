package com.mcnedward.bramble.utils.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.mcnedward.bramble.activity.AlbumPopup;
import com.mcnedward.bramble.media.Artist;
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
