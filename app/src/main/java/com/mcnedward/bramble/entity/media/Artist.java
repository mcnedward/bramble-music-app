package com.mcnedward.bramble.entity.media;

import android.content.SharedPreferences;

import com.mcnedward.bramble.entity.data.ArtistImage;
import com.mcnedward.bramble.entity.data.Thumbnail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward on 22/12/15.
 */
public class Artist extends Media implements Serializable {

    private String artistName;
    private int numberOfAlbums;
    private List<ArtistImage> mArtistImages;

    public Artist(int artistId, String artistName, String artistKey, int numberOfAlbums) {
        super(artistId, "", artistName, artistKey, MediaType.ARTIST);
        this.artistName = artistName;
        this.numberOfAlbums = numberOfAlbums;
        mArtistImages = new ArrayList<>();
    }

    public Artist(int id, String imagePath, String title, String key, MediaType mediaType, String artistName, int numberOfAlbums) {
        super(id, imagePath, title, key, mediaType);
        this.artistName = artistName;
        this.numberOfAlbums = numberOfAlbums;
        mArtistImages = new ArrayList<>();
    }

    @Override
    public void saveMedia(SharedPreferences.Editor editor) {
        String theMediaType = MediaType.ARTIST.type();
        editor.putString(theMediaType + "_artistName", artistName);
        editor.putInt(theMediaType + "_numberOfAlbums", numberOfAlbums);
    }

    public static Artist get(SharedPreferences sharedPreferences) {
        String theMediaType = MediaType.ARTIST.type();
        int id = sharedPreferences.getInt(theMediaType + "_id", 0);
        if (id == 0) return null;

        String imagePath = sharedPreferences.getString(theMediaType + "_imagePath", "");
        String title = sharedPreferences.getString(theMediaType + "_title", "");
        String key = sharedPreferences.getString(theMediaType + "_key", "");
        String artistName = sharedPreferences.getString(theMediaType + "_artistName", "");
        int numberOfAlbums = sharedPreferences.getInt(theMediaType + "_numberOfAlbums", 0);

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

    public List<ArtistImage> getArtistImages() {
        return mArtistImages;
    }

    public void setArtistImages(List<ArtistImage> artistImages) {
        this.mArtistImages = artistImages;
        // TODO This will be changed based on user preferences
        ArtistImage artistImage = artistImages.get(0);
        if (artistImage == null) return;
        setImageUrl(artistImage.getMediaUrl());
    }

    @Override
    public String toString() {
        return artistName;
    }
}
