package com.mcnedward.bramble.async;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.mcnedward.bramble.enums.ImageSize;
import com.mcnedward.bramble.view.card.MediaCard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Edward on 5/15/2016.
 */
public abstract class BitmapTask<T1, T2, T3> extends AsyncTask<T1, T2, T3> {
    private static final String TAG = "BitmapTask";

    protected Context mContext;

    public BitmapTask(Context context) {
        mContext = context;
    }

    /**
     * Thanks to: http://stackoverflow.com/questions/18210700/best-method-to-download-image-from-url-in-android
     * @param bm
     * @param newHeight
     * @param newWidth
     * @return
     */
    protected Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }

    /**
     * This function will return the scaled version of original image.
     * Loading original images into thumbnail is wastage of computation
     * and hence we will take scaled version.
     */
    protected Bitmap getScaledImage(String imagePath) {
        Bitmap bitmap = null;
        Uri imageUri = Uri.fromFile(new File(imagePath));
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = calculateInSampleSize(options, ImageSize.SMALL.size, ImageSize.SMALL.size);
            options.inScaled = true;

            InputStream inputStream = mContext.getContentResolver().openInputStream(imageUri);

            bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    protected static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
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