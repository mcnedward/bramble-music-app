package com.mcnedward.bramble.utils.refresh;

import android.content.Context;
import android.widget.ListView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.media.Artist;
import com.mcnedward.bramble.utils.adapter.MediaListAdapter;

import java.util.Iterator;
import java.util.List;

/**
 * Created by edward on 24/12/15.
 */
public class ArtistRefresher extends Refresher<Artist> {

    public ArtistRefresher(Context context) {
        super(context);
    }

    @Override
    public int getResourceId() {
        return R.id.displayArtists;
    }

    @Override
    protected void setupListView(ListView view, MediaListAdapter<Artist> adapter, List<Artist> mediaList) {
        for (Iterator<Artist> iterator = mediaList.iterator(); iterator.hasNext();) {
            adapter.addGroup(iterator.next());
            view.setAdapter(adapter);
            view.setClickable(true);
        }
    }
}
