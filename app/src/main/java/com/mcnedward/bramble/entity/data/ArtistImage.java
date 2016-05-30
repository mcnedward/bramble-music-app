package com.mcnedward.bramble.entity.data;

import android.graphics.Bitmap;

import com.mcnedward.bramble.controller.ArtistImageResultResponse;
import com.mcnedward.bramble.entity.ITitleAndImage;
import com.mcnedward.bramble.utils.BitmapUtil;
import com.mcnedward.bramble.utils.MusicUtil;

/**
 * Created by Edward on 5/28/2016.
 */
public class ArtistImage extends Data implements ITitleAndImage {

    private long mArtistId;
    private String mTitle;
    private String mMediaUrl;
    private String mSourceUrl;
    private String mDisplayUrl;
    private int mWidth;
    private int mHeight;
    private int mFileSize;
    private String mContentType;
    private String mBitmapPath;
    private boolean mSelectedImage;
    private Thumbnail mThumbnail;
    private byte[] mBitmapBytes;
    private String mCacheKey;

    public ArtistImage() {
        mCacheKey = MusicUtil.generateCacheKey();
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
        mThumbnail = new Thumbnail(mTitle, response.getThumbnail());
        mCacheKey = MusicUtil.generateCacheKey();
    }

    public Bitmap getBitmap() {
        if (mBitmapBytes == null) return null;
        return BitmapUtil.fromBytes(mBitmapBytes);
    }

    @Override
    public String getTitle() {
        return mTitle;
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
        if (mThumbnail == null || mThumbnail.getMediaUrl() == null || mThumbnail.getMediaUrl().equals(""))
            return mMediaUrl;
        return mThumbnail.getMediaUrl();
    }

    @Override
    public String getCacheKey() {
        return mCacheKey;
    }

    @Override
    public boolean isSelected() {
        return mSelectedImage;
    }

    public String getMediaUrl() {
        return mMediaUrl;
    }

    public long getArtistId() {
        return mArtistId;
    }

    public void setArtistId(long artistId) {
        mArtistId = artistId;
    }

    public void setMediaUrl(String mediaUrl) {
        mMediaUrl = mediaUrl;
    }

    public void setTitle(String title) {
        mTitle = title;
        if (mThumbnail == null) return;
        mThumbnail.setTitle(title);
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

    public String getBitmapPath() {
        return mBitmapPath;
    }

    public void setBitmapPath(String bitmapPath) {
        mBitmapPath = bitmapPath;
    }

    public boolean isSelectedImage() {
        return mSelectedImage;
    }

    public void setSelectedImage(boolean selectedImage) {
        mSelectedImage = selectedImage;
    }

    public Thumbnail getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        mThumbnail = thumbnail;
    }

    public byte[] getBitmapBytes() {
        return mBitmapBytes;
    }

    public void setBitmapBytes(byte[] bitmapBytes) {
        mBitmapBytes = bitmapBytes;
    }

    @Override
    public String toString() {
        return mTitle;
    }
}
