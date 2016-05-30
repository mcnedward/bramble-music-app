package com.mcnedward.bramble.repository.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.mcnedward.bramble.entity.data.ArtistImage;
import com.mcnedward.bramble.entity.data.Thumbnail;
import com.mcnedward.bramble.exception.EntityAlreadyExistsException;
import com.mcnedward.bramble.exception.EntityDoesNotExistException;
import com.mcnedward.bramble.provider.ArtistImageContentProvider;

import java.util.List;

/**
 * Created by Edward on 5/28/2016.
 */
public class ArtistImageRepository extends DataRepository<ArtistImage> implements IArtistImageRepository {
    private static final String TAG = ArtistImageRepository.class.getName();

    private IThumbnailRepository mThumbnailRepository;

    public ArtistImageRepository(Context context) {
        super(context);
        mThumbnailRepository = new ThumbnailRepository(context);
    }

    @Override
    public List<ArtistImage> getForArtistId(long artistId) {
        return read(String.format("%s=?", DatabaseHelper.A_ARTIST_ID), new String[]{String.valueOf(artistId)});
    }

    @Override
    public ArtistImage getSelectedImageForArtist(long artistId) throws  EntityDoesNotExistException {
        return readFirstOrDefault(String.format("%s = ? AND %s = 1", DatabaseHelper.A_ARTIST_ID, DatabaseHelper.A_SELECTED_IMAGE), new String[]{String.valueOf(artistId)});
    }

    @Override
    public boolean setSelectedImage(ArtistImage newSelectedArtistImage, long artistId) {
        boolean updated = false;
        try {
            ArtistImage oldSelectedArtistImage = getSelectedImageForArtist(artistId);
            oldSelectedArtistImage.setSelectedImage(false);
            updated = update(oldSelectedArtistImage);
        } catch (EntityDoesNotExistException e) {
            // No default ArtistImage found, so just continue
        }
        try {
            updated = update(newSelectedArtistImage);
        } catch (EntityDoesNotExistException e) {
            Log.w(TAG, "Could not update the ArtistImage " + newSelectedArtistImage.getTitle() + " because it does not exist.");
        }
        return updated;
    }

    @Override
    public ArtistImage save(ArtistImage entity) throws EntityAlreadyExistsException {
        entity = super.save(entity);
        Thumbnail thumbnail = entity.getThumbnail();
        if (thumbnail != null) {
            thumbnail.setArtistImageId(entity.getId());
            thumbnail = mThumbnailRepository.save(thumbnail);
            entity.setThumbnail(thumbnail);
            try {
                update(entity);
            } catch (EntityDoesNotExistException e) {
                Log.w(TAG, e.getMessage());
            }
        }
        return entity;
    }

    @Override
    protected ArtistImage generateEntityFromCursor(Cursor cursor) {
        ArtistImage ai = new ArtistImage();
        ai.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.ID)));
        ai.setArtistId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.A_ARTIST_ID)));
        ai.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.A_TITLE)));
        ai.setMediaUrl(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.A_MEDIA_URL)));
        ai.setSourceUrl(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.A_SOURCE_URL)));
        ai.setDisplayUrl(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.A_DISPLAY_URL)));
        ai.setWidth(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.A_WIDTH)));
        ai.setHeight(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.A_HEIGHT)));
        ai.setFileSize(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.A_FILE_SIZE)));
        ai.setContentType(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.A_CONTENT_TYPE)));
        ai.setBitmapPath(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.A_BITMAP_PATH)));
        ai.setSelectedImage(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.A_SELECTED_IMAGE)) == 1);

        int thumbnailId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.A_THUMBNAIL_ID));
        try {
            Thumbnail thumbnail = mThumbnailRepository.get(thumbnailId);
            thumbnail.setTitle(ai.getTitle());
            ai.setThumbnail(thumbnail);
        } catch (EntityDoesNotExistException e) {
            Log.w(TAG, e.getMessage());
        }

        return ai;
    }

    @Override
    protected ContentValues generateContentValuesFromEntity(ArtistImage entity) {
        ContentValues values = generateContentValuesFromEntityWithNoId(entity);
        values.put(DatabaseHelper.ID, entity.getId());
        return values;
    }

    @Override
    protected ContentValues generateContentValuesFromEntityWithNoId(ArtistImage entity) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.A_ARTIST_ID, entity.getArtistId());
        values.put(DatabaseHelper.A_TITLE, entity.getTitle());
        values.put(DatabaseHelper.A_MEDIA_URL, entity.getMediaUrl());
        values.put(DatabaseHelper.A_SOURCE_URL, entity.getSourceUrl());
        values.put(DatabaseHelper.A_DISPLAY_URL, entity.getDisplayUrl());
        values.put(DatabaseHelper.A_WIDTH, entity.getWidth());
        values.put(DatabaseHelper.A_HEIGHT, entity.getHeight());
        values.put(DatabaseHelper.A_FILE_SIZE, entity.getFileSize());
        values.put(DatabaseHelper.A_BITMAP_PATH, entity.getBitmapPath());
        values.put(DatabaseHelper.A_CONTENT_TYPE, entity.getContentType());
        values.put(DatabaseHelper.A_SELECTED_IMAGE, entity.isSelectedImage() ? 1 : 0);
        values.put(DatabaseHelper.A_THUMBNAIL_ID, entity.getThumbnail() != null ? entity.getThumbnail().getId() : 0);
        return values;
    }

    @Override
    protected String[] getColumns() {
        return new String[]{
                DatabaseHelper.ID,
                DatabaseHelper.A_ARTIST_ID,
                DatabaseHelper.A_TITLE,
                DatabaseHelper.A_MEDIA_URL,
                DatabaseHelper.A_SOURCE_URL,
                DatabaseHelper.A_DISPLAY_URL,
                DatabaseHelper.A_WIDTH,
                DatabaseHelper.A_HEIGHT,
                DatabaseHelper.A_FILE_SIZE,
                DatabaseHelper.A_CONTENT_TYPE,
                DatabaseHelper.A_BITMAP_PATH,
                DatabaseHelper.A_SELECTED_IMAGE,
                DatabaseHelper.A_THUMBNAIL_ID
        };
    }

    @Override
    protected String getOrderBy() {
        return DatabaseHelper.ID + " asc";
    }

    @Override
    protected Uri getUri() {
        return ArtistImageContentProvider.CONTENT_URI;
    }
}
