package com.mcnedward.bramble.utils.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward on 23/12/15.
 */
public abstract class BaseDataLoader<T> extends AsyncTaskLoader<List<T>> {

    private List<T> mDataList = null;
    public BaseDataLoader(Context context) {
        super(context);
    }

    public abstract List<T> buildList();

    public void insert(T entity) {
//        new InsertTask<T>(this, entity, mDataSource).execute();
    }

    public void update(T entity) {
//        new UpdateTask<T>(this, entity, mDataSource).execute();
    }

    public void delete(T entity) {
//        new DeleteTask<T>(this, entity, mDataSource).execute();
    }

    @Override
    public List<T> loadInBackground() {
        return buildList();
    }

    /**
     * Runs on the UI thread, routing the results from the background thread to whatever is using the dataList.
     *
     * @param dataList The list of data.
     */
    @Override
    public void deliverResult(List<T> dataList) {
        if (isReset()) {
            dataList = new ArrayList<>();
            return;
        }
        List<T> oldDataList = mDataList;
        mDataList = dataList;
        if (isStarted()) {
            super.deliverResult(dataList);
        }
        if (oldDataList != null && oldDataList != dataList && oldDataList.size() > 0) {
            mDataList = new ArrayList<>();
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
            dataList = new ArrayList<>();
    }

    /**
     * Must be called from the UI thread, triggered by a call to reset(). Here,
     * we make sure our Cursor is closed, if it still exists and is not already
     * closed.
     */
    @Override
    public void onReset() {
        super.onReset();
        // Ensure the loader is stopped
        onStopLoading();
        if (mDataList != null && mDataList.size() > 0) {
            mDataList = new ArrayList<>();
        }
        mDataList = null;
    }
}