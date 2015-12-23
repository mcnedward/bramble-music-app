package com.mcnedward.bramble.media;

import java.io.Serializable;

/**
 * Created by edward on 22/12/15.
 */
public class Album implements Serializable {

    private String albumName;

    public Album(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    @Override
    public String toString() {
        return albumName;
    }
}
