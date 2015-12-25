package com.mcnedward.bramble.utils.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.mcnedward.bramble.activity.MainActivity;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Artist;
import com.mcnedward.bramble.media.Media;
import com.mcnedward.bramble.media.MediaType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward on 23/12/15.
 */
public class AlbumDataLoader extends BaseDataLoader<Album> {

    public AlbumDataLoader(Context context) {
        super(MediaType.ALBUM, context);
    }

    @Override
    protected Uri getMediaUri() {
        return MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
    }

    @Override
    protected String[] getMediaColumns() {
        return new String[]{
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ALBUM_KEY,
                MediaStore.Audio.Albums.ARTIST,
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

            List<Integer> songIds = loadSongsForAlbum(albumId);

            // Create a new album and add it to the total album list and the artist album list
            albums.add(new Album(albumId, albumName, albumKey, artist,
                    numberOfSongs, firstYear, lastYear, albumArt, songIds));
        }

        return albums;
    }

    @Override
    public void addToMediaService(List<Album> albumList) {
        MainActivity.mediaService.setAlbums(albumList);
    }

    private List<Integer> loadSongsForAlbum(int albumId) {
        List<Integer> songIds = new ArrayList<>();
        final Uri songUri = MediaStore.Audio.Media.getContentUri("external");
        final String[] songCols = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE
        };
        final String selection = String.format("%s = ?", MediaStore.Audio.Media.ALBUM_ID);
        final String[] selectionArgs = new String[] { String.valueOf(albumId) };
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(songUri, songCols, selection, selectionArgs, MediaStore.Audio.Media.TITLE + " ASC");
            while (cursor.moveToNext()) {
                Integer songId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                songIds.add(songId);
            }
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return songIds;
    }
}
