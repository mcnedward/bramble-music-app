package com.mcnedward.bramble.media;

import java.io.Serializable;
import java.util.List;

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
    private List<Integer> songIds;
    private List<Song> songs;

    public Album(int albumId, String albumName, String albumKey, String artist, int numberOfSongs, int firstYear, int lastYear, String albumArt, List<Integer> songIds) {
        super(albumId, albumArt, albumName, albumKey, MediaType.ALBUM);
        this.albumName = albumName;
        this.artist = artist;
        this.numberOfSongs = numberOfSongs;
        this.firstYear = firstYear;
        this.lastYear = lastYear;
        this.albumArt = albumArt;
        this.songIds = songIds;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
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

    public List<Integer> getSongIds() {
        return songIds;
    }

    public void setSongIds(List<Integer> songIds) {
        this.songIds = songIds;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    @Override
    public String toString() {
        return albumName;
    }
}
