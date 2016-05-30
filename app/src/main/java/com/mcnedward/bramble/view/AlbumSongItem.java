package com.mcnedward.bramble.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.listener.MediaChangeListener;
import com.mcnedward.bramble.entity.media.Album;
import com.mcnedward.bramble.entity.media.Song;
import com.mcnedward.bramble.utils.MusicUtil;
import com.mcnedward.bramble.utils.RippleUtil;
import com.mcnedward.bramble.view.mediaItem.GifView;

/**
 * Created by edward on 26/12/15.
 */
public class AlbumSongItem extends RelativeLayout implements MediaChangeListener {
    private final static String TAG = "AlbumSongItem";

    private Song mSong;
    private TextView mTxtTrack;
    private GifView mGifView;

    public AlbumSongItem(Album album, Song song, Context context) {
        super(context);
        inflate(context, R.layout.item_album_song, this);
        ((TextView) findViewById(R.id.song_title)).setText(song.getTitle());
        mTxtTrack = (TextView) findViewById(R.id.song_track);
        mTxtTrack.setText(String.valueOf(song.getTrack()));
        ((TextView) findViewById(R.id.song_duration)).setText(song.getDuration());
        mGifView = (GifView) findViewById(R.id.song_gif_view);
        mGifView.update(song);
        setClickable(true);
        setFocusable(true);

        RippleUtil.setRippleBackground(this, context);

        final Activity activity = (Activity) context;
        mSong = song;
        final Album nowPlayingAlbum = album;
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicUtil.startPlayingMusic(activity, mSong, nowPlayingAlbum);
            }
        });
    }

    @Override
    public void onMediaPlayStateChange(Song currentSong, boolean playing) {
        mGifView.onMediaPlayStateChange(currentSong, playing);
    }

    @Override
    public void onMediaChange(Song currentSong, boolean playing) {
        mGifView.onMediaChange(currentSong, playing);
        if (mSong.getId() == currentSong.getId())
            mTxtTrack.setVisibility(INVISIBLE);
        else
            mTxtTrack.setVisibility(VISIBLE);
    }

    @Override
    public void onMediaStop(Song song) {

    }
}
