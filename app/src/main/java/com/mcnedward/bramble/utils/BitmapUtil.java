package com.mcnedward.bramble.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.async.BitmapLoadTask;
import com.mcnedward.bramble.entity.ITitleAndImage;
import com.mcnedward.bramble.view.card.MediaCard;

import java.lang.ref.WeakReference;

/**
 * Created by Edward on 5/15/2016.
 */
public class BitmapUtil {

    public static BitmapLoadTask startBitmapLoadTask(final Context context, ITitleAndImage item, MediaCard mediaCard, LruCache<String, Bitmap> cache) {
        if (cancelPotentialWork(item.getCacheKey(), mediaCard)) {
            final BitmapLoadTask task = new BitmapLoadTask(mediaCard, item, cache, context);
            final AsyncDrawable drawable = new AsyncDrawable(context, task);
            mediaCard.getImageView().setImageDrawable(drawable);
            task.execute();
            return task;
        }
        return null;
    }

    public static boolean cancelPotentialWork(String cacheKey, MediaCard mediaCard) {
        final BitmapLoadTask task = getBitmapLoadTask(mediaCard.getImageView());

        if (task != null) {
            final String taskCacheKey = task.cacheKey;
            if (taskCacheKey.equals("") || !cacheKey.equals(taskCacheKey)) {
                // Cancel the task
                task.cancel(true);
            } else {
                return false;
            }
        }
        return true;
    }

    public static BitmapLoadTask getBitmapLoadTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapLoadTask();
            }
        }
        return null;
    }

    static class AsyncDrawable extends ColorDrawable {
        private final WeakReference<BitmapLoadTask> bitmapLoadTaskWeakReference;

        public AsyncDrawable(Context context, BitmapLoadTask bitmapLoadTask) {
            super(ContextCompat.getColor(context, R.color.Silver));
            bitmapLoadTaskWeakReference = new WeakReference<>(bitmapLoadTask);
        }

        public BitmapLoadTask getBitmapLoadTask() {
            return bitmapLoadTaskWeakReference.get();
        }
    }
}
