package com.mcnedward.bramble.view;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.activity.AlbumActivity;
import com.mcnedward.bramble.media.Album;

/**
 * Created by edward on 23/12/15.
 */
public class AlbumPopupItem extends LinearLayout {
    final private static String TAG = "AlbumPopupItem";

    private Context context;
    private TextView txtAlbumName;

    public AlbumPopupItem(final Album album, Context context) {
        super(context);
        this.context = context;
        inflate(context, R.layout.album_popup_item, this);

        txtAlbumName = (TextView) findViewById(R.id.albumName);
        setAlbumName(album.getAlbumName());

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startAlbumActivity(album);
            }
        });
    }

    private void startAlbumActivity(Album album) {
        Log.d(TAG, "Starting AlbumActivity for " + album + "!");
        Intent intent = new Intent(context, AlbumActivity.class);
        intent.putExtra("album", album);
        context.startActivity(intent);
    }

    public void setAlbumName(String name) {
        txtAlbumName.setText(name);
    }

}
