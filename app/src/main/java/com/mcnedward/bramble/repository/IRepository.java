package com.mcnedward.bramble.repository;

import android.database.Cursor;
import android.net.Uri;

import com.mcnedward.bramble.media.Media;
import com.mcnedward.bramble.media.MediaType;

import java.util.List;

/**
 * Created by Edward on 5/16/2016.
 */
public interface IRepository<T extends Media> {

    T createMedia(Cursor cursor);

    Uri getMediaUri();

    String getSortOrder();

    /**
     * Get all of the columns for the repository.
     * The first item should ALWAYS be the id, so that it can be found at the first index.
     * @return The columns.
     */
    String[] getColumns();

    List<T> query(String query, String... params);

    List<T> query(Uri mediaUri, String[] columns, String query, String[] params, String sortOrder);

    List<T> getAll();

    T get(int id);

    MediaType getMediaType();

}
