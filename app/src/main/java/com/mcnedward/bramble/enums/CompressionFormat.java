package com.mcnedward.bramble.enums;

import android.graphics.Bitmap;

/**
 * Created by Edward on 3/18/2016.
 */
public enum CompressionFormat {

    JPEG(Bitmap.CompressFormat.JPEG),
    PNG(Bitmap.CompressFormat.PNG),
    WEBP(Bitmap.CompressFormat.WEBP);

    public Bitmap.CompressFormat format;

    CompressionFormat(Bitmap.CompressFormat format) {
        this.format = format;
    }

    public static CompressionFormat getCompressionFormat(String formatType) {
        if (formatType.toLowerCase().contains("jpeg")) {
            return JPEG;
        }
        if (formatType.toLowerCase().contains("png")) {
            return PNG;
        }
        if (formatType.toLowerCase().contains("webp")) {
            return WEBP;
        }
        return JPEG;
    }

}
