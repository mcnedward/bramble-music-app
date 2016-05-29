package com.mcnedward.bramble.enums;

/**
 * Created by Edward on 5/29/2016.
 */
public enum ArtistImageChooserType {
    THUMBNAIL("Thumbnail"),
    BACKGROUND("Background");
    String mType;

    ArtistImageChooserType(String type) {
        mType = type;
    }

    public String type() {
        return mType;
    }
}
