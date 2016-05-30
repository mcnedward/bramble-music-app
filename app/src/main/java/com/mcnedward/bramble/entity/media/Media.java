package com.mcnedward.bramble.entity.media;

import android.content.SharedPreferences;

import com.mcnedward.bramble.entity.ITitleAndImage;
import com.mcnedward.bramble.utils.MusicUtil;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by edward on 24/12/15.
 */
public abstract class Media implements Serializable, ITitleAndImage {

    protected long mId;
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
        mCacheKey = MusicUtil.generateCacheKey();
    }

    public boolean save(SharedPreferences.Editor editor) {
        String theMediaType = mMediaType.type();
        editor.putInt(theMediaType + "_id", (int) mId);
        editor.putString(theMediaType + "_imagePath", mImagePath);
        editor.putString(theMediaType + "_title", mTitle);
        editor.putString(theMediaType + "_key", mKey);
        editor.putString(theMediaType + "_mediaType", theMediaType);
        saveMedia(editor);
        return editor.commit();
    }

    protected abstract void saveMedia(SharedPreferences.Editor editor);

    public long getId() {
        return mId;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getImagePath() {
        return mImagePath;
    }

    @Override
    public String getImageUrl() {
        return mImageUrl;
    }

    @Override
    public String getImageThumbnailUrl() {
        return mImageUrl;
    }

    @Override
    public String getCacheKey() {
        return mCacheKey;
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    public void setImagePath(String imagePath) {
        mImagePath = imagePath;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
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

}
