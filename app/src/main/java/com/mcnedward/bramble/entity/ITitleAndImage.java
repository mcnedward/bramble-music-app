package com.mcnedward.bramble.entity;

import android.graphics.Bitmap;

/**
 * Created by Edward on 5/29/2016.
 */
public interface ITitleAndImage {

    long getId();

    String getTitle();

    Bitmap getBitmap();

    String getImagePath();

    String getImageUrl();

    String getImageThumbnailUrl();

    String getCacheKey();

    boolean isSelected();

}
