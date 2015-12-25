package com.mcnedward.bramble.utils.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.mcnedward.bramble.media.Artist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward on 23/12/15.
 */
public class ArtistDataLoader extends BaseDataLoader<Artist> {

    public ArtistDataLoader(Context context) {
        super(context);
    }

    @Override
    protected Uri getMediaUri() {
        return MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
    }

    @Override
    protected String[] getMediaColumns() {
        return new String[] {
                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.ARTIST_KEY,
                MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
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
        return MediaStore.Audio.Artists.ARTIST + " ASC";
    }

    @Override
    protected List<Artist> handleMediaCursor(Cursor cursor) {
        List<Artist> artists = new ArrayList<>();
        while (cursor.moveToNext()) {
            Integer artistId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID));
            String artistName = cursor.getString(
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST));
            String artistKey = cursor.getString(
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST_KEY));
            Integer numberOfAlbums = cursor.getInt(
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));
            artists.add(new Artist(artistId, artistName, artistKey, numberOfAlbums));
        }
        return artists;
    }
}
