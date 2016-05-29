package com.mcnedward.bramble.activity.fragment;

import com.mcnedward.bramble.adapter.grid.MediaGridAdapter;
import com.mcnedward.bramble.adapter.grid.SongGridAdapter;
import com.mcnedward.bramble.adapter.list.MediaListAdapter;
import com.mcnedward.bramble.adapter.list.SongListAdapter;
import com.mcnedward.bramble.entity.media.MediaType;
import com.mcnedward.bramble.entity.media.Song;
import com.mcnedward.bramble.repository.media.IMediaRepository;
import com.mcnedward.bramble.repository.media.SongRepository;

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
    public IMediaRepository<Song> createRepository() {
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
