package com.mcnedward.bramble.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.media.Media;
import com.mcnedward.bramble.media.MediaType;
import com.mcnedward.bramble.utils.adapter.MediaListAdapter;

import java.util.List;

/**
 * Created by edward on 26/12/15.
 */
public abstract class MediaFragment<T extends Media> extends Fragment implements LoaderManager.LoaderCallbacks<List<T>> {
    private final static String TAG = "ArtistFragment";

    private MediaType mediaType;

    protected ListView listView;
    protected ProgressBar progressBar;
    private TextView txtProgress;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View thisView = inflater.inflate(R.layout.media_fragment_view, container, false);

        listView = (ListView) thisView.findViewById(R.id.media_list);
        listView.setItemsCanFocus(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setOnItemClick(parent, view, position, id);
            }
        });
        adapter = getMediaListAdapter();
        listView.setAdapter(adapter);

        progressBar = (ProgressBar) thisView.findViewById(R.id.media_progress_bar);
        txtProgress = (TextView) thisView.findViewById(R.id.media_progress_text);
        switch (mediaType) {
            case ARTIST:
                txtProgress.setText(getContext().getString(R.string.artist_loading_text));
                break;
            case ALBUM:
                txtProgress.setText(getContext().getString(R.string.album_loading_text));
                break;
            case SONG:
                txtProgress.setText(getContext().getString(R.string.song_loading_text));
                break;
        }

        return thisView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, String.format("Calling %s Fragment initLoader with id %s!", mediaType.type(), getLoaderId()));
        getLoaderManager().initLoader(getLoaderId(), savedInstanceState, this);
    }

    @Override
    public void onLoadFinished(Loader<List<T>> loader, List<T> data) {
        Log.d(TAG, String.format("onLoadFinished() called! Loading %s data!", mediaType.type()));
        adapter.reset();
        adapter.setGroups(data);
        adapter.notifyDataSetChanged();

        if (progressBar != null)
            progressBar.setVisibility(View.GONE);
        if (txtProgress != null)
            txtProgress.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<T>> loader) {
        adapter.reset();
        loader.reset();
    }
}
