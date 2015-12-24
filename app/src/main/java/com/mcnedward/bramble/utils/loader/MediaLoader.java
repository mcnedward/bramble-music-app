package com.mcnedward.bramble.utils.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.mcnedward.bramble.media.Artist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward on 23/12/15.
 */
public class MediaLoader {
    final private static String TAG = "MediaLoader";

    private Context context;

    private ArtistDataLoader artistDataLoader;

    public MediaLoader(Context context) {
        this.context = context;
        artistDataLoader = new ArtistDataLoader(context, null, null, null, null, null);
    }

    public List<Artist> getArtists() {
        List<Artist> artists = new ArrayList<>();
        Cursor cursor = null;
        try {
            // Set the Uri and columns for extracting artist media data
            final Uri artistUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
            final String[] artistCols = {
                    MediaStore.Audio.Artists._ID,
                    MediaStore.Audio.Artists.ARTIST,
                    MediaStore.Audio.Artists.ARTIST_KEY,
                    MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
            };
            cursor = context.getContentResolver().query(artistUri, artistCols, null, null, null);

            int artistCount = cursor.getCount();
            int x = 1;
            Log.d(TAG, "Number of results for artist retrieval: " + artistCount);
            while (cursor.moveToNext()) {
                // Get artist information
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
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return artists;
    }
}
