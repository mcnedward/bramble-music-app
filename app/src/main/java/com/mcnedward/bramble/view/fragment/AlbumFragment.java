package com.mcnedward.bramble.view.fragment;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.MediaType;
import com.mcnedward.bramble.utils.adapter.AlbumListAdapter;
import com.mcnedward.bramble.utils.adapter.MediaListAdapter;
import com.mcnedward.bramble.utils.loader.AlbumDataLoader;

import java.util.List;
import java.util.Random;

/**
 * Created by edward on 24/12/15.
 */
public class AlbumFragment extends MediaFragment<Album> {
    private final static String TAG = "AlbumFragment";
    private final static int LOADER_ID = new Random().nextInt();

    public AlbumFragment() {
        super(MediaType.ALBUM);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.album_fragment_view, container, false);
    }

    @Override
    public Loader<List<Album>> onCreateLoader(int id, Bundle args) {
        return new AlbumDataLoader(getActivity());
    }

    @Override
    protected void setOnItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected MediaListAdapter<Album> getMediaListAdapter() {
        return new AlbumListAdapter(getActivity());
    }

    @Override
    protected int getLoaderId() {
        return LOADER_ID;
    }
}