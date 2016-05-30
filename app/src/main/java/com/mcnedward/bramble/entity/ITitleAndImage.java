package com.mcnedward.bramble.entity;

/**
 * Created by Edward on 5/29/2016.
 */
public interface ITitleAndImage {

    long getId();
    String getTitle();
    String getImagePath();
    String getImageUrl();
    String getImageThumbnailUrl();
    String getCacheKey();

}
