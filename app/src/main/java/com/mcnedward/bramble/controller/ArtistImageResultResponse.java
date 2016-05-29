package com.mcnedward.bramble.controller;

/**
 * Created by Edward on 5/28/2016.
 * A result returned from a Bing search.
 */
public class ArtistImageResultResponse {

    String ID;
    String Title;
    String MediaUrl;
    String SourceUrl;
    String DisplayUrl;
    Integer Width;
    Integer Height;
    Integer FileSize;
    String ContentType;
    ThumbnailResponse Thumbnail;
    Metadata __metadata;

    public ArtistImageResultResponse(String ID, String title, String mediaUrl, String sourceUrl, String displayUrl, Integer width, Integer height,
                                     Integer fileSize, String contentType, ThumbnailResponse thumbnail, Metadata metadata) {
        this.ID = ID;
        Title = title;
        MediaUrl = mediaUrl;
        SourceUrl = sourceUrl;
        DisplayUrl = displayUrl;
        Width = width;
        Height = height;
        FileSize = fileSize;
        ContentType = contentType;
        Thumbnail = thumbnail;
        __metadata = metadata;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMediaUrl() {
        return MediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        MediaUrl = mediaUrl;
    }

    public String getSourceUrl() {
        return SourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        SourceUrl = sourceUrl;
    }

    public String getDisplayUrl() {
        return DisplayUrl;
    }

    public void setDisplayUrl(String displayUrl) {
        DisplayUrl = displayUrl;
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

    public String getContentType() {
        return ContentType;
    }

    public void setContentType(String contentType) {
        ContentType = contentType;
    }

    public ThumbnailResponse getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(ThumbnailResponse thumbnail) {
        Thumbnail = thumbnail;
    }

    public Metadata get__metadata() {
        return __metadata;
    }

    public void set__metadata(Metadata __metadata) {
        this.__metadata = __metadata;
    }

    final class Metadata {
        String uri;
        String type;

        public Metadata(String uri, String type) {
            this.uri = uri;
            this.type = type;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
