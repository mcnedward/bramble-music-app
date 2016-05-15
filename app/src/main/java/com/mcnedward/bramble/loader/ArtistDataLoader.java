package com.mcnedward.bramble.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.mcnedward.bramble.activity.MainActivity;
import com.mcnedward.bramble.media.Artist;
import com.mcnedward.bramble.media.MediaType;
import com.mcnedward.bramble.utils.MediaCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward on 23/12/15.
 */
public class ArtistDataLoader extends BaseDataLoader<Artist> {

    private List<Artist> artists;

    public ArtistDataLoader(Context context) {
        super(MediaType.ARTIST, context);
    }

    @Override
    protected Uri getMediaUri() {
        return MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
    }

    @Override
    protected String[] getMediaColumns() {
        return new String[]{
                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.ARTIST_KEY,
                MediaStore.Audio.Artists.NUMBER_OF_ALBUMS
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

            List<String> albumKeys = loadAlbumKeysForArtist(artistId);

            artists.add(new Artist(artistId, artistName, artistKey, numberOfAlbums, albumKeys));
        }
        return artists;
    }

    @Override
    public void addToMediaService(List<Artist> artistList) {
        MediaCache.saveArtists(artistList);
    }

    private List<String> loadAlbumKeysForArtist(int artistId) {
        List<String> albumKeys = new ArrayList<>();
        final Uri albumUri = MediaStore.Audio.Artists.Albums.getContentUri("external", artistId);
        final String[] albumCols = {
                MediaStore.Audio.Artists.Albums.ALBUM_KEY,
                MediaStore.Audio.Artists.Albums.ALBUM
        };
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(albumUri, albumCols, null, null, MediaStore.Audio.Artists.Albums.ALBUM + " ASC");
            while (cursor.moveToNext()) {
                String albumKey = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.ALBUM_KEY));
                albumKeys.add(albumKey);
            }
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return albumKeys;
    }
}
