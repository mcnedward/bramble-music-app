package com.mcnedward.bramble.utils;

import com.mcnedward.bramble.exception.MediaNotFoundException;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Artist;
import com.mcnedward.bramble.media.MediaType;
import com.mcnedward.bramble.media.Song;
import com.mcnedward.bramble.utils.listener.AlbumLoadListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward on 25/12/15.
 */
public final class MediaCache {

    private List<Artist> artists;
    private List<Album> albums;
    private List<Song> songs;

    private AlbumLoadListener listener;

    private boolean loadingArtists, loadingAlbums, loadingSongs;

    public MediaCache() {
        artists = new ArrayList<>();
        albums = new ArrayList<>();
        songs = new ArrayList<>();
    }

    public List<Album> getAlbumsForArtist(Artist artist) {
        List<Album> albumList = new ArrayList<>();

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
    public List<Song> getSongsForAlbum(Album album) {
        List<Song> songList = new ArrayList<>();

        for (int albumId : album.getSongIds()) {
            for (Song song : songs) {
                if (song.getId() == albumId) {
                    songList.add(song);
                    break;
                }
            }
        }

        return songList;
    }

    public Album getAlbumForSong(Song song) throws MediaNotFoundException {
        for (Album album : albums) {
            if (album.getId() == song.getAlbumId())
                return album;
        }
        throw new MediaNotFoundException(String.format("Could not find an album for the song '%s' with the album id: %s", song, song.getAlbumId()));
    }

    public void registerAlbumLoadListener(AlbumLoadListener listener) {
        this.listener = listener;
        if (!loadingAlbums)
            notifyAlbumLoadListener();
    }

    public void notifyAlbumLoadListener() {
        if (listener != null)
            listener.notifyAlbumLoadReady();
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public void setLoading(MediaType mediaType, boolean loading) {
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

    public boolean isLoadingArtists() {
        return loadingArtists;
    }

    public boolean isLoadingAlbums() {
        return loadingAlbums;
    }

    public boolean isLoadingSongs() {
        return loadingSongs;
    }
}
