package com.mcnedward.bramble.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.activity.AlbumPopup;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Artist;
import com.mcnedward.bramble.media.Song;
import com.mcnedward.bramble.service.MediaService;

import java.io.File;

/**
 * Created by edward on 25/12/15.
 */
public class Extension {
    private final static String TAG = "Extension";

    /**
     * Creates a new RippleDrawable for a ripple effect on a View.
     *
     * @param rippleColor     The color of the ripple.
     * @param backgroundColor The color of the background for the ripple. If this is 0, then there will be no background and the ripple effect will be circular.
     * @param context         The context.
     * @return A RippleDrawable.
     */
    public static void setRippleBackground(View view, int rippleColor, int backgroundColor, Context context) {
        view.setBackground(new RippleDrawable(
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
                null));
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

    public static void updateAlbumArt(Album album, ImageView imageView) {
        String albumArt = album.getAlbumArt();
        if (albumArt != null) {
            // Create the album art bitmap and scale it to fit properly and avoid over using memory
            File imageFile = new File(album.getAlbumArt());
            Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            imageView.setImageBitmap(imageBitmap);
        }
    }

    public static void startAlbumPopup(final Artist artist, final Activity activity) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(activity, AlbumPopup.class);
                intent.putExtra("artist", artist);
                activity.startActivity(intent);
            }
        }, 500);
    }

    public static void startPlayingMusic(final Song song, final Activity activity) {
        // Start playing music!
        Log.d(TAG, String.format("Starting to play '%s' from %s!", song, TAG));
        Intent intent = new Intent(activity, MediaService.class);
        intent.putExtra("song", song);
        activity.startService(intent);
    }

}
