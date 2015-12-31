package com.mcnedward.bramble.view.nowPlaying;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.utils.Extension;

/**
 * Created by edward on 27/12/15.
 */
public class NowPlayingSubControlsView extends RelativeLayout {
    private final static String TAG = "NowPlayingBottomControlsView";

    public static int SUBCONTROL_HEIGHT = 60;

    private ImageView imgAlbumArt;
    private TextView txtSongTitle;
    private ImageView btnPlay;

    public NowPlayingSubControlsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.now_playing_sub_control, this);
        imgAlbumArt = (ImageView) findViewById(R.id.now_playing_bottom_album_art);
        txtSongTitle = (TextView) findViewById(R.id.now_playing_bottom_song);
        btnPlay = (ImageView) findViewById(R.id.now_playing_bottom_play);
        Extension.setRippleBackground(btnPlay, R.color.FireBrick, 0, context);
    }

    public void setSongTitle(String songTitle) {
        txtSongTitle.setText(songTitle);
    }

    public void updateAlbumArt(Album album) {
        Extension.updateAlbumArt(album, imgAlbumArt);
    }

}
