package com.mcnedward.bramble.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.mcnedward.bramble.media.Media;
import com.mcnedward.bramble.media.MediaType;
import com.mcnedward.bramble.repository.IRepository;
import com.mcnedward.bramble.repository.media.IMediaRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward on 23/12/15.
 */
public class BaseDataLoader<T extends Media> extends AsyncTaskLoader<List<T>> {
    private final static String TAG = "BaseDataLoader";

    private IMediaRepository<T> mRepository;
    private MediaType mMediaType;

    private List<T> mDataList = null;

    public BaseDataLoader(IMediaRepository repository, Context context) {
        super(context);
        mRepository = repository;
        mMediaType = mRepository.getMediaType();
    }

    @Override
    public List<T> loadInBackground() {
        return mRepository.getAll();
    }

    /**
     * Runs on the UI thread, routing the results from the background thread to whatever is using the dataList.
     *
     * @param dataList The list of data.
     */
    @Override
    public void deliverResult(List<T> dataList) {
        Log.d(TAG, String.format("Delivering %s results!", mMediaType.type()));
        if (isReset()) {
            resetDataList(dataList);
            return;
        }
        List<T> oldDataList = mDataList;
        mDataList = dataList;
        if (isStarted()) {
            super.deliverResult(dataList);
        }
        if (oldDataList != null && oldDataList != dataList && oldDataList.size() > 0) {
            resetDataList(mDataList);
        }
    }

    /**
     * Starts an asynchronous load of the list data. When the result is ready
     * the callbacks will be called on the UI thread. If a previous load has
     * been completed and is still valid, the result may be passed to the
     * callbacks immediately. Must be called from the UI thread.
     */
    @Override
    public void onStartLoading() {
        if (mDataList != null) {
            deliverResult(mDataList);
        }
        if (takeContentChanged() || mDataList == null || mDataList.size() == 0) {
            forceLoad();
        }
    }

    /**
     * Must be called from the UI thread, triggered by a call to stopLoading().
     */
    @Override
    public void onStopLoading() {
        cancelLoad();
    }

    /**
     * Must be called from the UI thread, triggered by a call to cancel(). Here,
     * we make sure our Cursor is closed, if it still exists and is not already
     * closed.
     */
    @Override
    public void onCanceled(List<T> dataList) {
        // TODO Change this
        if (dataList != null & dataList.size() > 0)
            resetDataList(dataList);
    }

    /**
     * Must be called from the UI thread, triggered by a call to reset(). Here,
     * we make sure our Cursor is closed, if it still exists and is not already closed.
     */
    @Override
    public void onReset() {
        super.onReset();
        Log.d(TAG, String.format("onReset called for %s loader...", mMediaType.type()));
        // Ensure the loader is stopped
        onStopLoading();
        if (mDataList != null && mDataList.size() > 0) {
            resetDataList(mDataList);
        }
        mDataList = null;
    }

    private void resetDataList(List<T> dataList) {
        dataList = new ArrayList<>();
    }
}