package com.mcnedward.bramble.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.media.Media;
import com.mcnedward.bramble.media.MediaType;
import com.mcnedward.bramble.utils.adapter.MediaListAdapter;

import java.util.List;
import java.util.Random;

/**
 * Created by edward on 24/12/15.
 */
public abstract class MediaFragment<T extends Media> extends Fragment implements LoaderManager.LoaderCallbacks<List<T>> {
    private final static String TAG = "ArtistFragment";

    private MediaType mediaType;
    protected ListView listView;
    protected MediaListAdapter<T> adapter;

    public MediaFragment(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public static MediaFragment newInstance(MediaType mediaType) {
        switch (mediaType) {
            case ARTIST:
                return new ArtistFragment();
            case ALBUM:
                return new AlbumFragment();
            case SONG:
                return new SongFragment();
        }
        return null;
    }

    protected abstract void setOnItemClick(AdapterView<?> parent, View view, int position, long id);

    protected abstract MediaListAdapter<T> getMediaListAdapter();

    protected abstract int getLoaderId();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int resourceId;
        switch (mediaType) {
            case ARTIST:
                resourceId = R.id.artist_list;
                break;
            case ALBUM:
                resourceId = R.id.album_list;
                break;
            case SONG:
                resourceId = R.id.song_list;
                break;
            default:
                resourceId = R.id.media_list;
                break;
        }
        listView = (ListView) getActivity().findViewById(resourceId);
        listView.setItemsCanFocus(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setOnItemClick(parent, view, position, id);
            }
        });
        adapter = getMediaListAdapter();
        listView.setAdapter(adapter);

        Log.d(TAG, String.format("Calling %s Fragment initLoader with id %s!", mediaType.type(), getLoaderId()));
        getLoaderManager().initLoader(getLoaderId(), savedInstanceState, this);
    }

    @Override
    public void onLoadFinished(Loader<List<T>> loader, List<T> data) {
        Log.d(TAG, String.format("onLoadFinished() called! Loading %s data!", mediaType.type()));
        adapter.reset();
        adapter.setGroups(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<T>> loader) {
        adapter.reset();
        loader.reset();
    }

}
