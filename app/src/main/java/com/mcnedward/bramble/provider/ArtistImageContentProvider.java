package com.mcnedward.bramble.provider;

import android.content.ContentResolver;
import android.net.Uri;

import com.mcnedward.bramble.repository.data.DatabaseHelper;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by Edward on 5/28/2016.
 *
 * ContentProvider for accessing entities from the Playlist database table.
 */
public class ArtistImageContentProvider extends DataContentProvider {

    public static final String AUTHORITY = "artist_image";
    public static final String PATH = "/artist_image";
    public static Uri CONTENT_URI = Uri.parse(BASE_CONTENT_AUTHORITY + AUTHORITY + PATH);
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + PATH;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + PATH;

    static {
        sURIMatcher.addURI(BASE_AUTHORITY + AUTHORITY, PATH, ENTITY);
        sURIMatcher.addURI(BASE_AUTHORITY + AUTHORITY, PATH + "/#", ENTITY_ID);
    }

    @Override
    protected void checkColumns(String[] projection) {
        String[] available = { DatabaseHelper.ID,
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
                DatabaseHelper.A_THUMBNAIL_ID };
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<>(Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }

    @Override
    protected String getTableName() {
        return DatabaseHelper.ARTIST_IMAGE_TABLE;
    }

    @Override
    protected String getBasePath() { return PATH; }
}
