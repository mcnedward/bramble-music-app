package com.mcnedward.bramble.entity.data;

import android.graphics.Bitmap;

import com.mcnedward.bramble.controller.ThumbnailResponse;
import com.mcnedward.bramble.entity.ITitleAndImage;
import com.mcnedward.bramble.utils.MusicUtil;

/**
 * Created by Edward on 5/28/2016.
 */
public class Thumbnail extends Data implements ITitleAndImage {

    private String mTitle;
    private long mArtistImageId;
    private String mMediaUrl;
    private String mContentType;
    private int mWidth;
    private int mHeight;
    private int mFileSize;
    private String mCacheKey;

    public Thumbnail() {
    }

    public Thumbnail(String title, long artistImageId, String mediaUrl, String contentType, int width, int height, int fileSize) {
        mTitle = title;
        mArtistImageId = artistImageId;
        mMediaUrl = mediaUrl;
        mContentType = contentType;
        mWidth = width;
        mHeight = height;
        mFileSize = fileSize;
        mCacheKey = MusicUtil.generateCacheKey();
    }

    public Thumbnail(String title, ThumbnailResponse thumbnail) {
        mTitle = title;
        mMediaUrl = thumbnail.getMediaUrl();
        mContentType = thumbnail.getContentType();
        mWidth = thumbnail.getWidth();
        mHeight = thumbnail.getHeight();
        mFileSize = thumbnail.getFileSize();
        mCacheKey = MusicUtil.generateCacheKey();
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public Bitmap getBitmap() {
        return null;
    }

    @Override
    public String getImagePath() {
        return null;
    }

    @Override
    public String getImageUrl() {
        return mMediaUrl;
    }

    @Override
    public String getImageThumbnailUrl() {
        return mMediaUrl;
    }

    @Override
    public String getCacheKey() {
        return mCacheKey;
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public long getArtistImageId() {
        return mArtistImageId;
    }

    public void setArtistImageId(long artistImageId) {
        mArtistImageId = artistImageId;
    }

    public String getMediaUrl() {
        return mMediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        mMediaUrl = mediaUrl;
    }

    public String getContentType() {
        return mContentType;
    }

    public void setContentType(String contentType) {
        mContentType = contentType;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public int getFileSize() {
        return mFileSize;
    }

    public void setFileSize(int fileSize) {
        mFileSize = fileSize;
    }
}
