package com.mcnedward.bramble.entity.media;

import android.content.SharedPreferences;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Created by edward on 24/12/15.
 */
public class Song extends Media implements Serializable {

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
        this(songId, "", title, titleKey, MediaType.SONG, displayName, artistId, albumId, composer, track, duration, year, dateAdded, mimeType, data);
    }

    public Song(int id, String imagePath, String title, String key, MediaType mediaType, String displayName, int artistId, int
            albumId, String composer, int track, int duration, int year, int dateAdded, String mimeType, String data) {
        super(id, imagePath, title, key, mediaType);
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

    private void handleTrack(int trackNumber) {
        if (trackNumber > 999) {
            track = trackNumber % 1000;
        }
        if (track == 0)
            track = 1;
    }

    @Override
    public void saveMedia(SharedPreferences.Editor editor) {
        String theMediaType = MediaType.SONG.type();
        editor.putString(theMediaType + "_displayName", displayName);
        editor.putInt(theMediaType + "_artistId", artistId);
        editor.putInt(theMediaType + "_albumId", albumId);
        editor.putString(theMediaType + "_composer", composer);
        editor.putInt(theMediaType + "_track", track);
        editor.putInt(theMediaType + "_duration", duration);
        editor.putInt(theMediaType + "_year", year);
        editor.putInt(theMediaType + "_dateAdded", dateAdded);
        editor.putString(theMediaType + "_mimeType", mimeType);
        editor.putString(theMediaType + "_data", data);
    }

    public static Song get(SharedPreferences sharedPreferences) {
        String theMediaType = MediaType.SONG.type();
        int id = sharedPreferences.getInt(theMediaType + "_id", 0);
        if (id == 0) return null;

        String imagePath = sharedPreferences.getString(theMediaType + "_imagePath", "");
        String title = sharedPreferences.getString(theMediaType + "_title", "");
        String key = sharedPreferences.getString(theMediaType + "_key", "");
        String displayName = sharedPreferences.getString(theMediaType + "_displayName", "");
        int artistId = sharedPreferences.getInt(theMediaType + "_artistId", 0);
        int albumId = sharedPreferences.getInt(theMediaType + "_albumId", 0);
        String composer = sharedPreferences.getString(theMediaType + "_composer", "");
        int track = sharedPreferences.getInt(theMediaType + "_track", 0);
        int duration = sharedPreferences.getInt(theMediaType + "_duration", 0);
        int year = sharedPreferences.getInt(theMediaType + "_year", 0);
        int dateAdded = sharedPreferences.getInt(theMediaType + "_dateAdded", 0);
        String mimeType = sharedPreferences.getString(theMediaType + "_mimeType", "");
        String data = sharedPreferences.getString(theMediaType + "_data", "");

        return new Song(id, imagePath, title, key, MediaType.SONG, displayName, artistId, albumId, composer, track, duration, year, dateAdded, mimeType, data);
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
        return mTitle;
    }
}
