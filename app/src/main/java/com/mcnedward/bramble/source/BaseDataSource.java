package com.mcnedward.bramble.source;

import android.database.Cursor;

import java.util.List;

/**
 * Created by edward on 23/12/15.
 */
public class BaseDataSource implements IDataSource {
    @Override
    public boolean insert(Object entity) {
        return false;
    }

    @Override
    public boolean update(Object entity) {
        return false;
    }

    @Override
    public boolean delete(Object entity) {
        return false;
    }

    @Override
    public boolean save(Object entity) {
        return false;
    }

    @Override
    public List read(String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return null;
    }

    @Override
    public Cursor rawQuery(String sql, String[] selectionArgs) {
        return null;
    }
}
