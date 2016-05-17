package com.mcnedward.bramble.adapter.list;

import android.content.Context;
import android.view.View;

import com.mcnedward.bramble.media.Song;
import com.mcnedward.bramble.utils.MusicUtil;

import java.util.List;

/**
 * Created by edward on 24/12/15.
 */
public class SongListAdapter extends MediaListAdapter<Song> {

    public SongListAdapter(Context context) {
        super(context);
    }

    public SongListAdapter(List<Song> songs, Context context) {
        super(songs, context);
    }

    @Override
    protected void doOnClickAction(Song song, View view) {
        MusicUtil.startPlayingMusic(song, mContext);
    }

}
