package com.mcnedward.bramble.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.adapter.grid.MediaGridAdapter;
import com.mcnedward.bramble.loader.BaseDataLoader;
import com.mcnedward.bramble.media.Media;
import com.mcnedward.bramble.media.MediaType;
import com.mcnedward.bramble.adapter.list.MediaListAdapter;
import com.mcnedward.bramble.repository.IRepository;

import java.util.List;

/**
 * Created by edward on 26/12/15.
 */
public abstract class MediaFragment<T extends Media> extends Fragment implements LoaderManager.LoaderCallbacks<List<T>> {
    private final static String TAG = "ArtistFragment";

    private MediaType mediaType;

    protected ListView listView;
    protected GridView gridView;

    protected BaseDataLoader<T> mDataLoader;
    protected MediaListAdapter<T> mListAdapter;
    protected MediaGridAdapter<T> mGridAdapter;

    protected ProgressBar progressBar;
    private TextView txtProgress;

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

    protected abstract IRepository<T> createRepository();

    protected abstract MediaListAdapter<T> createMediaListAdapter();

    protected abstract MediaGridAdapter<T> createMediaGridAdapter();

    protected abstract int getLoaderId();

    public void toggleMediaView(boolean grid) {
        if (grid) {
            listView.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View thisView = inflater.inflate(R.layout.fragment_media, container, false);

        progressBar = (ProgressBar) thisView.findViewById(R.id.media_progress_bar);
        txtProgress = (TextView) thisView.findViewById(R.id.media_progress_text);

        setupList(thisView);
        setupGrid(thisView);

        switch (mediaType) {
            case ARTIST:
                txtProgress.setText(getContext().getString(R.string.artist_loading_text));
                toggleMediaView(false);
                break;
            case ALBUM:
                txtProgress.setText(getContext().getString(R.string.album_loading_text));
                toggleMediaView(true);
                break;
            case SONG:
                txtProgress.setText(getContext().getString(R.string.song_loading_text));
                toggleMediaView(false);
                break;
        }

        return thisView;
    }

    private void setupList(View view) {
        listView = (ListView) view.findViewById(R.id.list_media);
        listView.setItemsCanFocus(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setOnItemClick(parent, view, position, id);
            }
        });
        if (mListAdapter == null)
            mListAdapter = createMediaListAdapter();
        listView.setAdapter(mListAdapter);

        if (mListAdapter.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            txtProgress.setVisibility(View.VISIBLE);
        }
    }

    private void setupGrid(View view) {
        gridView = (GridView) view.findViewById(R.id.grid_media);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setOnItemClick(parent, view, position, id);
            }
        });
        if (mGridAdapter == null)
            mGridAdapter = createMediaGridAdapter();
        gridView.setAdapter(mGridAdapter);

        if (mGridAdapter.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            txtProgress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, String.format("Calling %s Fragment initLoader with id %s!", mediaType.type(), getLoaderId()));
        getLoaderManager().initLoader(getLoaderId(), savedInstanceState, this);
    }

    @Override
    public Loader<List<T>> onCreateLoader(int id, Bundle args) {
        if (mDataLoader == null) {
            mDataLoader = new BaseDataLoader<>(createRepository(), getContext());
        }
        return mDataLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<T>> loader, List<T> data) {
        Log.d(TAG, String.format("onLoadFinished() called! Loading %s data!", mediaType.type()));
        mListAdapter.reset();
        mListAdapter.setGroups(data);
        mListAdapter.notifyDataSetChanged();

        // TODO Only load the adapter that is currently available (so Grid for Album, List for others)
        mGridAdapter.reset();
        mGridAdapter.setGroups(data);
        mGridAdapter.notifyDataSetChanged();

        if (progressBar != null)
            progressBar.setVisibility(View.GONE);
        if (txtProgress != null)
            txtProgress.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<T>> loader) {
        loader.reset();
    }

}
