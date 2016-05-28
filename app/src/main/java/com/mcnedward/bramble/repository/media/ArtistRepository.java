package com.mcnedward.bramble.repository.media;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.mcnedward.bramble.media.Artist;
import com.mcnedward.bramble.media.MediaType;
import com.mcnedward.bramble.repository.Repository;

/**
 * Created by Edward on 5/16/2016.
 */
public class ArtistRepository extends MediaRepository<Artist> {

    public ArtistRepository(Context context) {
        super(context, MediaType.ARTIST);
    }

    @Override
    public Artist generateEntityFromCursor(Cursor cursor) {
        Integer artistId = cursor.getInt(
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID));
        String artistName = cursor.getString(
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST));
        String artistKey = cursor.getString(
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST_KEY));
        Integer numberOfAlbums = cursor.getInt(
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));

        return new Artist(artistId, artistName, artistKey, numberOfAlbums);
    }

    @Override
    protected ContentValues generateContentValuesFromEntity(Artist entity) {
        return null;
    }

    @Override
    public Uri getUri() {
        return MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
    }

    @Override
    public String getOrderBy() {
        return MediaStore.Audio.Artists.ARTIST + " ASC";
    }

    @Override
    public String[] getColumns() {
        return new String[]{
                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST_KEY,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.NUMBER_OF_ALBUMS
        };
    }
}
