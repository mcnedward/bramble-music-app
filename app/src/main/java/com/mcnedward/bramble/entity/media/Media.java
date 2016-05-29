package com.mcnedward.bramble.entity.media;

import android.content.SharedPreferences;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by edward on 24/12/15.
 */
public abstract class Media implements Serializable {

    protected int mId;
    protected String mImagePath;
    protected String mImageUrl;
    protected String mTitle;
    protected String mKey;
    protected MediaType mMediaType;
    private String mCacheKey;

    public Media(int id, String imagePath, String title, String key, MediaType mediaType) {
        mId = id;
        mImagePath = imagePath;
        mTitle = title;
        mKey = key;
        mMediaType = mediaType;
        mCacheKey = generateCacheKey();
    }

    public boolean save(SharedPreferences.Editor editor) {
        String theMediaType = mMediaType.type();
        editor.putInt(theMediaType + "_id", mId);
        editor.putString(theMediaType + "_imagePath", mImagePath);
        editor.putString(theMediaType + "_title", mTitle);
        editor.putString(theMediaType + "_key", mKey);
        editor.putString(theMediaType + "_mediaType", theMediaType);
        saveMedia(editor);
        return editor.commit();
    }

    protected abstract void saveMedia(SharedPreferences.Editor editor);

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
        return mId;
    }

    public String getImagePath() {
        return mImagePath;
    }

    public void setImagePath(String imagePath) {
        mImagePath = imagePath;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public MediaType getMediaType() {
        return mMediaType;
    }

    public String getCacheKey() {
        return mCacheKey;
    }
}
