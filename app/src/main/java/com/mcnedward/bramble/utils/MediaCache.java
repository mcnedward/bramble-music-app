package com.mcnedward.bramble.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.mcnedward.bramble.exception.MediaNotFoundException;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Artist;
import com.mcnedward.bramble.media.Media;
import com.mcnedward.bramble.media.MediaType;
import com.mcnedward.bramble.media.Song;
import com.mcnedward.bramble.listener.AlbumLoadListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by edward on 25/12/15.
 */
public final class MediaCache {
    private static final String TAG = "MediaCache";

    private static final String PREFERENCE_KEY = "preference_key";

    private static Map<String, Object> mediaCache = new HashMap<>();
    private static List<AlbumLoadListener> listeners = new ArrayList<>();
    private static boolean loadingArtists, loadingAlbums, loadingSongs;

    public static void saveArtist(Artist artist, Context context) {
        if (artist == null) return;
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE).edit();
        artist.save(editor);
    }

    public static void saveAlbum(Album album, Context context) {
        if (album == null) return;
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE).edit();
        album.save(editor);
    }

    public static void saveSong(Song song, Context context) {
        if (song == null) return;
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE).edit();
        song.save(editor);
    }

    public static Artist getArtist(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
        Artist artist = null;
        try {
            artist = Artist.get(sharedPreferences);
        } catch (NullPointerException e) {
            Log.w(TAG, "Could not create a artist from preferences.", e);
        }
        return artist;
    }

    public static Album getAlbum(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
        Album album = null;
        try {
            album = Album.get(sharedPreferences);
        } catch (NullPointerException e) {
            Log.w(TAG, "Could not create a album from preferences.", e);
        }
        return album;
    }

    public static Song getSong(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
        Song song = null;
        try {
            song = Song.get(sharedPreferences);
        } catch (NullPointerException e) {
            Log.w(TAG, "Could not create a song from preferences.", e);
        }
        return song;
    }

    public static void saveArtists(List<Artist> artists) {
        mediaCache.put("artists", artists);
    }

    public static void saveAlbums(List<Album> albums) {
        mediaCache.put("albums", albums);
    }

    public static void saveSongs(List<Song> songs) {
        mediaCache.put("songs", songs);
    }

    public static void registerAlbumLoadListener(AlbumLoadListener listener) {
        listeners.add(listener);
        if (!loadingAlbums)
            notifyAlbumLoadListeners();
    }

    public static void notifyAlbumLoadListeners() {
        if (!listeners.isEmpty())
            for (AlbumLoadListener listener : listeners)
                listener.notifyAlbumLoadReady();
    }

    public static void setLoading(MediaType mediaType, boolean loading) {
        switch (mediaType) {
            case ARTIST:
                loadingArtists = loading;
                break;
            case ALBUM:
                loadingAlbums = loading;
                break;
            case SONG:
                loadingSongs = loading;
                break;
        }
    }

}
