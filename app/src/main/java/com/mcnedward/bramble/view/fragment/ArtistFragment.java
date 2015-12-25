package com.mcnedward.bramble.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.activity.AlbumPopup;
import com.mcnedward.bramble.media.Artist;
import com.mcnedward.bramble.media.MediaType;
import com.mcnedward.bramble.utils.adapter.ArtistListAdapter;
import com.mcnedward.bramble.utils.adapter.MediaListAdapter;
import com.mcnedward.bramble.utils.loader.ArtistDataLoader;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.artist_fragment_view, container, false);
    }

    @Override
    public Loader<List<Artist>> onCreateLoader(int id, Bundle args) {
        return new ArtistDataLoader(getActivity());
    }

    @Override
    protected void setOnItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), AlbumPopup.class);
        intent.putExtra("artist", (Artist) listView.getItemAtPosition(position));
        getActivity().startActivity(intent);
    }

    @Override
    public MediaListAdapter<Artist> getMediaListAdapter() {
        return new ArtistListAdapter(getActivity());
    }

    @Override
    protected int getLoaderId() {
        return LOADER_ID;
    }

}
