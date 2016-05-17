package com.mcnedward.bramble.view.mediaItem;

import android.content.Context;

import com.mcnedward.bramble.listener.SongPlayingListener;
import com.mcnedward.bramble.media.Song;

/**
 * Created by edward on 26/12/15.
 */
public class SongMediaItem extends MediaItem<Song> implements SongPlayingListener {
    private final static String TAG = "SongMediaItem";

    public SongMediaItem(Song song, Context context) {
        super(song, context);
    }

    public void update(Song media) {
        super.update(media);

    }

    @Override
    public void notifySongChange(Song currentSong, boolean isPlaying) {
        if (mMedia != null && currentSong != null) {
            if (mMedia.getId() == currentSong.getId()) {
                mGifView.play(true);
            } else {
                mGifView.play(false);
            }
        }
    }

}
