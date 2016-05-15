package com.mcnedward.bramble.async;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.mcnedward.bramble.enums.ImageSize;
import com.mcnedward.bramble.media.Media;
import com.mcnedward.bramble.view.MediaCard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by Edward on 5/15/2016.
 * Source: https://techienotes.info/2015/08/28/caching-bitmaps-in-android-using-lrucache/
 */
public class BitmapLoadTask extends AsyncTask<Void, Void, Bitmap> {
    private static final String TAG = "BitmapLoadTask";

    private LruCache<String, Bitmap> cache;
    private Context context;
    public String bitmapPath;
    public String cacheKey;
    private final WeakReference<MediaCard> mediaCardWeakReference;

    public BitmapLoadTask(MediaCard mediaCard, Media media, LruCache<String, Bitmap> cache, Context context) {
        this.cache = cache;
        this.context = context;
        bitmapPath = media.getImagePath();
        cacheKey = media.getCacheKey();
        mediaCardWeakReference = new WeakReference<>(mediaCard);
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Bitmap result = null;
        try {
            result = getBitmapFromMemCache(cacheKey);
            if (result == null) {
                result = getScaledImage(bitmapPath);
                addBitmapToMemCache(cacheKey, result);
            }
        } catch (Exception e) {
            Log.e(TAG, "ERROR", e);
        }
        return result;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }
        if (mediaCardWeakReference != null && bitmap != null) {
            final MediaCard mediaCard = mediaCardWeakReference.get();
            if (mediaCard != null) {
                mediaCard.setImage(bitmap);
            }
        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return cache.get(key);
    }

    private void addBitmapToMemCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            cache.put(key, bitmap);
        }
    }

    /**
     * This function will return the scaled version of original image.
     * Loading original images into thumbnail is wastage of computation
     * and hence we will take put scaled version.
     */
    private Bitmap getScaledImage(String imagePath) {
        Bitmap bitmap = null;
        Uri imageUri = Uri.fromFile(new File(imagePath));
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();

            /**
             * inSampleSize flag if set to a value > 1,
             * requests the decoder to sub-sample the original image,
             * returning a smaller image to save memory.
             * This is a much faster operation as decoder just reads
             * every n-th pixel from given image, and thus
             * providing a smaller scaled image.
             * 'n' is the value set in inSampleSize
             * which would be a power of 2 which is downside
             * of this technique.
             */
            options.inSampleSize = calculateInSampleSize(options, ImageSize.SMALL.size, ImageSize.SMALL.size);
            options.inScaled = true;

            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);

            bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}