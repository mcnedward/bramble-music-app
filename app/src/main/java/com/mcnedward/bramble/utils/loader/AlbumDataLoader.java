package com.mcnedward.bramble.utils.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Artist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward on 23/12/15.
 */
public class AlbumDataLoader extends BaseDataLoader<Album> {

    public AlbumDataLoader(Context context) {
        super(context);
    }

    @Override
    protected Uri getMediaUri() {
        return MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
    }

    @Override
    protected String[] getMediaColumns() {
        return new String[] {
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ALBUM_KEY,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS,
                MediaStore.Audio.Albums.FIRST_YEAR,
                MediaStore.Audio.Albums.LAST_YEAR,
                MediaStore.Audio.Albums.ALBUM_ART
        };
    }

    @Override
    protected String getMediaSelection() {
        return null;
    }

    @Override
    protected String[] getMediaSelectionArgs() {
        return new String[0];
    }

    @Override
    protected String getMediaSrtOrder() {
        return MediaStore.Audio.Albums.ALBUM + " DESC";
    }

    @Override
    protected List<Album> handleMediaCursor(Cursor cursor) {
        List<Album> albums = new ArrayList<>();
        while (cursor.moveToNext()) {
            // Get album information
            Integer albumId = cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Albums._ID));
            String albumName = cursor
                    .getString(cursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.ALBUM));
            String albumKey = cursor
                    .getString(cursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.ALBUM_KEY));
            Integer numberOfSongs = cursor
                    .getInt(cursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.NUMBER_OF_SONGS));
            Integer firstYear = cursor
                    .getInt(cursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.FIRST_YEAR));
            Integer lastYear = cursor
                    .getInt(cursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.LAST_YEAR));
            String albumArt = cursor
                    .getString(cursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.ALBUM_ART));

            // Create a new album and add it to the total album list and the artist album list
            albums.add(new Album(albumId, albumName, albumKey,
                    numberOfSongs, firstYear, lastYear, albumArt));
        }
        return albums;
    }
}
