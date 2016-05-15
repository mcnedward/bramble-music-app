package com.mcnedward.bramble.adapter.grid;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.mcnedward.bramble.activity.AlbumPopup;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Artist;
import com.mcnedward.bramble.utils.MediaCache;

import java.util.List;

/**
 * Created by edward on 24/12/15.
 */
public class ArtistGridAdapter extends MediaGridAdapter<Artist> {

    public ArtistGridAdapter(Context context) {
        super(context);
    }

    @Override
    protected List<Artist> getGroups() {
        return MediaCache.getArtists();
    }

    @Override
    protected void setOnClickListener(Artist artist, View view) {
        Intent intent = new Intent(context, AlbumPopup.class);
        intent.putExtra("artist", artist);
        context.startActivity(intent);
    }

}
