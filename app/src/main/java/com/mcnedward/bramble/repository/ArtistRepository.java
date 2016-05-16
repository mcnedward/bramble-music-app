package com.mcnedward.bramble.repository;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.mcnedward.bramble.media.Artist;
import com.mcnedward.bramble.media.MediaType;

/**
 * Created by Edward on 5/16/2016.
 */
public class ArtistRepository extends BaseRepository<Artist> {

    public ArtistRepository(Context context) {
        super(MediaType.ARTIST, context);
    }

    @Override
    public Artist createMedia(Cursor cursor) {
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
    public Uri getMediaUri() {
        return MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
    }

    @Override
    public String getSortOrder() {
        return MediaStore.Audio.Artists.ARTIST + " ASC";
    }

    @Override
    public String[] getColumns() {
        return new String[]{
                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.ARTIST_KEY,
                MediaStore.Audio.Artists.NUMBER_OF_ALBUMS
        };
    }
}
