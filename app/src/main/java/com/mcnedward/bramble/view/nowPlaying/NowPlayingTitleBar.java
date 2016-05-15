package com.mcnedward.bramble.view.nowPlaying;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.utils.MusicUtil;
import com.mcnedward.bramble.utils.RippleUtil;

import java.util.List;

/**
 * Created by edward on 27/12/15.
 */
public class NowPlayingTitleBar extends LinearLayout {
    private final static String TAG = "NowPlayingTitleBar";

    // Layout height + padding
    public static int HEIGHT = 75;

    private Context context;

    private ImageView imgAlbumArt;
    private TextView txtNowPlayingTitle;
    private TextView txtNowPlayingAlbum;
    private ImageView btnPlay;

    public NowPlayingTitleBar(Context context) {
        super(context, null);
        initialize(context);
    }

    public NowPlayingTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    private void initialize(Context context) {
        this.context = context;
        inflate(context, R.layout.view_now_playing_title_bar, this);
        imgAlbumArt = (ImageView) findViewById(R.id.now_playing_bottom_album_art);
        txtNowPlayingTitle = (TextView) findViewById(R.id.now_playing_title);
        txtNowPlayingAlbum = (TextView) findViewById(R.id.now_playing_album);
        btnPlay = (ImageView) findViewById(R.id.now_playing_title_play);
        RippleUtil.setRippleBackground(btnPlay, R.color.FireBrick, 0, context);
    }

    public void updateAlbumArt(Album album) {
        MusicUtil.loadAlbumArt(album.getAlbumArt(), imgAlbumArt, context);
    }

    public void updateSongTitle(String songTitle) {
        txtNowPlayingTitle.setText(songTitle);
    }

    public void updateAlbumTitle(String albumTitle) {
        txtNowPlayingAlbum.setText(albumTitle);
    }

    public void setPlayButtonListener(final List<ImageView> playButtons, final MediaPlayer player) {
        btnPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicUtil.setPlayButtonListener(playButtons, player, context);
            }
        });
    }

    public void switchPlayIcon(boolean top) {
        if (top) {
            btnPlay.animate().alpha(0f).setDuration(100);
            btnPlay.setVisibility(GONE);
        } else {
            btnPlay.animate().alpha(1f).setDuration(100);
            btnPlay.setVisibility(VISIBLE);
        }
    }

    public ImageView getPlayButton() {
        return btnPlay;
    }

}
