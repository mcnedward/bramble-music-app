package com.mcnedward.bramble.entity.media;

import java.io.Serializable;

/**
 * Created by edward on 24/12/15.
 */
public enum MediaType implements Serializable {

    ARTIST ("Artist"),
    ALBUM ("Album"),
    SONG ("Song"),
    GENRE ("Genre");

    private String type;

    MediaType(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }
}
