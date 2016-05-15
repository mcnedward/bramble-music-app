package com.mcnedward.bramble.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.ProgressBar;

import com.mcnedward.bramble.activity.MainActivity;
import com.mcnedward.bramble.media.MediaType;
import com.mcnedward.bramble.utils.MediaCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward on 23/12/15.
 */
public abstract class BaseDataLoader<T> extends AsyncTaskLoader<List<T>> {
    private final static String TAG = "BaseDataLoader";

    protected Context context;
    private MediaType mediaType;

    private List<T> mDataList = null;

    public BaseDataLoader(MediaType mediaType, Context context) {
        super(context);
        this.mediaType = mediaType;
        this.context = context;
    }

    protected abstract Uri getMediaUri();

    protected abstract String[] getMediaColumns();

    protected abstract String getMediaSelection();

    protected abstract String[] getMediaSelectionArgs();

    protected abstract String getMediaSrtOrder();

    protected abstract List<T> handleMediaCursor(Cursor cursor);

    // TODO I really don't like this...
    protected abstract void addToMediaService(List<T> mediaList);

    @Override
    public List<T> loadInBackground() {
        MediaCache.setLoading(mediaType, true);
        List<T> mediaList = null;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(getMediaUri(), getMediaColumns(), getMediaSelection(), getMediaSelectionArgs(), getMediaSrtOrder());
            while (cursor.moveToNext()) {
                mediaList = handleMediaCursor(cursor);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            Log.d(TAG, String.format("Done with %s media!", mediaType.type()));
        }
        return mediaList;
    }

    /**
     * Runs on the UI thread, routing the results from the background thread to whatever is using the dataList.
     *
     * @param dataList The list of data.
     */
    @Override
    public void deliverResult(List<T> dataList) {
        Log.d(TAG, String.format("Delivering %s results!", mediaType.type()));
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
        addToMediaService(dataList);
        MediaCache.setLoading(mediaType, false);
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
        MediaCache.setLoading(mediaType, false);
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
        MediaCache.setLoading(mediaType, false);
    }

    /**
     * Must be called from the UI thread, triggered by a call to reset(). Here,
     * we make sure our Cursor is closed, if it still exists and is not already closed.
     */
    @Override
    public void onReset() {
        super.onReset();
        Log.d(TAG, String.format("onReset called for %s loader...", mediaType.type()));
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