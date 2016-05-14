package com.mcnedward.bramble.utils;

import android.content.Context;

import com.squareup.picasso.Picasso;

/**
 * Created by Edward on 5/14/2016.
 */
public class PicassoUtil {

    private static Picasso piccasoInstance = null;

    private PicassoUtil(Context context) {
        piccasoInstance = new Picasso.Builder(context).build();
    }

    public static Picasso getPiccasoInstance(Context context) {
        if (piccasoInstance == null) {
            new PicassoUtil(context);
            return piccasoInstance;
        }
        return piccasoInstance;
    }

}
