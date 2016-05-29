package com.mcnedward.bramble.entity.data;

import com.mcnedward.bramble.controller.ThumbnailResponse;

/**
 * Created by Edward on 5/28/2016.
 */
public class Thumbnail extends Data {

    protected long mArtistImageId;
    protected String mMediaUrl;
    protected String mContentType;
    protected int mWidth;
    protected int mHeight;
    protected int mFileSize;

    public Thumbnail() { }

    public Thumbnail(long artistImageId, String mediaUrl, String contentType, int width, int height, int fileSize) {
        mArtistImageId = artistImageId;
        mMediaUrl = mediaUrl;
        mContentType = contentType;
        mWidth = width;
        mHeight = height;
        mFileSize = fileSize;
    }

    public Thumbnail(ThumbnailResponse thumbnail) {
        mMediaUrl = thumbnail.getMediaUrl();
        mContentType = thumbnail.getContentType();
        mWidth = thumbnail.getWidth();
        mHeight = thumbnail.getHeight();
        mFileSize = thumbnail.getFileSize();
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
