package com.mcnedward.bramble.repository.media;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.MediaType;
import com.mcnedward.bramble.repository.Repository;

import java.util.List;

/**
 * Created by Edward on 5/16/2016.
 */
public class AlbumRepository extends MediaRepository<Album> {

    public AlbumRepository(Context context) {
        super(context, MediaType.ALBUM);
    }

    public List<Album> getAlbumsForArtist(int artistId) {
        Uri mediaUri = MediaStore.Audio.Artists.Albums.getContentUri("external", artistId);
        return read(mediaUri, getColumns(), null, null, getOrderBy());
    }

    @Override
    public Album generateEntityFromCursor(Cursor cursor) {
        // Get album information
        Integer albumId = cursor.getInt(cursor
                .getColumnIndexOrThrow(MediaStore.Audio.Albums._ID));
        String albumName = cursor
                .getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.ALBUM));
        String albumKey = cursor
                .getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.ALBUM_KEY));
        String artist = cursor
                .getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.ARTIST));
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
        return new Album(albumId, albumName, albumKey, artist, numberOfSongs, firstYear, lastYear, albumArt);
    }

    @Override
    protected ContentValues generateContentValuesFromEntity(Album entity) {
        return null;
    }

    @Override
    public String getOrderBy() {
        return MediaStore.Audio.Albums.ALBUM + " ASC";
    }

    @Override
    public Uri getUri() {
        return MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
    }

    @Override
    public String[] getColumns() {
        return new String[]{
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM_KEY,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS,
                MediaStore.Audio.Albums.FIRST_YEAR,
                MediaStore.Audio.Albums.LAST_YEAR,
                MediaStore.Audio.Albums.ALBUM_ART
        };
    }

}
