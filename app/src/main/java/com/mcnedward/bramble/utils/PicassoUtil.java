package com.mcnedward.bramble.utils;

import android.content.Context;

import com.squareup.picasso.Picasso;

/**
 * Created by Edward on 5/15/2016.
 */
public class PicassoUtil {

    private static Picasso picasso;

    private PicassoUtil(Context context) {
        picasso = new Picasso.Builder(context).build();
    }

    public static Picasso getPicasso(Context context) {
        if (picasso == null) {
            new PicassoUtil(context);
        }
        return picasso.with(context);
    }

}
