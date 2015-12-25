package com.mcnedward.bramble.media;

import java.io.Serializable;

/**
 * Created by edward on 24/12/15.
 */
public abstract class Media implements Serializable {

    protected int id;
    protected String key;
    protected MediaType mediaType;

    public Media(int id, String key, MediaType mediaType) {
        this.id = id;
        this.key = key;
        this.mediaType = mediaType;
    }

    public int getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public MediaType getMediaType() {
        return mediaType;
    }
}
