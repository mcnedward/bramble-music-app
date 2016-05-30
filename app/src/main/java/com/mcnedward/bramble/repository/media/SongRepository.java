package com.mcnedward.bramble.repository.media;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.mcnedward.bramble.exception.EntityDoesNotExistException;
import com.mcnedward.bramble.entity.media.MediaType;
import com.mcnedward.bramble.entity.media.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Edward on 5/16/2016.
 */
public class SongRepository extends MediaRepository<Song> {

    private static SongComparator mSongComparator;

    public SongRepository(Context context) {
        super(context, MediaType.SONG);
        mSongComparator = new SongComparator();
    }

    public List<Song> getSongsForAlbum(long albumId) {
        List<Song> songs = read(String.format("%s = ?", MediaStore.Audio.Media.ALBUM_ID), String.valueOf(albumId));
        Collections.sort(songs, mSongComparator);
        return songs;
    }

    public List<Song> getSongsForKeys(List<String> keys) {
        List<Song> songs = new ArrayList<>();
        for (String key : keys) {
            Song song = null;
            try {
                song = get(key);
            } catch (EntityDoesNotExistException e) {
                e.printStackTrace();
            }
            if (song != null)
                songs.add(song);
        }
        Collections.sort(songs, mSongComparator);
        return songs;
    }

    @Override
    public Song generateEntityFromCursor(Cursor cursor) {
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
            return new Song(titleId, title, titleKey, displayName, artistId, albumId, composer, track, duration, year, dateAdded, mimeType, data);
        return null;
    }

    @Override
    protected ContentValues generateContentValuesFromEntity(Song entity) {
        return null;
    }

    @Override
    public Uri getUri() {
        return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    public String getOrderBy() {
        return MediaStore.Audio.Media.TITLE + " DESC";
    }

    @Override
    public String[] getColumns() {
        return new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE_KEY,
                MediaStore.Audio.Media.TITLE,
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
                MediaStore.Audio.Media.IS_MUSIC
        };
    }

    protected static class SongComparator implements Comparator<Song> {
        @Override
        public int compare(Song lhs, Song rhs) {
            return lhs.getTrack() - rhs.getTrack();
        }
    }
}
