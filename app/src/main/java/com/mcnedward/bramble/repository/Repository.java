package com.mcnedward.bramble.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.mcnedward.bramble.exception.EntityDoesNotExistException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 5/16/2016.
 */
public abstract class Repository<T> implements IRepository<T> {
    private static final String TAG = "Repository";

    protected Context mContext;

    public Repository(Context context) {
        mContext = context;
    }

    public Repository() {}

    @Override
    public T get(long id) throws EntityDoesNotExistException {
        String whereClause = String.format("%s = ?", getColumns()[0]);
        return readFirstOrDefault(whereClause, new String[]{String.valueOf(id)});
    }

    @Override
    public T readFirstOrDefault(String whereClause, String[] whereArgs) throws EntityDoesNotExistException {
        return readFirstOrDefault(whereClause, whereArgs, getOrderBy());
    }

    @Override
    public T readFirstOrDefault(String whereClause, String[] whereArgs, String orderBy) throws EntityDoesNotExistException {
        T entity = null;
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(
                    getUri(),
                    getColumns(),
                    whereClause,
                    whereArgs,
                    orderBy);
            if (cursor.moveToFirst()) {
                entity = generateEntityFromCursor(cursor);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        if (entity == null) {
            throw new EntityDoesNotExistException("Entity does not exist for where clause: " + whereClause + "[" + whereArgs + "]");
        }
        return entity;
    }

    @Override
    public List<T> read(String whereClause, String... whereArgs) {
        return read(whereClause, whereArgs, getOrderBy());
    }

    @Override
    public List<T> read(String whereClause, String[] whereArgs, String orderBy) {
        return read(getUri(), getColumns(), whereClause, whereArgs, orderBy);
    }

    protected List<T> read(Uri mediaUri, String[] columns, String whereClause, String[] whereArgs, String orderBy) {
        List<T> mediaList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(mediaUri, columns, whereClause, whereArgs, orderBy);
            while (cursor.moveToNext()) {
                T media = generateEntityFromCursor(cursor);
                if (media != null)
                    mediaList.add(media);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return mediaList;
    }

    @Override
    public List<T> getAll() {
        return read(null);
    }

    protected abstract T generateEntityFromCursor(Cursor cursor);

    protected abstract ContentValues generateContentValuesFromEntity(T entity);

    /**
     * Get all of the columns for the repository.
     * The first item should ALWAYS be the mId, so that it can be found at the first index.
     * If this is a MediaRepository, the second item should ALWAYS be the key, so that it can be found at the second index.
     * @return The columns.
     */
    protected abstract String[] getColumns();

    protected abstract String getOrderBy();

    protected abstract Uri getUri();

}
