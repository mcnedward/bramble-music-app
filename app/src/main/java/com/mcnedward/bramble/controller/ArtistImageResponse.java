package com.mcnedward.bramble.controller;

import com.mcnedward.bramble.entity.data.ArtistImage;
import com.mcnedward.bramble.entity.media.Artist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 5/28/2016.
 *
 * Response container for artist image search from bing. This will also allow for creating the ArtistImage entities.
 */
public class ArtistImageResponse implements Serializable {

    Artist mArtist; // The artist that the request used
    ArtistImageData d;

    public ArtistImageResponse(ArtistImageData d) {
        this.d = d;
    }

    public List<ArtistImage> getArtistImages() {
        List<ArtistImage> artistImages = new ArrayList<>();
        for (ArtistImageResultResponse aid : d.getResults()) {
            artistImages.add(new ArtistImage(aid));
        }
        return artistImages;
    }

    public ArtistImageData getD() {
        return d;
    }

    public void setD(ArtistImageData d) {
        this.d = d;
    }

    public Artist getArtist() {
        return mArtist;
    }

    public void setArtist(Artist artist) {
        mArtist = artist;
    }

    final class ArtistImageData {
        List<ArtistImageResultResponse> results;

        public ArtistImageData(List<ArtistImageResultResponse> results) {
            this.results = results;
        }

        public List<ArtistImageResultResponse> getResults() {
            return results;
        }

        public void setResults(List<ArtistImageResultResponse> results) {
            this.results = results;
        }
    }

}
