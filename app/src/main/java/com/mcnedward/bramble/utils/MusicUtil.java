package com.mcnedward.bramble.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.activity.AlbumPopup;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Artist;
import com.mcnedward.bramble.media.Song;
import com.mcnedward.bramble.service.MediaService;

import java.io.File;
import java.util.List;

/**
 * Created by edward on 25/12/15.
 */
public class MusicUtil {
    private final static String TAG = "MusicUtil";

    public static void loadAlbumArt(String albumArtPath, ImageView imageView, Context context) {
        if (albumArtPath != null && !albumArtPath.equals("")) {
            File imageFile = new File(albumArtPath);
            PicassoUtil.getPicasso(context).with(context).load(imageFile).into(imageView);
        } else {
            PicassoUtil.getPicasso(context).with(context).load(R.drawable.no_album_art).into(imageView);
        }
    }

    public static void setPlayButtonListener(List<ImageView> playButtons, MediaPlayer player, Context context) {
        if (player != null) {
            if (player.isPlaying()) {
                player.pause();
                MusicUtil.switchPlayButton(playButtons, true, context);
            } else {
                player.start();
                MusicUtil.switchPlayButton(playButtons, false, context);
            }
        }
    }

    public static void switchPlayButton(List<ImageView> playButtons, boolean pause, Context context) {
        if (pause) {
            for (ImageView view : playButtons)
                view.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_play_large));
        } else {
            for (ImageView view : playButtons)
                view.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_pause));
        }
    }

    // TODO What's going on here?
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

    /**
     * Used to format the time of the current media
     *
     * @param millis - The time in milliseconds to format
     * @return - The formatted time
     */
    public static String getTimeString(long millis) {
        int minutes = (int) (millis / (1000 * 60));
        int seconds = (int) ((millis / 1000) % 60);
        return String.format("%d:%02d", minutes, seconds);
    }

}
