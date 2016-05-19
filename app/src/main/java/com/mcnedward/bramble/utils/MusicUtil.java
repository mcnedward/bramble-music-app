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
import com.mcnedward.bramble.activity.AlbumActivity;
import com.mcnedward.bramble.activity.AlbumPopup;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Artist;
import com.mcnedward.bramble.media.Song;
import com.mcnedward.bramble.repository.SongRepository;
import com.mcnedward.bramble.service.MediaService;

import java.io.File;
import java.util.List;

/**
 * Created by edward on 25/12/15.
 */
public class MusicUtil {
    private final static String TAG = "MusicUtil";

    private static SongRepository mSongRepository;

    public static void loadAlbumArt(String albumArtPath, ImageView imageView, Context context) {
        if (albumArtPath != null && !albumArtPath.equals("")) {
            File imageFile = new File(albumArtPath);
            PicassoUtil.getPicasso(context).with(context).load(imageFile).into(imageView);
        } else {
            PicassoUtil.getPicasso(context).with(context).load(R.drawable.no_album_art).into(imageView);
        }
    }

    public static void doPreviousButtonAction(Context context) {
        Song song = MediaCache.getSong(context);
        Album album = MediaCache.getAlbum(context);
        if (song == null || album == null) return;
        List<Integer> albumIds = album.getSongIds();
        if (albumIds.isEmpty()) return;

        SongRepository songRepository = getSongRepository(context);
        Song nextSong = null;
        for (int i = 0; i < albumIds.size(); i++) {
            int id = albumIds.get(i);
            if (song.getId() == id) {
                // Get the previous song, or the last one if the current song is the first
                if (i == 0) {
                    nextSong = songRepository.get(albumIds.get(albumIds.size() - 1));
                } else {
                    nextSong = songRepository.get(i - 1);
                }
            }
        }
        startPlayingMusic(nextSong, album, context);
    }

    public static void doForwardButtonAction(Context context) {
        Song song = MediaCache.getSong(context);
        Album album = MediaCache.getAlbum(context);
        if (song == null || album == null) return;


    }

    public static void doPlayButtonAction(List<ImageView> playButtons, Context context) {
        MediaPlayer player = MediaService.getPlayer();
        if (player == null) {
            Song song = MediaCache.getSong(context);
            if (song != null) {
                MusicUtil.startPlayingMusic(song, context);
                MusicUtil.switchPlayButton(playButtons, false, context);
            }
        } else {
            if (player.isPlaying()) {
                player.pause();
                MusicUtil.switchPlayButton(playButtons, true, context);
            } else {
                player.start();
                MusicUtil.switchPlayButton(playButtons, false, context);
            }
            MediaService.notifySongPlayingListeners();
        }
    }

    public static void switchPlayButton(List<ImageView> playButtons, boolean pause, Context context) {
        if (pause) {
            for (ImageView view : playButtons)
                view.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_play));
        } else {
            for (ImageView view : playButtons)
                view.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_pause));
        }
    }

    public static void startAlbumPopup(Artist artist, Context context) {
        Intent intent = new Intent(context, AlbumPopup.class);
        intent.putExtra("artist", artist);
        context.startActivity(intent);
    }

    public static void openAlbum(Album album, Context context) {
        Intent intent = new Intent(context, AlbumActivity.class);
        intent.putExtra("album", album);
        context.startActivity(intent);
    }

    public static void startPlayingMusic(Song song, Context context) {
        // Start playing music!
        Intent intent = new Intent(context, MediaService.class);
        intent.putExtra("song", song);
        context.startService(intent);
    }

    public static void startPlayingMusic(Song song, Album album, Context context) {
        Intent intent = new Intent(context, MediaService.class);
        intent.putExtra("song", song);
        intent.putExtra("album", album);
        context.startService(intent);
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

    public static SongRepository getSongRepository(Context context) {
        if (mSongRepository == null) {
            mSongRepository = new SongRepository(context);
        }
        return mSongRepository;
    }

}
