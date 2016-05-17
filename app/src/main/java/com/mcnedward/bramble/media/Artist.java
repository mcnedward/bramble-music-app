package com.mcnedward.bramble.media;

import android.content.SharedPreferences;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward on 22/12/15.
 */
public class Artist extends Media implements Serializable {

    private String artistName;
    private int numberOfAlbums;

    public Artist(int artistId, String artistName, String artistKey, int numberOfAlbums) {
        super(artistId, "", artistName, artistKey, MediaType.ARTIST);
        this.artistName = artistName;
        this.numberOfAlbums = numberOfAlbums;
    }

    public Artist(int id, String imagePath, String title, String key, MediaType mediaType, String artistName, int numberOfAlbums) {
        super(id, imagePath, title, key, mediaType);
        this.artistName = artistName;
        this.numberOfAlbums = numberOfAlbums;
    }

    @Override
    public void saveMedia(SharedPreferences.Editor editor) {
        editor.putString("artistName", artistName);
        editor.putInt("numberOfAlbums", numberOfAlbums);
    }

    public static Artist get(SharedPreferences sharedPreferences) {
        int id = sharedPreferences.getInt("id", 0);
        if (id == 0) return null;

        String imagePath = sharedPreferences.getString("imagePath", "");
        String key = sharedPreferences.getString("key", "");
        String title = sharedPreferences.getString("title", "");
        String artistName = sharedPreferences.getString("artistName", "");
        int numberOfAlbums = sharedPreferences.getInt("numberOfAlbums", 0);

        return new Artist(id, imagePath, title, key, MediaType.ARTIST, artistName, numberOfAlbums);
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

    @Override
    public String toString() {
        return artistName;
    }
}
