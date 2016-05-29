package com.mcnedward.bramble.entity.data;

import com.mcnedward.bramble.controller.ArtistImageResultResponse;

/**
 * Created by Edward on 5/28/2016.
 */
public class ArtistImage extends Data {

    private int mArtistId;
    private String mTitle;
    private String mMediaUrl;
    private String mSourceUrl;
    private String mDisplayUrl;
    private int mWidth;
    private int mHeight;
    private int mFileSize;
    private String mContentType;
    private Thumbnail mThumbnail;
    private String mBitmapPath;

    public ArtistImage() {

    }

    public ArtistImage(ArtistImageResultResponse response) {
        mTitle = response.getTitle();
        mMediaUrl = response.getMediaUrl();
        mSourceUrl = response.getSourceUrl();
        mDisplayUrl = response.getDisplayUrl();
        mWidth = response.getWidth();
        mHeight = response.getHeight();
        mFileSize = response.getFileSize();
        mContentType = response.getContentType();
        mThumbnail = new Thumbnail(response.getThumbnail());
    }

    public String getMediaUrl() {
        return mMediaUrl;
    }

    public int getArtistId() {
        return mArtistId;
    }

    public void setArtistId(int artistId) {
        mArtistId = artistId;
    }

    public void setMediaUrl(String mediaUrl) {
        mMediaUrl = mediaUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getSourceUrl() {
        return mSourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        mSourceUrl = sourceUrl;
    }

    public String getDisplayUrl() {
        return mDisplayUrl;
    }

    public void setDisplayUrl(String displayUrl) {
        mDisplayUrl = displayUrl;
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

    public String getContentType() {
        return mContentType;
    }

    public void setContentType(String contentType) {
        mContentType = contentType;
    }

    public Thumbnail getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        mThumbnail = thumbnail;
    }

    public String getBitmapPath() {
        return mBitmapPath;
    }

    public void setBitmapPath(String bitmapPath) {
        mBitmapPath = bitmapPath;
    }

    @Override
    public String toString() {
        return mTitle;
    }
}
