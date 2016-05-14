package com.mcnedward.bramble.adapter.grid;

import android.content.Context;
import android.view.View;

import com.mcnedward.bramble.adapter.list.MediaListAdapter;
import com.mcnedward.bramble.media.Song;

/**
 * Created by edward on 24/12/15.
 */
public class SongGridAdapter extends MediaGridAdapter<Song> {

    public SongGridAdapter(Context context) {
        super(context);
    }

    @Override
    protected String getMediaTitleText(Song media) {
        return media.getTitle();
    }

    @Override
    protected String getMediaIconPath(Song media) {
        return "";
    }

    @Override
    protected void setOnClickListener(Song song, View view) {
    }

}
