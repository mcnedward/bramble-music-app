package com.mcnedward.bramble.media;

import android.content.SharedPreferences;
import android.util.Log;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Created by edward on 24/12/15.
 */
public class Song extends Media implements Serializable {

    private String title;
    private String displayName;
    private int artistId;
    private int albumId;
    private String composer;
    private int track;
    private int duration;
    private int year;
    private int dateAdded;
    private String mimeType;
    private String data;

    public Song(int songId, String title, String titleKey, String displayName, int artistId, int albumId, String composer, int track, int duration, int year, int dateAdded, String mimeType, String data) {
        super(songId, "", title, titleKey, MediaType.SONG);
        this.title = title;
        this.displayName = displayName;
        this.artistId = artistId;
        this.albumId = albumId;
        this.composer = composer;
        this.track = track;
        this.duration = duration;
        this.year = year;
        this.dateAdded = dateAdded;
        this.mimeType = mimeType;
        this.data = data;

        handleTrack(track);
    }

    public Song(int id, String imagePath, String title, String key, MediaType mediaType, String displayName, int artistId, int
            albumId, String composer, int track, int duration, int year, int dateAdded, String mimeType, String data) {
        super(id, imagePath, title, key, mediaType);
        this.title = title;
        this.displayName = displayName;
        this.artistId = artistId;
        this.albumId = albumId;
        this.composer = composer;
        this.track = track;
        this.duration = duration;
        this.year = year;
        this.dateAdded = dateAdded;
        this.mimeType = mimeType;
        this.data = data;
    }

    private void handleTrack(int trackNumber) {
        if (trackNumber > 999) {
            track = trackNumber % 1000;
        }
        if (track == 0)
            track = 1;
    }

    @Override
    public void saveMedia(SharedPreferences.Editor editor) {
        editor.putString("title", title);
        editor.putString("displayName", displayName);
        editor.putInt("artistId", artistId);
        editor.putInt("albumId", albumId);
        editor.putString("composer", composer);
        editor.putInt("track", track);
        editor.putInt("duration", year);
        editor.putInt("year", year);
        editor.putInt("dateAdded", dateAdded);
        editor.putString("mimeType", mimeType);
        editor.putString("data", data);
    }

    public static Song get(SharedPreferences sharedPreferences) {
        int id = sharedPreferences.getInt("id", 0);
        if (id == 0) return null;

        String imagePath = sharedPreferences.getString("imagePath", "");
        String key = sharedPreferences.getString("key", "");
        String title = sharedPreferences.getString("title", "");
        String displayName = sharedPreferences.getString("displayName", "");
        int artistId = sharedPreferences.getInt("artistId", 0);
        int albumId = sharedPreferences.getInt("albumId", 0);
        String composer = sharedPreferences.getString("composer", "");
        int track = sharedPreferences.getInt("artistId", 0);
        int duration = sharedPreferences.getInt("albumId", 0);
        int year = sharedPreferences.getInt("year", 0);
        int dateAdded = sharedPreferences.getInt("dateAdded", 0);
        String mimeType = sharedPreferences.getString("mimeType", "");
        String data = sharedPreferences.getString("data", "");

        return new Song(id, imagePath, title, key, MediaType.SONG, displayName, artistId, albumId, composer, track, duration, year, dateAdded, mimeType, data);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public int getTrack() {
        return track;
    }

    public void setTrack(int track) {
        this.track = track;
    }

    public String getDuration() {
        return String.format("%d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(int dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return title;
    }
}
