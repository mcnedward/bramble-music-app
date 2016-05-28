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
    public static final int DB_VERSION = 1;

    // Tables
    public static final String PLAYLIST_TABLE = "Playlist";
    public static final String CURRENT_TABLE = "Current";
    // Id column, which should be the same across all tables
    public static final String ID = "Id";
    // Playlist table
    public static final String P_SONG_KEYS = "SongIds";
    // Current table
    public static final String C_SONG_ID = "SongId";
    public static final String C_ALBUM_ID = "AlbumId";

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
    private static final String createPlaylistTable = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, %s TEXT)",
            PLAYLIST_TABLE, ID, P_SONG_KEYS);
    private static final String createCurrentTable = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            "%s INTEGER, %s INTEGER)", CURRENT_TABLE, ID, C_SONG_ID, C_ALBUM_ID);


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createPlaylistTable);
        sqLiteDatabase.execSQL(createCurrentTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i(TAG, "Update database tables...");
        dropTables(sqLiteDatabase);
        onCreate(sqLiteDatabase);
    }


    private static void resetData(SQLiteDatabase database) {
        database.execSQL(DROP_PLAYLIST_TABLE);
        database.execSQL(DROP_CURRENT_TABLE);

        database.execSQL(createPlaylistTable);
        database.execSQL(createCurrentTable);
    }

    /*****
     * DROP Statements
     *****/
    private static final String DROP_PLAYLIST_TABLE = "DROP TABLE IF EXISTS " + PLAYLIST_TABLE;
    private static final String DROP_CURRENT_TABLE = "DROP TABLE IF EXISTS " + CURRENT_TABLE;

    public void dropTables(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DROP_PLAYLIST_TABLE);
        sqLiteDatabase.execSQL(DROP_CURRENT_TABLE);
    }
}
