package com.mcnedward.bramble.repository.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.mcnedward.bramble.entity.data.Thumbnail;
import com.mcnedward.bramble.provider.ThumbnailContentProvider;

/**
 * Created by Edward on 5/28/2016.
 */
public class ThumbnailRepository extends DataRepository<Thumbnail> implements IThumbnailRepository {

    public ThumbnailRepository(Context context) {
        super(context);
    }

    @Override
    protected Thumbnail generateEntityFromCursor(Cursor cursor) {
        Thumbnail thumbnail = new Thumbnail();
        thumbnail.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.ID)));
        thumbnail.setArtistImageId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.T_ARTIST_IMAGE_ID)));
        thumbnail.setContentType(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.T_CONTENT_TYPE)));
        thumbnail.setMediaUrl(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.T_MEDIA_URL)));
        thumbnail.setWidth(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.T_WIDTH)));
        thumbnail.setHeight(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.T_HEIGHT)));
        thumbnail.setFileSize(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.T_FILE_SIZE)));
        return thumbnail;
    }

    @Override
    protected ContentValues generateContentValuesFromEntity(Thumbnail entity) {
        ContentValues values = generateContentValuesFromEntityWithNoId(entity);
        values.put(DatabaseHelper.ID, entity.getId());
        return values;
    }

    @Override
    protected ContentValues generateContentValuesFromEntityWithNoId(Thumbnail entity) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.T_ARTIST_IMAGE_ID, entity.getArtistImageId());
        values.put(DatabaseHelper.T_MEDIA_URL, entity.getMediaUrl());
        values.put(DatabaseHelper.T_WIDTH, entity.getWidth());
        values.put(DatabaseHelper.T_HEIGHT, entity.getHeight());
        values.put(DatabaseHelper.T_FILE_SIZE, entity.getFileSize());
        values.put(DatabaseHelper.T_CONTENT_TYPE, entity.getContentType());
        return values;
    }

    @Override
    protected String[] getColumns() {
        return new String[] {
                DatabaseHelper.ID,
                DatabaseHelper.T_ARTIST_IMAGE_ID,
                DatabaseHelper.T_MEDIA_URL,
                DatabaseHelper.T_WIDTH,
                DatabaseHelper.T_HEIGHT,
                DatabaseHelper.T_FILE_SIZE,
                DatabaseHelper.T_CONTENT_TYPE,
        };
    }

    @Override
    protected String getOrderBy() {
        return DatabaseHelper.ID + " asc";
    }

    @Override
    protected Uri getUri() {
        return ThumbnailContentProvider.CONTENT_URI;
    }
}
