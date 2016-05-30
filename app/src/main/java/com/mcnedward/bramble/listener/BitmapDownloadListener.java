package com.mcnedward.bramble.listener;

/**
 * Created by Edward on 5/30/2016.
 *
 * This is a listener for use when a Bitmap has been downloaded.
 */
public interface BitmapDownloadListener<T> {

    void onDownloadComplete(T item);

}
