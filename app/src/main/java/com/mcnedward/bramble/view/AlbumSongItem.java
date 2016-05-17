package com.mcnedward.bramble.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Song;
import com.mcnedward.bramble.utils.MusicUtil;
import com.mcnedward.bramble.utils.RippleUtil;

/**
 * Created by edward on 26/12/15.
 */
public class AlbumSongItem extends RelativeLayout {
    private final static String TAG = "AlbumSongItem";

    public AlbumSongItem(Album album, Song song, Context context) {
        super(context);
        inflate(context, R.layout.item_album_song, this);
        ((TextView) findViewById(R.id.song_title)).setText(song.getTitle());
        ((TextView) findViewById(R.id.song_track)).setText(String.valueOf(song.getTrack()));
        ((TextView) findViewById(R.id.song_duration)).setText(song.getDuration());
        setClickable(true);
        setFocusable(true);

        RippleUtil.setRippleBackground(this, context);

        final Activity activity = (Activity) context;
        final Song nowPlayingSong = song;
        final Album nowPlayingAlbum = album;
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicUtil.startPlayingMusic(nowPlayingSong, nowPlayingAlbum, activity);
            }
        });
    }
}
