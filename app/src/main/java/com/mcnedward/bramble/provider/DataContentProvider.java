package com.mcnedward.bramble.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.mcnedward.bramble.repository.data.DatabaseHelper;

/**
 * Created by Edward on 5/28/2016.
 *
 * Help from: http://www.vogella.com/tutorials/AndroidSQLite/article.html#sqliteoverview
 */
public abstract class DataContentProvider extends ContentProvider {

    private DatabaseHelper mHelper;

    // Used for the UriMatcher
    protected static final int ENTITY = 10;
    protected static final int ENTITY_ID = 20;

    protected static final String BASE_AUTHORITY = "com.mcnedward.bramble.provider.";
    protected static final String BASE_CONTENT_AUTHORITY = "content://" + BASE_AUTHORITY;

    protected static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    @Override
    public boolean onCreate() {
        mHelper = new DatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Using SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(getTableName());

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case ENTITY:
                break;
            case ENTITY_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(DatabaseHelper.ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase database = open();
        Cursor cursor = queryBuilder.query(database, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase database = open();
        long id;
        database.beginTransaction();
        switch (uriType) {
            case ENTITY:
                id = database.insert(getTableName(), null, values);
                database.setTransactionSuccessful();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        database.endTransaction();
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(getBasePath() + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase database = open();
        int rowsDeleted;
        database.beginTransaction();
        switch (uriType) {
            case ENTITY:
                rowsDeleted = database.delete(getTableName(), selection,
                        selectionArgs);
                database.setTransactionSuccessful();
                break;
            case ENTITY_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = database.delete(getTableName(),
                            DatabaseHelper.ID + "=" + id, null);
                } else {
                    rowsDeleted = database.delete(getTableName(),
                            DatabaseHelper.ID + "=" + id + " and " + selection,
                            selectionArgs);
                }
                database.setTransactionSuccessful();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        database.endTransaction();
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase database = open();
        int rowsUpdated;
        database.beginTransaction();
        switch (uriType) {
            case ENTITY:
                rowsUpdated = database.update(getTableName(),
                        values,
                        selection,
                        selectionArgs);
                database.setTransactionSuccessful();
                break;
            case ENTITY_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = database.update(getTableName(),
                            values,
                            DatabaseHelper.ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = database.update(getTableName(),
                            values,
                            DatabaseHelper.ID + "=" + id + " and " + selection,
                            selectionArgs);
                }
                database.setTransactionSuccessful();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        database.endTransaction();
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }
    private SQLiteDatabase open() throws android.database.SQLException {
        return mHelper.getWritableDatabase();
    }

    protected abstract void checkColumns(String[] projection);

    protected abstract String getTableName();

    protected abstract String getBasePath();
}
