package com.mcnedward.bramble.activity.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.mcnedward.bramble.activity.AlbumActivity;
import com.mcnedward.bramble.adapter.grid.AlbumGridAdapter;
import com.mcnedward.bramble.adapter.grid.MediaGridAdapter;
import com.mcnedward.bramble.adapter.list.AlbumListAdapter;
import com.mcnedward.bramble.adapter.list.MediaListAdapter;
import com.mcnedward.bramble.loader.AlbumDataLoader;
import com.mcnedward.bramble.loader.BaseDataLoader;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.MediaType;

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
    public BaseDataLoader<Album> createDataLoader() {
        return new AlbumDataLoader(getActivity());
    }

    @Override
    protected void setOnItemClick(AdapterView<?> parent, View view, int position, long id) {
        Album album = (Album) listView.getItemAtPosition(position);
        Log.d(TAG, "Starting AlbumActivity for " + album + "!");
        Intent intent = new Intent(getActivity(), AlbumActivity.class);
        intent.putExtra("album", album);
        getActivity().startActivity(intent);
    }

    @Override
    protected MediaListAdapter<Album> createMediaListAdapter() {
        return new AlbumListAdapter(getActivity());
    }

    @Override
    protected MediaGridAdapter<Album> createMediaGridAdapter() {
        return new AlbumGridAdapter(getActivity());
    }

    @Override
    protected int getLoaderId() {
        return LOADER_ID;
    }
}
