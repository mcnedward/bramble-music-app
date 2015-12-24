package com.mcnedward.bramble.media;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward on 22/12/15.
 */
public class Artist implements Serializable {

    private int artistId;
    private String artistName;
    private String artistKey;
    private int numberOfAlbums;
    private List<Album> albums;

    public Artist(String artistName) {
        this.artistName = artistName;
        albums = new ArrayList<>();
    }

    public Artist(int artistId, String artistName, String artistKey, int numberOfAlbums) {
        this(artistName);
        this.artistId = artistId;
        this.artistKey = artistKey;
        this.numberOfAlbums = numberOfAlbums;
    }

    public void addAlbum(Album album) {
        albums.add(album);
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistKey() {
        return artistKey;
    }

    public void setArtistKey(String artistKey) {
        this.artistKey = artistKey;
    }

    public int getNumberOfAlbums() {
        return numberOfAlbums;
    }

    public void setNumberOfAlbums(int numberOfAlbums) {
        this.numberOfAlbums = numberOfAlbums;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    @Override
    public String toString() {
        return artistName;
    }
}
