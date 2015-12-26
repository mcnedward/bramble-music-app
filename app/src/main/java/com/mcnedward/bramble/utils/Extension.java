package com.mcnedward.bramble.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.RippleDrawable;
import android.support.v4.content.ContextCompat;

import com.mcnedward.bramble.R;

/**
 * Created by edward on 25/12/15.
 */
public class Extension {

    public static RippleDrawable rippleDrawable(int rippleColor, int backgroundColor, Context context) {
        return new RippleDrawable(
                new ColorStateList(
                        new int[][]
                                {
                                        new int[]{android.R.attr.state_window_focused},
                                },
                        new int[]
                                {
                                        ContextCompat.getColor(context, rippleColor),
                                }),
                new ColorDrawable(ContextCompat.getColor(context, backgroundColor)),
                null);
    }

    public static RippleDrawable rippleDrawable(Context context) {
        return rippleDrawable(R.color.FireBrick, R.color.GhostWhite, context);
    }

}
