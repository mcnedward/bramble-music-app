package com.mcnedward.bramble.view.nowPlaying;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcnedward.bramble.R;

/**
 * Created by edward on 27/12/15.
 */
public class NowPlayingTitleBar extends LinearLayout {

    private TextView txtNowPlayingTitle;
    private TextView txtNowPlayingAlbum;

    public NowPlayingTitleBar(Context context) {
        super(context, null);
    }

    public NowPlayingTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.now_playing_title_bar, this);
        txtNowPlayingTitle = (TextView) findViewById(R.id.now_playing_title);
        txtNowPlayingAlbum = (TextView) findViewById(R.id.now_playing_album);
    }

    public void setTitleText(String songTitle, String albumTitle) {
        txtNowPlayingTitle.setText(songTitle);
        txtNowPlayingAlbum.setText(albumTitle);
    }

}
