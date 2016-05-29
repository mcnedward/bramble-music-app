package com.mcnedward.bramble.view.mediaItem;

import android.content.Context;

import com.mcnedward.bramble.entity.media.Song;

/**
 * Created by edward on 26/12/15.
 */
public class SongMediaItem extends MediaItem<Song> {
    private final static String TAG = "SongMediaItem";

    public SongMediaItem(Song song, Context context) {
        super(song, context);
    }

    public void setGifViewCurrentSong(Song song) {
        mGifView.setCurrentSong(song);
    }

}
