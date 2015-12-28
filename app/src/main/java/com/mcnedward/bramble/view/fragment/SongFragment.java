package com.mcnedward.bramble.view.fragment;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.mcnedward.bramble.activity.MainActivity;
import com.mcnedward.bramble.exception.MediaNotFoundException;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.MediaType;
import com.mcnedward.bramble.media.Song;
import com.mcnedward.bramble.utils.Extension;
import com.mcnedward.bramble.utils.adapter.MediaListAdapter;
import com.mcnedward.bramble.utils.adapter.SongListAdapter;
import com.mcnedward.bramble.utils.loader.SongDataLoader;

import java.util.List;
import java.util.Random;

/**
 * Created by edward on 24/12/15.
 */
public class SongFragment extends MediaFragment<Song> {
    private final static String TAG = "SongFragment";
    private final static int LOADER_ID = new Random().nextInt();

    public SongFragment() {
        super(MediaType.SONG);
    }

    @Override
    public Loader<List<Song>> onCreateLoader(int id, Bundle args) {
        return new SongDataLoader(getActivity());
    }

    @Override
    protected void setOnItemClick(AdapterView<?> parent, View view, int position, long id) {
        Song song = (Song) listView.getItemAtPosition(position);
        Extension.startPlayingMusic(song, getActivity());
    }

    @Override
    protected MediaListAdapter<Song> getMediaListAdapter() {
        return new SongListAdapter(getActivity());
    }

    @Override
    protected int getLoaderId() {
        return LOADER_ID;
    }
}
