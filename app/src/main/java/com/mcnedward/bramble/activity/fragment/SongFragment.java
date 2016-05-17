package com.mcnedward.bramble.activity.fragment;

import android.view.View;
import android.widget.AdapterView;

import com.mcnedward.bramble.adapter.grid.MediaGridAdapter;
import com.mcnedward.bramble.adapter.grid.SongGridAdapter;
import com.mcnedward.bramble.adapter.list.MediaListAdapter;
import com.mcnedward.bramble.adapter.list.SongListAdapter;
import com.mcnedward.bramble.loader.BaseDataLoader;
import com.mcnedward.bramble.loader.SongDataLoader;
import com.mcnedward.bramble.media.MediaType;
import com.mcnedward.bramble.media.Song;
import com.mcnedward.bramble.repository.IRepository;
import com.mcnedward.bramble.repository.SongRepository;
import com.mcnedward.bramble.utils.MusicUtil;

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
    public IRepository<Song> createRepository() {
        return new SongRepository(getActivity());
    }

    @Override
    protected MediaListAdapter<Song> createMediaListAdapter() {
        return new SongListAdapter(getActivity());
    }

    @Override
    protected MediaGridAdapter<Song> createMediaGridAdapter() {
        return new SongGridAdapter(getActivity());
    }

    @Override
    protected int getLoaderId() {
        return LOADER_ID;
    }
}
