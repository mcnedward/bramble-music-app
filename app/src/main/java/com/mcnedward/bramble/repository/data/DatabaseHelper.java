package com.mcnedward.bramble.repository.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Edward on 5/26/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    private static DatabaseHelper sInstance;

    // Database title
    public static String DB_NAME = "Bramble.db";
    // Database version - increment this number to upgrade the database
    public static final int DB_VERSION = 16;

    // Tables
    public static final String PLAYLIST_TABLE = "Playlist";
    public static final String CURRENT_TABLE = "Current";
    public static final String ARTIST_IMAGE_TABLE = "ArtistImage";
    public static final String THUMBNAIL_TABLE = "Thumbnail";
    // Id column, which should be the same across all tables
    public static final String ID = "Id";
    // Playlist table
    public static final String P_SONG_KEYS = "SongIds";
    // Current table
    public static final String C_SONG_ID = "SongId";
    public static final String C_ALBUM_ID = "AlbumId";
    // ArtistImage table
    // TODO Probably don't need this, and can just use the Thumbnails
    public static final String A_ARTIST_ID = "ArtistId";
    public static final String A_TITLE = "Title";
    public static final String A_MEDIA_URL = "MediaUrl";
    public static final String A_SOURCE_URL = "SourceUrl";
    public static final String A_DISPLAY_URL = "DisplayUrl";
    public static final String A_WIDTH = "Width";
    public static final String A_HEIGHT = "Height";
    public static final String A_FILE_SIZE = "FileSize";
    public static final String A_CONTENT_TYPE = "ContentType";
    public static final String A_BITMAP_PATH = "BitmapPath";
    public static final String A_THUMBNAIL_ID = "ThumbnailId";
    // Thumbnail table
    public static final String T_ARTIST_IMAGE_ID = "ArtistImageId";
    public static final String T_MEDIA_URL = "MediaUrl";
    public static final String T_CONTENT_TYPE = "ContentType";
    public static final String T_WIDTH = "Width";
    public static final String T_HEIGHT = "Height";
    public static final String T_FILE_SIZE = "FileSize";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context);
        }
        return sInstance;
    }

    /*****
     * CREATE STATEMENTS
     */
    private static final String createPlaylistTable = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "%s TEXT)",
            PLAYLIST_TABLE, ID, P_SONG_KEYS);
    private static final String createCurrentTable = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            "%s INTEGER, %s INTEGER)", CURRENT_TABLE, ID, C_SONG_ID, C_ALBUM_ID);
    private static final String createArtistImageTable = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT NOT " +
                    "NULL, " +
                    "%s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s INTEGER, FOREIGN KEY" +
                    "(%s) REFERENCES %s(%s))",
            ARTIST_IMAGE_TABLE, ID, A_ARTIST_ID, A_TITLE, A_MEDIA_URL, A_SOURCE_URL, A_DISPLAY_URL, A_WIDTH, A_HEIGHT, A_FILE_SIZE, A_CONTENT_TYPE,
            A_BITMAP_PATH,
            A_THUMBNAIL_ID, A_THUMBNAIL_ID, THUMBNAIL_TABLE, ID);
    private static final String createThumbnailTable = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " " +
                    "%s TEXT, %s TEXT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, FOREIGN KEY(%s) REFERENCES %s(%s))",
            THUMBNAIL_TABLE, ID, T_MEDIA_URL, T_CONTENT_TYPE, A_WIDTH, A_HEIGHT, A_FILE_SIZE,
            T_ARTIST_IMAGE_ID, T_ARTIST_IMAGE_ID, ARTIST_IMAGE_TABLE, ID);

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createPlaylistTable);
        sqLiteDatabase.execSQL(createCurrentTable);
        sqLiteDatabase.execSQL(createArtistImageTable);
        sqLiteDatabase.execSQL(createThumbnailTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i(TAG, "Update database tables...");
        dropTables(sqLiteDatabase);
        onCreate(sqLiteDatabase);
    }

    /**
     * Drops all tables, then recreates them.
     *
     * @param database
     */
    private static void resetData(SQLiteDatabase database) {
        database.execSQL(DROP_PLAYLIST_TABLE);
        database.execSQL(DROP_CURRENT_TABLE);
        database.execSQL(DROP_ARTIST_IMAGE_TABLE);
        database.execSQL(DROP_THUMBNAIL_TABLE);

        database.execSQL(createPlaylistTable);
        database.execSQL(createCurrentTable);
        database.execSQL(createArtistImageTable);
        database.execSQL(createThumbnailTable);
    }

    /*****
     * DROP Statements
     *****/
    private static final String DROP_PLAYLIST_TABLE = "DROP TABLE IF EXISTS " + PLAYLIST_TABLE;
    private static final String DROP_CURRENT_TABLE = "DROP TABLE IF EXISTS " + CURRENT_TABLE;
    private static final String DROP_ARTIST_IMAGE_TABLE = "DROP TABLE IF EXISTS " + ARTIST_IMAGE_TABLE;
    private static final String DROP_THUMBNAIL_TABLE = "DROP TABLE IF EXISTS " + THUMBNAIL_TABLE;

    public void dropTables(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DROP_PLAYLIST_TABLE);
        sqLiteDatabase.execSQL(DROP_CURRENT_TABLE);
        sqLiteDatabase.execSQL(DROP_ARTIST_IMAGE_TABLE);
        sqLiteDatabase.execSQL(DROP_ARTIST_IMAGE_TABLE);
    }
}
