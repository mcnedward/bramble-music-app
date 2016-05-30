package com.mcnedward.bramble.repository.data;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.mcnedward.bramble.exception.EntityAlreadyExistsException;
import com.mcnedward.bramble.exception.EntityDoesNotExistException;
import com.mcnedward.bramble.entity.data.Data;
import com.mcnedward.bramble.repository.Repository;

/**
 * Created by Edward on 5/28/2016.
 */
public abstract class DataRepository<T extends Data> extends Repository<T> implements IDataRepository<T> {
    private static final String TAG = "DataRepository";
    private static final String WHERE_ID_CLAUSE = "Id = ?";

    public DataRepository(Context context) {
        super(context);
    }

    @Override
    public T save(T entity) throws EntityAlreadyExistsException {
        Uri entityUri = mContext.getContentResolver().insert(getUri(), generateContentValuesFromEntityWithNoId(entity));
        if (entityUri != null) {
            String lastSegment = entityUri.getLastPathSegment();
            try {
                int id = Integer.parseInt(lastSegment);
                entity.setId(id);
            } catch (NumberFormatException e) {
                Log.w(TAG, "Could not parse int on: " + lastSegment);
            }
        }
        return entity;
    }

    @Override
    public boolean update(T entity) throws EntityDoesNotExistException {
        int result = mContext.getContentResolver().update(getUri(), generateContentValuesFromEntity(entity), WHERE_ID_CLAUSE, new String[]{String
                .valueOf(entity.getId())});
        return result != -1;
    }

    @Override
    public boolean update(long id) throws EntityDoesNotExistException {
        T entity = get(id);
        return update(entity);
    }

    @Override
    public boolean delete(T entity) throws EntityDoesNotExistException {
        return delete(entity.getId());
    }

    @Override
    public boolean delete(long id) throws EntityDoesNotExistException {
        int result = mContext.getContentResolver().delete(getUri(), WHERE_ID_CLAUSE, new String[]{String.valueOf(id)});
        return result != -1;
    }

    /**
     * To be used for inserting, since the id should be auto-incremented.
     *
     * @param entity The entity to generate from.
     * @return
     */
    protected abstract ContentValues generateContentValuesFromEntityWithNoId(T entity);

}
