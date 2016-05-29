package com.mcnedward.bramble.controller;

/**
 * Created by Edward on 5/28/2016.
 */
public class ThumbnailResponse {

    String MediaUrl;
    String ContentType;
    Integer Width;
    Integer Height;
    Integer FileSize;

    public ThumbnailResponse(String mediaUrl, String contentType, Integer width, Integer height, Integer fileSize) {
        MediaUrl = mediaUrl;
        ContentType = contentType;
        Width = width;
        Height = height;
        FileSize = fileSize;
    }

    public String getMediaUrl() {
        return MediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        MediaUrl = mediaUrl;
    }

    public String getContentType() {
        return ContentType;
    }

    public void setContentType(String contentType) {
        ContentType = contentType;
    }

    public Integer getWidth() {
        return Width;
    }

    public void setWidth(Integer width) {
        Width = width;
    }

    public Integer getHeight() {
        return Height;
    }

    public void setHeight(Integer height) {
        Height = height;
    }

    public Integer getFileSize() {
        return FileSize;
    }

    public void setFileSize(Integer fileSize) {
        FileSize = fileSize;
    }
}
