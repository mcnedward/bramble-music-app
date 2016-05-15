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
    private static SongComparator songComparator = new SongComparator();
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

    public static List<Artist> getArtists() {

        List<Artist> artists = (List<Artist>) mediaCache.get("artists");
        if (artists == null) {
            artists = new ArrayList<>();
            saveArtists(artists);
        }
        return artists;
    }

    public static List<Album> getAlbums() {
        List<Album> albums = (List<Album>) mediaCache.get("albums");
        if (albums == null) {
            albums = new ArrayList<>();
            saveAlbums(albums);
        }
        return albums;
    }

    public static List<Song> getSongs() {
        List<Song> songs = (List<Song>) mediaCache.get("songs");
        if (songs == null) {
            songs = new ArrayList<>();
            saveSongs(songs);
        }
        return songs;
    }

    public static List<Album> getAlbumsForArtist(Artist artist) {
        List<Album> albumList = new ArrayList<>();
        List<Album> albums = getAlbums();
        if (albums == null) return albumList;

        for (String artistAlbumKey : artist.getAlbumKeys()) {
            for (Album album : albums) {
                if (album.getKey().equals(artistAlbumKey)) {
                    albumList.add(album);
                    break;
                }
            }
        }
        return albumList;
    }

    // TODO Add exceptions
    public static List<Song> getSongsForAlbum(Album album) {
        List<Song> songList = new ArrayList<>();
        List<Song> songs = getSongs();
        if (songs == null) return songList;

        for (int albumId : album.getSongIds()) {
            for (Song song : songs) {
                if (song.getId() == albumId) {
                    songList.add(song);
                    break;
                }
            }
        }
        Collections.sort(songList, songComparator);
        return songList;
    }

    public static Album getAlbumForSong(Song song) throws MediaNotFoundException {
        List<Album> albums = getAlbums();
        if (albums != null) {
            for (Album album : albums) {
                if (album.getId() == song.getAlbumId())
                    return album;
            }
        }
        throw new MediaNotFoundException(String.format("Could not find an album for the song '%s' with the album id: %s", song, song.getAlbumId()));
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

    protected static class SongComparator implements Comparator<Song> {
        @Override
        public int compare(Song lhs, Song rhs) {
            return lhs.getTrack() - rhs.getTrack();
        }
    }
}
