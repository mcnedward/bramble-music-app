package com.mcnedward.bramble.enums;

/**
 * Created by Edward on 3/18/2016.
 */
public enum ImageSize {

    SMALL(64),
    MEDIUM(128),
    LARGE(256);

    public int size;

    ImageSize(int size) {
        this.size = size;
    }

}
