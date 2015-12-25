package com.mcnedward.bramble.utils.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Artist;
import com.mcnedward.bramble.media.Song;

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
            Log.d(TAG, "Done with artists!");
        }
        return artists;
    }

    public List<Album> getAlbums() {
        List<Album> albums = new ArrayList<>();
        Cursor cursor = null;
        try {
            // Get the album information for each artist
            final Uri albumUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
            final String[] albumCols = { MediaStore.Audio.Albums._ID,
                    MediaStore.Audio.Albums.ALBUM,
                    MediaStore.Audio.Albums.ALBUM_KEY,
                    MediaStore.Audio.Albums.NUMBER_OF_SONGS,
                    MediaStore.Audio.Albums.FIRST_YEAR,
                    MediaStore.Audio.Albums.LAST_YEAR,
                    MediaStore.Audio.Albums.ALBUM_ART };
            cursor = context.getContentResolver().query(albumUri, albumCols,
                    null, null, null);

            int albumCount = cursor.getCount();
            Log.d(TAG, "Number of results for album retrieval: " + albumCount);
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
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            Log.d(TAG, "Done with albums@");
        }
        return albums;
    }

    public List<Song> getSongs() {
        List<Song> songs = new ArrayList<>();
        Cursor cursor = null;
        try {
            // Set the Uri and columns for extracting artist media data
            final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            final String[] cols = { MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.TITLE_KEY,
                    MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.Media.ARTIST_ID,
                    MediaStore.Audio.Media.ALBUM_ID,
                    MediaStore.Audio.Media.COMPOSER,
                    MediaStore.Audio.Media.TRACK,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.YEAR,
                    MediaStore.Audio.Media.DATE_ADDED,
                    MediaStore.Audio.Media.MIME_TYPE,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.IS_MUSIC };

            cursor = context.getContentResolver().query(uri, cols,
                    null, null, null);

            int songCount = cursor.getCount();
            Log.d(TAG, "Number of results for artist retrieval: " + songCount);
            while (cursor.moveToNext()) {
                Integer titleId = cursor.getInt(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                String title = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String titleKey = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE_KEY));
                String displayName = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                Integer artistId = cursor
                        .getInt(cursor
                                .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID));
                Integer albumId = cursor
                        .getInt(cursor
                                .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                String composer = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Audio.Media.COMPOSER));
                Integer track = cursor.getInt(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK));
                Integer duration = cursor
                        .getInt(cursor
                                .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                Integer year = cursor.getInt(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR));
                Integer dateAdded = cursor
                        .getInt(cursor
                                .getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED));
                String mimeType = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE));
                String data = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                Integer isMusic = Integer
                        .parseInt(cursor.getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Audio.Media.IS_MUSIC)));

                if (isMusic == 1)
                songs.add(new Song(titleId, title, titleKey,
                        displayName, artistId, albumId, composer, track,
                        duration, year, dateAdded, mimeType, data));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            Log.d(TAG, "Done with songs!");
        }
        return songs;
    }
}
