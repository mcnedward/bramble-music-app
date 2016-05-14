package com.mcnedward.bramble.activity.fragment;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView;

import com.mcnedward.bramble.adapter.grid.ArtistGridAdapter;
import com.mcnedward.bramble.adapter.grid.MediaGridAdapter;
import com.mcnedward.bramble.media.Artist;
import com.mcnedward.bramble.media.MediaType;
import com.mcnedward.bramble.utils.Extension;
import com.mcnedward.bramble.adapter.list.ArtistListAdapter;
import com.mcnedward.bramble.adapter.list.MediaListAdapter;
import com.mcnedward.bramble.loader.ArtistDataLoader;

import java.util.List;
import java.util.Random;

/**
 * Created by edward on 24/12/15.
 */
public class ArtistFragment extends MediaFragment<Artist> {
    private final static String TAG = "ArtistFragment";
    private final static int LOADER_ID = new Random().nextInt();

    public ArtistFragment() {
        super(MediaType.ARTIST);
    }

    @Override
    public Loader<List<Artist>> onCreateLoader(int id, Bundle args) {
        return new ArtistDataLoader(getActivity());
    }

    @Override
    protected void setOnItemClick(AdapterView<?> parent, View view, int position, long id) {
        Extension.startAlbumPopup((Artist) listView.getItemAtPosition(position), getActivity());
    }

    @Override
    public MediaListAdapter<Artist> getMediaListAdapter() {
        return new ArtistListAdapter(getActivity());
    }

    @Override
    protected MediaGridAdapter<Artist> getMediaGridAdapter() {
        return new ArtistGridAdapter(getActivity());
    }

    @Override
    protected int getLoaderId() {
        return LOADER_ID;
    }

}
