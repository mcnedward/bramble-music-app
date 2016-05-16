package com.mcnedward.bramble.utils;

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

    private static Map<String, Object> mediaCache = new HashMap<>();
    private static List<AlbumLoadListener> listeners = new ArrayList<>();
    private static boolean loadingArtists, loadingAlbums, loadingSongs;

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
