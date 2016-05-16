package com.mcnedward.bramble.repository;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.mcnedward.bramble.media.Media;
import com.mcnedward.bramble.media.MediaType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 5/16/2016.
 */
public abstract class BaseRepository<T extends Media> implements IRepository<T> {
    private static final String TAG = "BaseRepository";

    protected MediaType mMediaType;
    protected Context mContext;

    public BaseRepository(MediaType mediaType, Context context) {
        mMediaType = mediaType;
        mContext = context;
    }

    @Override
    public List<T> query(String query, String... params) {
        return query(getMediaUri(), getColumns(), query, params, getSortOrder());
    }

    @Override
    public List<T> query(Uri mediaUri, String[] columns, String query, String[] params, String sortOrder) {
        List<T> mediaList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(mediaUri, columns, query, params, sortOrder);
            while (cursor.moveToNext()) {
                T media = createMedia(cursor);
                if (media != null)
                    mediaList.add(media);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            Log.d(TAG, String.format("Done with %s media!", mMediaType.type()));
        }
        return mediaList;
    }

    @Override
    public List<T> getAll() {
        return query(null);
    }

    @Override
    public T get(int id) {
        String selection = String.format("%s = ?", getColumns()[0]);
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(
                    getMediaUri(),
                    getColumns(),
                    selection,
                    new String[] { String.valueOf(id)},
                    getSortOrder());
            if (cursor.moveToFirst()) {
                return createMedia(cursor);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return null;
    }

    @Override
    public MediaType getMediaType() {
        return mMediaType;
    }

}
