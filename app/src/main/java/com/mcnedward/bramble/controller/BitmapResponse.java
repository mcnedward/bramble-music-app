package com.mcnedward.bramble.controller;

import android.graphics.Bitmap;

import com.mcnedward.bramble.entity.data.ArtistImage;
import com.mcnedward.bramble.listener.BitmapDownloadListener;

import java.io.Serializable;

/**
 * Created by Edward on 5/28/2016.
 *
 * Response container for bitmap downloads.
 */
public class BitmapResponse implements Serializable {

    Bitmap bitmap;
    ArtistImage artistImage;
    BitmapDownloadListener<ArtistImage> listener;

    public BitmapResponse(Bitmap bitmap, ArtistImage artistImage, BitmapDownloadListener<ArtistImage> listener) {
        this.bitmap = bitmap;
        this.artistImage = artistImage;
        this.listener = listener;
    }

}
