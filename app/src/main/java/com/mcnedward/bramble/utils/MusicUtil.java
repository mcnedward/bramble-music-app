package com.mcnedward.bramble.utils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
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
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by edward on 25/12/15.
 */
public class MusicUtil {
    private final static String TAG = "MusicUtil";

    public static void loadAlbumArt(Context context, String albumArtPath, ImageView imageView) {
        if (albumArtPath != null && !albumArtPath.equals("")) {
            File imageFile = new File(albumArtPath);
            PicassoUtil.getPicasso(context).with(context).load(imageFile).into(imageView);
        } else {
            PicassoUtil.getPicasso(context).with(context).load(R.drawable.no_album_art).into(imageView);
        }
    }

    public static void doPreviousButtonAction(Context context) {
        Song song = MediaCache.getSong(context);
        List<String> songList = MediaCache.getSongList(context);
        if (song == null) return;

        // First check if the current song should just be restarted
        // If the player is null, then all songs have stopped, or the app is freshly opened, so the song should not be restarted, but should go to the previous
        MediaPlayer player = MediaService.getPlayer();
        if (player != null) {
            int currentPosition = getTimeInSeconds(player.getCurrentPosition());
            if (currentPosition > 2) {
                startPlayingMusic(context, song, songList);
                return;
            }
        }

        if (songList.isEmpty()) return; // TODO Restart song if there are no other songs in list? Or should there always be at least one song in the list? Yes there should.

        int currentIndex = getSongIndex(songList, song);
        if (currentIndex == -1) return;
        String nextSongKey;
        // Get the previous song, or the last one if the current song is the first
        if (currentIndex == 0) {
            nextSongKey = songList.get(songList.size() - 1);
        } else {
            nextSongKey = songList.get(currentIndex - 1);
        }
        Song nextSong = RepositoryUtil.getSongRepository(context).get(nextSongKey);
        startPlayingMusic(context, nextSong);
    }

    public static void doForwardButtonAction(Context context) {
        Song song = MediaCache.getSong(context);
        List<String> songList = MediaCache.getSongList(context);
        if (song == null) return;

        if (songList.isEmpty()) return; // TODO Restart song if there are no other songs in list? Or should there always be at least one song in the list? Yes there should.

        int currentIndex = getSongIndex(songList, song);
        if (currentIndex == -1) return;
        String nextSongKey;
        // Get the next song, or the first if the current song is the last
        if (currentIndex == songList.size() - 1) {
            nextSongKey = songList.get(0);
        } else {
            nextSongKey = songList.get(currentIndex + 1);
        }
        Song nextSong = RepositoryUtil.getSongRepository(context).get(nextSongKey);
        startPlayingMusic(context, nextSong);
    }

    private static int getSongIndex(List<String> songList, Song song) {
        int currentIndex = -1;
        for (int i = 0; i < songList.size(); i++) {
            String songKey = songList.get(i);
            if (song.getKey().equals(songKey)) {
                currentIndex = i;
                break;
            }
        }
        return currentIndex;
    }

    public static void doPlayButtonAction(Context context, ImageView playButton) {
        MediaPlayer player = MediaService.getPlayer();
        if (player == null) {
            Song song = MediaCache.getSong(context);
            if (song != null) {
                MusicUtil.startPlayingMusic(context, song);
            }
        } else {
            if (player.isPlaying()) {
                player.pause();
                MusicUtil.switchPlayButton(context, playButton, false);
            } else {
                player.start();
                MusicUtil.switchPlayButton(context, playButton, true);
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
    public static void switchPlayButton(Context context, ImageView playButton, boolean isPlaying) {
        if (isPlaying) {
            playButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_pause));
        } else {
            playButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_play));
        }
    }

    public static void startAlbumPopup(Context context, Artist artist) {
        AlbumPopup.startAlbumPopup(context, artist);
//        Intent intent = new Intent(context, AlbumPopup.class);
//        intent.putExtra("artist", artist);
//        context.startActivity(intent);
    }

    public static void openAlbum(Context context, Album album) {
        Intent intent = new Intent(context, AlbumActivity.class);
        intent.putExtra("album", album);
        context.startActivity(intent);
    }

    public static void startPlayingMusic(Context context, Song song) {
        // Start playing music!
        Intent intent = new Intent(context, MediaService.class);
        intent.putExtra("song", song);
        context.startService(intent);
    }

    public static void startPlayingMusic(Context context, Song song, List<String> songList) {
        Intent intent = new Intent(context, MediaService.class);
        intent.putExtra("song", song);
        intent.putExtra("songList", new ArrayList<>(songList));
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

}
