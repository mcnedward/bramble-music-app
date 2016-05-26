package com.mcnedward.bramble.adapter.list;

import android.content.Context;

import com.mcnedward.bramble.media.Song;
import com.mcnedward.bramble.utils.MusicUtil;
import com.mcnedward.bramble.view.mediaItem.MediaItem;
import com.mcnedward.bramble.view.mediaItem.SongMediaItem;

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
    protected MediaItem<Song> getCustomView(Song song) {
        return new SongMediaItem(song, mContext);
    }

    @Override
    protected void doOnClickAction(Song song, MediaItem view) {
        MusicUtil.startPlayingMusic(mContext, song);
        ((SongMediaItem) view).setGifViewCurrentSong(song);
        view.update(song);
    }

}
