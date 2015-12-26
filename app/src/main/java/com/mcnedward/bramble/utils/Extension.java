package com.mcnedward.bramble.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.RippleDrawable;
import android.support.v4.content.ContextCompat;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.activity.AlbumPopup;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Artist;
import com.mcnedward.bramble.media.Song;
import com.mcnedward.bramble.activity.NowPlayingActivity;

/**
 * Created by edward on 25/12/15.
 */
public class Extension {

    /**
     * Creates a new RippleDrawable for a ripple effect on a View.
     * @param rippleColor The color of the ripple.
     * @param backgroundColor The color of the background for the ripple. If this is 0, then there will be no background and the ripple effect will be circular.
     * @param context The context.
     * @return A RippleDrawable.
     */
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
                backgroundColor == 0 ? null : new ColorDrawable(ContextCompat.getColor(context, backgroundColor)),
                null);
    }

    /**
     * Creates a new RippleDrawable for a ripple effect on a View. This will create a ripple with the default color of FireBrick for the ripple and GhostWhite for the background.
     * @param context The context.
     * @return A RippleDrawable.
     */
    public static RippleDrawable rippleDrawable(Context context) {
        return rippleDrawable(R.color.FireBrick, R.color.GhostWhite, context);
    }

    public static void startAlbumPopup(Artist artist, Activity activity) {
        Intent intent = new Intent(activity, AlbumPopup.class);
        intent.putExtra("artist", artist);
        activity.startActivity(intent);
    }

    public static void startNowPlayingActivity(Song song, Activity activity) {
        Intent intent = new Intent(activity, NowPlayingActivity.class);
        intent.putExtra("song", song);
        activity.startActivity(intent);
    }

}
