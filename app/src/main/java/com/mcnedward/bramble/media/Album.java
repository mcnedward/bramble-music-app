package com.mcnedward.bramble.media;

import java.io.Serializable;

/**
 * Created by edward on 22/12/15.
 */
public class Album implements Serializable {

    private int albumId;
    private String albumName;
    private String albumKey;
    private int numberOfSongs;
    private int firstYear;
    private int lastYear;
    private String albumArt;

    public Album(String albumName) {
        this.albumName = albumName;
    }

    public Album(int albumId, String albumName, String albumKey, int numberOfSongs, int firstYear, int lastYear, String albumArt) {
        this.albumId = albumId;
        this.albumName = albumName;
        this.albumKey = albumKey;
        this.numberOfSongs = numberOfSongs;
        this.firstYear = firstYear;
        this.lastYear = lastYear;
        this.albumArt = albumArt;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumKey() {
        return albumKey;
    }

    public void setAlbumKey(String albumKey) {
        this.albumKey = albumKey;
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
