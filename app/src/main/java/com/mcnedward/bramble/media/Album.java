package com.mcnedward.bramble.media;

import android.content.SharedPreferences;

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
    private List<Song> songs;

    public Album(int albumId, String albumName, String albumKey, String artist, int numberOfSongs, int firstYear, int lastYear, String albumArt) {
        super(albumId, albumArt, albumName, albumKey, MediaType.ALBUM);
        this.albumName = albumName;
        this.artist = artist;
        this.numberOfSongs = numberOfSongs;
        this.firstYear = firstYear;
        this.lastYear = lastYear;
        this.albumArt = albumArt;
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
    }

    @Override
    public void saveMedia(SharedPreferences.Editor editor) {
        editor.putString("albumName", albumName);
        editor.putString("artist", artist);
        editor.putInt("numberOfSongs", numberOfSongs);
        editor.putInt("firstYear", firstYear);
        editor.putInt("lastYear", lastYear);
        editor.putString("albumArt", albumArt);
    }

    public static Album get(SharedPreferences sharedPreferences) {
        int id = sharedPreferences.getInt("id", 0);
        if (id == 0) return null;

        String imagePath = sharedPreferences.getString("imagePath", "");
        String key = sharedPreferences.getString("key", "");
        String title = sharedPreferences.getString("title", "");
        String albumName = sharedPreferences.getString("albumName", "");
        String artist = sharedPreferences.getString("artist", "");
        int numberOfSongs = sharedPreferences.getInt("numberOfSongs", 0);
        int firstYear = sharedPreferences.getInt("firstYear", 0);
        int lastYear = sharedPreferences.getInt("lastYear", 0);
        String albumArt = sharedPreferences.getString("albumArt", "");

        return new Album(id, imagePath, title, key, MediaType.ALBUM, albumName, artist, numberOfSongs, firstYear, lastYear, albumArt);
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
