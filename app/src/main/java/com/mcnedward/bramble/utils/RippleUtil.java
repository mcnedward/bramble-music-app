package com.mcnedward.bramble.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.mcnedward.bramble.R;

/**
 * Created by Edward on 5/14/2016.
 */
public class RippleUtil {
    private final static String TAG = "RippleUtil";

    /**
     * Creates a new RippleDrawable for a ripple effect on a View.
     *
     * @param rippleColor     The color of the ripple.
     * @param backgroundColor The color of the background for the ripple. If this is 0, then there will be no background and the ripple effect will be circular.
     * @param context         The context.
     * @return A RippleDrawable.
     */
    public static void setRippleBackground(View view, int rippleColor, int backgroundColor, Context context) {
        view.setBackground(getRippleDrawable(rippleColor, backgroundColor, context));
    }

    /**
     * Creates a new RippleDrawable for a ripple effect on a View. This will create a ripple with the default color of FireBrick for the ripple and GhostWhite for the background.
     *
     * @param context The context.
     * @return A RippleDrawable.
     */
    public static void setRippleBackground(View view, Context context) {
        setRippleBackground(view, R.color.FireBrick, R.color.GhostWhite, context);
    }

    public static RippleDrawable getRippleDrawable(Context context) {
        return getRippleDrawable(R.color.FireBrick, R.color.GhostWhite, context);
    }

    public static RippleDrawable getRippleDrawable(int rippleColor, int backgroundColor, Context context) {
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
                backgroundColor == 0 ? null : new ColorDrawable(ContextCompat.getColor(context, backgroundColor)),
                null);
    }

    public static RippleDrawable getRippleDrawable(Context context, Drawable drawable) {
        return getRippleDrawable(context, R.color.FireBrick, drawable);
    }

    public static RippleDrawable getRippleDrawable(Context context, int rippleColor, Drawable drawable) {
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
                drawable,
                null);
    }

}
