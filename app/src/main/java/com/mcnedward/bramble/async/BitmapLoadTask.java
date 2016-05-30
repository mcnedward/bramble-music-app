package com.mcnedward.bramble.async;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.mcnedward.bramble.entity.ITitleAndImage;
import com.mcnedward.bramble.enums.ImageSize;
import com.mcnedward.bramble.view.card.MediaCard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by Edward on 5/15/2016.
 * Source: https://techienotes.info/2015/08/28/caching-bitmaps-in-android-using-lrucache/
 */
public class BitmapLoadTask extends BitmapTask<Void, Void, Bitmap> {
    private static final String TAG = "BitmapLoadTask";

    public String mCacheKey;
    private LruCache<String, Bitmap> mCache;
    private ITitleAndImage mMedia;
    private String mBitmapPath;
    private final WeakReference<MediaCard> mMediaCardWeakReference;

    public BitmapLoadTask(Context context, MediaCard mediaCard, ITitleAndImage media, LruCache<String, Bitmap> cache) {
        super(context);
        mCache = cache;
        mMedia = media;
        mBitmapPath = media.getImagePath();
        mCacheKey = media.getCacheKey();
        mMediaCardWeakReference = new WeakReference<>(mediaCard);
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Bitmap result = null;
        try {
         result = mMedia.getBitmap() != null ?
                mMedia.getBitmap() :
                getBitmapFromMemCache(mCacheKey);
            if (result == null) {
                result = getScaledImage(mBitmapPath);
                addBitmapToMemCache(mCacheKey, result);
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
        if (mMediaCardWeakReference != null) {
            final MediaCard mediaCard = mMediaCardWeakReference.get();
            if (mediaCard != null) {
                mediaCard.updateAfterBitmapLoadTaskFinished(mMedia, bitmap);
            }
        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return mCache.get(key);
    }

    private void addBitmapToMemCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mCache.put(key, bitmap);
        }
    }

}