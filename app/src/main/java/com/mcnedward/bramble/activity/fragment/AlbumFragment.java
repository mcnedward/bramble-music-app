package com.mcnedward.bramble.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.mcnedward.bramble.activity.AlbumActivity;
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
    public Loader<List<Album>> onCreateLoader(int id, Bundle args) {
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
    protected MediaListAdapter<Album> getMediaListAdapter() {
        return new AlbumListAdapter(getActivity());
    }

    @Override
    protected int getLoaderId() {
        return LOADER_ID;
    }
}
