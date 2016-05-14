package com.mcnedward.bramble.utils.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.mcnedward.bramble.activity.AlbumPopup;
import com.mcnedward.bramble.media.Artist;

/**
 * Created by edward on 24/12/15.
 */
public class ArtistListAdapter extends MediaListAdapter<Artist> {

    public ArtistListAdapter(Context context) {
        super(context);
    }

    @Override
    protected void setOnClickListener(Artist artist, View view) {
        Intent intent = new Intent(context, AlbumPopup.class);
        intent.putExtra("artist", artist);
        context.startActivity(intent);
    }

}
