package com.mcnedward.bramble.activity.fragment;

import com.mcnedward.bramble.adapter.grid.ArtistGridAdapter;
import com.mcnedward.bramble.adapter.grid.MediaGridAdapter;
import com.mcnedward.bramble.adapter.list.ArtistListAdapter;
import com.mcnedward.bramble.adapter.list.MediaListAdapter;
import com.mcnedward.bramble.media.Artist;
import com.mcnedward.bramble.media.MediaType;
import com.mcnedward.bramble.repository.media.ArtistRepository;
import com.mcnedward.bramble.repository.IRepository;

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
    public IRepository<Artist> createRepository() {
        return new ArtistRepository(getActivity());
    }

    @Override
    public MediaListAdapter<Artist> createMediaListAdapter() {
        return new ArtistListAdapter(getActivity());
    }

    @Override
    protected MediaGridAdapter<Artist> createMediaGridAdapter() {
        return new ArtistGridAdapter(getActivity());
    }

    @Override
    protected int getLoaderId() {
        return LOADER_ID;
    }

}
