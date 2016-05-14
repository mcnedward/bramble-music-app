package com.mcnedward.bramble.source;

import android.database.Cursor;

import java.util.List;

/**
 * Created by edward on 23/12/15.
 */
public interface IDataSource<T> {

    boolean insert(T entity);

    boolean update(T entity);

    boolean delete(T entity);

    boolean save(T entity);

    List<T> read(String selection, String[] selectionArgs, String groupBy, String having, String orderBy);

    Cursor rawQuery(String sql, String[] selectionArgs);

}
