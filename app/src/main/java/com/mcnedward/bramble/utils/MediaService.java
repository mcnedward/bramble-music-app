package com.mcnedward.bramble.utils;

import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Artist;
import com.mcnedward.bramble.media.MediaType;
import com.mcnedward.bramble.media.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward on 25/12/15.
 */
public final class MediaService {

    private List<Artist> artists;
    private List<Album> albums;
    private List<Song> songs;

    public MediaService() {
        artists = new ArrayList<>();
        albums = new ArrayList<>();
        songs = new ArrayList<>();
    }

    public List<Album> findAlbumsForArtist(Artist artist) {
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
}
