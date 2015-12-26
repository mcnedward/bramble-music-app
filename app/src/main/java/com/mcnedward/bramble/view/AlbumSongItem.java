package com.mcnedward.bramble.view;

import android.content.Context;
import android.graphics.drawable.RippleDrawable;
import android.util.Log;
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

        RippleDrawable rippleDrawable = Extension.rippleDrawable(context);
        setBackground(rippleDrawable);

        final Context c = context;
        final String title = song.getTitle();
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, title + " CLICKED!");
            }
        });
    }
}
