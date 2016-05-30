package com.mcnedward.bramble.entity.media;

import android.content.SharedPreferences;

import java.io.Serializable;
import java.util.ArrayList;
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
    private List<Long> songIds;
    private List<Song> songs;

    public Album(int albumId, String albumName, String albumKey, String artist, int numberOfSongs, int firstYear, int lastYear, String albumArt) {
        super(albumId, albumArt, albumName, albumKey, MediaType.ALBUM);
        this.albumName = albumName;
        this.artist = artist;
        this.numberOfSongs = numberOfSongs;
        this.firstYear = firstYear;
        this.lastYear = lastYear;
        this.albumArt = albumArt;
        songIds = new ArrayList<>();
    }

    public Album(int id, String imagePath, String title, String key, MediaType mediaType, String albumName, String artist, int numberOfSongs, int
            firstYear, int lastYear, String albumArt) {
        super(id, imagePath, title, key, mediaType);
        this.albumName = albumName;
        this.artist = artist;
        this.numberOfSongs = numberOfSongs;
        this.firstYear = firstYear;
        this.lastYear = lastYear;
        this.albumArt = albumArt;
        songIds = new ArrayList<>();
    }

    @Override
    public void saveMedia(SharedPreferences.Editor editor) {
        String theMediaType = MediaType.ALBUM.type();
        editor.putString(theMediaType + "_albumName", albumName);
        editor.putString(theMediaType + "_artist", artist);
        editor.putInt(theMediaType + "_numberOfSongs", numberOfSongs);
        editor.putInt(theMediaType + "_firstYear", firstYear);
        editor.putInt(theMediaType + "_lastYear", lastYear);
        editor.putString(theMediaType + "_albumArt", albumArt);
    }

    public static Album get(SharedPreferences sharedPreferences) {
        String theMediaType = MediaType.ALBUM.type();
        int id = sharedPreferences.getInt(theMediaType + "_id", 0);
        if (id == 0) return null;

        String imagePath = sharedPreferences.getString(theMediaType + "_imagePath", "");
        String title = sharedPreferences.getString(theMediaType + "_title", "");
        String key = sharedPreferences.getString(theMediaType + "_key", "");
        String albumName = sharedPreferences.getString(theMediaType + "_albumName", "");
        String artist = sharedPreferences.getString(theMediaType + "_artist", "");
        int numberOfSongs = sharedPreferences.getInt(theMediaType + "_numberOfSongs", 0);
        int firstYear = sharedPreferences.getInt(theMediaType + "_firstYear", 0);
        int lastYear = sharedPreferences.getInt(theMediaType + "_lastYear", 0);
        String albumArt = sharedPreferences.getString(theMediaType + "_albumArt", "");

        return new Album(id, imagePath, title, key, MediaType.ALBUM, albumName, artist, numberOfSongs, firstYear, lastYear, albumArt);
    }

    public void setAlbumSongIds(List<Song> songs) {
        for (Song song : songs)
            songIds.add(song.getId());
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

    public List<Long> getSongIds() {
        return songIds;
    }

    public void setSongIds(List<Long> songIds) {
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
