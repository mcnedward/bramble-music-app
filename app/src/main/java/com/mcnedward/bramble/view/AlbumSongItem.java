package com.mcnedward.bramble.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.RippleDrawable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.media.Song;
import com.mcnedward.bramble.utils.Extension;

/**
 * Created by edward on 26/12/15.
 */
public class AlbumSongItem extends RelativeLayout {
    private final static String TAG = "AlbumSongItem";

    public AlbumSongItem(Song song, Context context) {
        super(context);
        inflate(context, R.layout.album_song_item, this);
        ((TextView) findViewById(R.id.song_title)).setText(song.getTitle());
        ((TextView) findViewById(R.id.song_track)).setText(String.valueOf(song.getTrack()));
        ((TextView) findViewById(R.id.song_duration)).setText(song.getDuration());
        setClickable(true);
        setFocusable(true);

        Extension.setRippleBackground(this, context);

        final Activity activity = (Activity) context;
        final Song nowPlayingSong = song;
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Extension.startNowPlayingActivity(nowPlayingSong, activity);
            }
        });
    }
}
