package com.mcnedward.bramble.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.adapter.grid.MediaGridAdapter;
import com.mcnedward.bramble.adapter.list.BaseMediaAdapter;
import com.mcnedward.bramble.adapter.list.MediaListAdapter;
import com.mcnedward.bramble.entity.media.Media;
import com.mcnedward.bramble.entity.media.MediaType;
import com.mcnedward.bramble.loader.DataLoader;
import com.mcnedward.bramble.repository.media.IMediaRepository;

import java.util.List;

/**
 * Created by edward on 26/12/15.
 */
public abstract class MediaFragment<T extends Media> extends Fragment implements LoaderManager.LoaderCallbacks<List<T>> {
    private final static String TAG = "MediaFragment";

    private MediaType mMediaType;

    protected ListView mListView;
    protected GridView mGridView;

    protected DataLoader<T> mDataLoader;
    protected BaseMediaAdapter<T> mListAdapter;
    protected MediaGridAdapter<T> mGridAdapter;

    protected ProgressBar mProgressBar;
    private TextView mTxtProgress;

    public MediaFragment(MediaType mediaType) {
        this.mMediaType = mediaType;
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

    protected abstract IMediaRepository<T> createRepository();

    protected abstract MediaListAdapter<T> createMediaListAdapter();

    protected abstract MediaGridAdapter<T> createMediaGridAdapter();

    protected abstract int getLoaderId();

    public void toggleMediaView(boolean grid) {
        if (grid) {
            mListView.setVisibility(View.GONE);
            mGridView.setVisibility(View.VISIBLE);
        } else {
            mListView.setVisibility(View.VISIBLE);
            mGridView.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View thisView = inflater.inflate(R.layout.fragment_media, container, false);

        mProgressBar = (ProgressBar) thisView.findViewById(R.id.media_progress_bar);
        mTxtProgress = (TextView) thisView.findViewById(R.id.media_progress_text);

        setupList(thisView);
        setupGrid(thisView);

        switch (mMediaType) {
            case ARTIST:
                mTxtProgress.setText(getContext().getString(R.string.artist_loading_text));
                toggleMediaView(true);
                break;
            case ALBUM:
                mTxtProgress.setText(getContext().getString(R.string.album_loading_text));
                toggleMediaView(true);
                break;
            case SONG:
                mTxtProgress.setText(getContext().getString(R.string.song_loading_text));
                toggleMediaView(false);
                break;
        }

        return thisView;
    }

    private void setupList(View view) {
        mListView = (ListView) view.findViewById(R.id.list_media);
        mListView.setItemsCanFocus(true);
        if (mListAdapter == null)
            mListAdapter = createMediaListAdapter();
        mListView.setAdapter(mListAdapter);
        mListView.setOnScrollChangeListener(getScrollChangeListener());

        if (mListAdapter.isEmpty()) {
            mProgressBar.setVisibility(View.VISIBLE);
            mTxtProgress.setVisibility(View.VISIBLE);
        }
    }

    private void setupGrid(View view) {
        mGridView = (GridView) view.findViewById(R.id.grid_media);
        if (mGridAdapter == null)
            mGridAdapter = createMediaGridAdapter();
        mGridView.setAdapter(mGridAdapter);
        mGridView.setOnScrollChangeListener(getScrollChangeListener());

        if (mGridAdapter.isEmpty()) {
            mProgressBar.setVisibility(View.VISIBLE);
            mTxtProgress.setVisibility(View.VISIBLE);
        }
    }

    private View.OnScrollChangeListener getScrollChangeListener() {
        return new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.d(TAG, "SCROLLING");
            }
        };
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, String.format("Calling %s Fragment initLoader with mId %s!", mMediaType.type(), getLoaderId()));
        getLoaderManager().initLoader(getLoaderId(), savedInstanceState, this);
    }

    @Override
    public Loader<List<T>> onCreateLoader(int id, Bundle args) {
        if (mDataLoader == null) {
            mDataLoader = new DataLoader<>(createRepository(), getContext());
        }
        return mDataLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<T>> loader, List<T> data) {
        Log.d(TAG, String.format("onLoadFinished() called! Loading %s data!", mMediaType.type()));
        mListAdapter.reset();
        mListAdapter.setGroups(data);
        mListAdapter.notifyDataSetChanged();

        // TODO Only load the adapter that is currently available (so Grid for Album, List for others)
        mGridAdapter.reset();
        mGridAdapter.setGroups(data);
        mGridAdapter.notifyDataSetChanged();

        if (mProgressBar != null)
            mProgressBar.setVisibility(View.GONE);
        if (mTxtProgress != null)
            mTxtProgress.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<T>> loader) {
        loader.reset();
    }

}
