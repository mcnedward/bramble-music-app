package com.mcnedward.bramble.media;

import java.io.Serializable;

/**
 * Created by edward on 22/12/15.
 */
public class Album extends Media implements Serializable {

    private String albumName;
    private String artist;
    private int numberOfSongs;
    private int firstYear;
    private int lastYear;
    private String albumArt;

    public Album(int albumId, String albumName, String albumKey, String artist, int numberOfSongs, int firstYear, int lastYear, String albumArt) {
        super(albumId, albumKey, MediaType.ALBUM);
        this.albumName = albumName;
        this.artist = artist;
        this.numberOfSongs = numberOfSongs;
        this.firstYear = firstYear;
        this.lastYear = lastYear;
        this.albumArt = albumArt;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    public void setNumberOfSongs(int numberOfSongs) {
        this.numberOfSongs = numberOfSongs;
    }

    public int getFirstYear() {
        return firstYear;
    }

    public void setFirstYear(int firstYear) {
        this.firstYear = firstYear;
    }

    public int getLastYear() {
        return lastYear;
    }

    public void setLastYear(int lastYear) {
        this.lastYear = lastYear;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    @Override
    public String toString() {
        return albumName;
    }
}
