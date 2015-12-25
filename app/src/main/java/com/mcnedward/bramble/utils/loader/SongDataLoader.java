package com.mcnedward.bramble.utils.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.mcnedward.bramble.activity.MainActivity;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Artist;
import com.mcnedward.bramble.media.MediaType;
import com.mcnedward.bramble.media.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward on 23/12/15.
 */
public class SongDataLoader extends BaseDataLoader<Song> {

    public SongDataLoader(Context context) {
        super(MediaType.SONG, context);
    }

    @Override
    protected Uri getMediaUri() {
        return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    protected String[] getMediaColumns() {
        return new String[] {
                MediaStore.Audio.Media._ID,
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
                MediaStore.Audio.Media.IS_MUSIC
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
        return MediaStore.Audio.Media.TITLE + " DESC";
    }

    @Override
    protected List<Song> handleMediaCursor(Cursor cursor) {
        List<Song> songs = new ArrayList<>();
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
        return songs;
    }

    @Override
    public void addToMediaService(List<Song> songList) {
        MainActivity.mediaService.setSongs(songList);
    }
}
