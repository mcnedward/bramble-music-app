package com.mcnedward.bramble.activity.fragment;

import com.mcnedward.bramble.adapter.grid.AlbumGridAdapter;
import com.mcnedward.bramble.adapter.grid.MediaGridAdapter;
import com.mcnedward.bramble.adapter.list.AlbumListAdapter;
import com.mcnedward.bramble.adapter.list.MediaListAdapter;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.MediaType;
import com.mcnedward.bramble.repository.media.AlbumRepository;
import com.mcnedward.bramble.repository.IRepository;
import com.mcnedward.bramble.repository.media.IMediaRepository;

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
    public IMediaRepository<Album> createRepository() {
        return new AlbumRepository(getActivity());
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
