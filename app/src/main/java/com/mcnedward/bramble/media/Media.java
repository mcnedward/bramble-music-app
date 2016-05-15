package com.mcnedward.bramble.media;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by edward on 24/12/15.
 */
public abstract class Media implements Serializable {

    protected int id;
    protected String imagePath;
    protected String title;
    protected String key;
    protected MediaType mediaType;
    private String cacheKey;

    public Media(int id, String imagePath, String title, String key, MediaType mediaType) {
        this.id = id;
        this.imagePath = imagePath;
        this.title = title;
        this.key = key;
        this.mediaType = mediaType;
        cacheKey = generateCacheKey();
    }

    /**
     * Source: http://stackoverflow.com/questions/20536566/creating-a-random-string-with-a-z-and-0-9-in-java
     * @return
     */
    private String generateCacheKey() {
        String SALT_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) {
            int index = (int) (rnd.nextFloat() * SALT_CHARS.length());
            salt.append(SALT_CHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr.toLowerCase();

    }

    public int getId() {
        return id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getCacheKey() {
        return cacheKey;
    }
}
