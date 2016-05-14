package com.mcnedward.bramble.media;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward on 22/12/15.
 */
public class Artist extends Media implements Serializable {

    private String artistName;
    private int numberOfAlbums;
    private List<String> albumKeys;

    public Artist(int artistId, String artistName, String artistKey, int numberOfAlbums, List<String> albumKeys) {
        super(artistId, artistKey, MediaType.ARTIST);
        this.artistName = artistName;
        this.numberOfAlbums = numberOfAlbums;
        this.albumKeys = albumKeys;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getNumberOfAlbums() {
        return numberOfAlbums;
    }

    public void setNumberOfAlbums(int numberOfAlbums) {
        this.numberOfAlbums = numberOfAlbums;
    }

    public List<String> getAlbumKeys() {
        return albumKeys;
    }

    public void setAlbumKeys(List<String> albumKeys) {
        this.albumKeys = albumKeys;
    }

    @Override
    public String toString() {
        return artistName;
    }
}
