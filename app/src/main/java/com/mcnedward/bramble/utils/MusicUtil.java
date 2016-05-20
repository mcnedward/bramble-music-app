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
import com.mcnedward.bramble.repository.AlbumRepository;
import com.mcnedward.bramble.repository.SongRepository;
import com.mcnedward.bramble.service.MediaService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward on 25/12/15.
 */
public class MusicUtil {
    private final static String TAG = "MusicUtil";

    private static SongRepository mSongRepository;
    private static AlbumRepository mAlbumRepository;

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

        // First check if the current song should just be restarted
        // If the player is null, then all songs have stopped, or the app is freshly opened, so the song should not be restarted, but should go to the previous
        MediaPlayer player = MediaService.getPlayer();
        if (player != null) {
            int currentPosition = getTimeInSeconds(player.getCurrentPosition());
            if (currentPosition > 2) {
                startPlayingMusic(song, album, context);
                return;
            }
        }

        SongRepository songRepository = getSongRepository(context);
        List<Song> songs = songRepository.getSongsForAlbum(album.getId());
        if (songs.isEmpty()) return;

        int currentIndex = getSongIndex(songs, song);
        if (currentIndex == -1) return;
        Song nextSong;
        // Get the previous song, or the last one if the current song is the first
        if (currentIndex == 0) {
            nextSong = songs.get(songs.size() - 1);
        } else {
            nextSong = songs.get(currentIndex - 1);
        }
        startPlayingMusic(nextSong, album, context);
    }

    public static void doForwardButtonAction(Context context) {
        Album album = MediaCache.getAlbum(context);
        Song nextSong = getNextSongForAlbum(album, context);
        if (nextSong == null) return;
        startPlayingMusic(nextSong, album, context);
    }

    public static Song getNextSongForAlbum(Album album, Context context) {
        Song song = MediaCache.getSong(context);
        if (song == null || album == null) return null;
        SongRepository songRepository = getSongRepository(context);
        List<Song> songs = songRepository.getSongsForAlbum(album.getId());
        if (songs.isEmpty()) return null;

        int currentIndex = getSongIndex(songs, song);
        if (currentIndex == -1) return null;
        Song nextSong;
        if (currentIndex == songs.size() - 1) {
            nextSong = songs.get(0);
        } else {
            nextSong = songs.get(currentIndex + 1);
        }
        return nextSong;
    }

    private static int getSongIndex(List<Song> songs, Song song) {
        int currentIndex = -1;
        for (int i = 0; i < songs.size(); i++) {
            Song albumSong = songs.get(i);
            if (albumSong.getId() == song.getId()) {
                currentIndex = i;
                break;
            }
        }
        return currentIndex;
    }

    public static void doPlayButtonAction(ImageView playButton, Context context) {
        MediaPlayer player = MediaService.getPlayer();
        if (player == null) {
            Song song = MediaCache.getSong(context);
            if (song != null) {
                MusicUtil.startPlayingMusic(song, context);
            }
        } else {
            if (player.isPlaying()) {
                player.pause();
                MusicUtil.switchPlayButton(playButton, false, context);
            } else {
                player.start();
                MusicUtil.switchPlayButton(playButton, true, context);
            }
            if (MediaService.isStopped()) {
                MediaService.pauseNowPlayingThread(false);
            }
            MediaService.notifyMediaChangeListeners();
        }
    }

    /**
     * Switches a play button's image. If you want to show the pause image, pass true. If you want to show the play image, pass false.
     * @param playButton The play button to update.
     * @param isPlaying True for the pause image, false for the play image.
     * @param context The context.
     */
    public static void switchPlayButton(ImageView playButton, boolean isPlaying, Context context) {
        if (isPlaying) {
            playButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_pause));
        } else {
            playButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_play));
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
        int seconds = getTimeInSeconds(millis);
        return String.format("%d:%02d", minutes, seconds);
    }

    public static int getTimeInSeconds(long millis) {
        return (int) ((millis / 1000) % 60);
    }

    public static SongRepository getSongRepository(Context context) {
        if (mSongRepository == null) {
            mSongRepository = new SongRepository(context);
        }
        return mSongRepository;
    }

    public static AlbumRepository getAlbumRepository(Context context) {
        if (mAlbumRepository == null) {
            mAlbumRepository = new AlbumRepository(context);
        }
        return mAlbumRepository;
    }

}
