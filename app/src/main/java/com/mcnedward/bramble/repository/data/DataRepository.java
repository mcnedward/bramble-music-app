package com.mcnedward.bramble.repository.data;

import android.content.Context;
import android.util.Log;

import com.mcnedward.bramble.exception.EntityAlreadyExistsException;
import com.mcnedward.bramble.exception.EntityDoesNotExistException;
import com.mcnedward.bramble.media.Data;
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

    public T save(T entity) throws EntityAlreadyExistsException {
        mContext.getContentResolver().insert(getUri(), generateContentValuesFromEntity(entity));
        // TODO Get the id from the uri returned from insert and add it to entity
        return entity;
    }

    public boolean update(T entity) throws EntityDoesNotExistException {
        int result = mContext.getContentResolver().update(getUri(), generateContentValuesFromEntity(entity), WHERE_ID_CLAUSE, new String[]{String.valueOf(entity
                .getId())});
        return result != -1;
    }

    public boolean delete(T entity) throws EntityDoesNotExistException {
        return delete(entity.getId());
    }

    public boolean delete(long id) throws EntityDoesNotExistException {
        int result = mContext.getContentResolver().delete(getUri(), WHERE_ID_CLAUSE, new String[] { String.valueOf(id) });
        return result != -1;
    }

}
