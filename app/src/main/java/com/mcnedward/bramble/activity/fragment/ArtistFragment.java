package com.mcnedward.bramble.activity.fragment;

import android.view.View;
import android.widget.AdapterView;

import com.mcnedward.bramble.adapter.grid.ArtistGridAdapter;
import com.mcnedward.bramble.adapter.grid.MediaGridAdapter;
import com.mcnedward.bramble.adapter.list.ArtistListAdapter;
import com.mcnedward.bramble.adapter.list.MediaListAdapter;
import com.mcnedward.bramble.loader.ArtistDataLoader;
import com.mcnedward.bramble.loader.BaseDataLoader;
import com.mcnedward.bramble.media.Artist;
import com.mcnedward.bramble.media.MediaType;
import com.mcnedward.bramble.repository.ArtistRepository;
import com.mcnedward.bramble.repository.IRepository;
import com.mcnedward.bramble.utils.MusicUtil;

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
    protected void setOnItemClick(AdapterView<?> parent, View view, int position, long id) {
        MusicUtil.startAlbumPopup((Artist) listView.getItemAtPosition(position), getActivity());
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
