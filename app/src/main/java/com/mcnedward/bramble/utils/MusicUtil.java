package com.mcnedward.bramble.utils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.activity.AlbumActivity;
import com.mcnedward.bramble.activity.AlbumPopup;
import com.mcnedward.bramble.activity.artist.ArtistActivity;
import com.mcnedward.bramble.activity.artist.ArtistImageChooserActivity;
import com.mcnedward.bramble.entity.media.Album;
import com.mcnedward.bramble.entity.media.Artist;
import com.mcnedward.bramble.entity.media.Media;
import com.mcnedward.bramble.entity.media.Song;
import com.mcnedward.bramble.enums.IntentKey;
import com.mcnedward.bramble.exception.EntityDoesNotExistException;
import com.mcnedward.bramble.service.MediaService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    /**
     * Starts the previous song in the queue. This can also check if the song should be just be restarted instead of skipped back.
     *
     * @param checkTime True if this should check if a few seconds have passed, and the song should just be restarted. False if the song should
     *                  just be restarted right.
     */
    public static void doPreviousButtonAction(Context context, boolean checkTime) {
        // If the player is null, then all songs have stopped, or the app is freshly opened, so the song should not be restarted, but should go
        // to the previous
        MediaPlayer player = MediaService.getPlayer();
        if (player == null) {
            // Need to start up the Service
            Song song = MediaCache.getSong(context);
            List<Long> queue = MediaCache.getQueue(context);
            try {
                Song previousSong = getPreviousSongFromIds(context, song, queue);
                startPlayingMusic(context, previousSong, queue);
            } catch (EntityDoesNotExistException e) {
                Log.w(TAG, e.getMessage());
            }
            return;
        }
        if (checkTime) {
            // First check if the current song should just be restarted
            if (player != null) {
                int currentPosition = getTimeInSeconds(player.getCurrentPosition());
                if (currentPosition > 2) {
                    player.seekTo(0);
                    return;
                }
            }
        }
        MediaService.startPreviousSongInQueue();
    }

    /**
     * Checks if the song should be restarted, or skipped back, then does the appropriate action.
     */
    public static void doPreviousButtonAction(Context context) {
        doPreviousButtonAction(context, true);
    }

    public static void doForwardButtonAction(Context context) {
        // If the player is null, then all songs have stopped, or the app is freshly opened, so the song should not be restarted, but should go
        // to the previous
        MediaPlayer player = MediaService.getPlayer();
        if (player == null) {
            // Need to start up the Service
            Song song = MediaCache.getSong(context);
            List<Long> queue = MediaCache.getQueue(context);
            try {
                Song nextSong = getNextSongFromIds(context, song, queue);
                startPlayingMusic(context, nextSong, queue);
            } catch (EntityDoesNotExistException e) {
                Log.w(TAG, e.getMessage());
            }
            return;
        }
        MediaService.startNextSongInQueue();
    }

    public static Song getNextSongFromIds(Context context, Song currentSong, List<Long> songIds) throws EntityDoesNotExistException {
        int currentIndex = getSongIndexFromIds(songIds, currentSong);
        if (currentIndex == -1) return null;
        Long nextSongId;
        // Get the next song, or the first if the current song is the last
        if (currentIndex == songIds.size() - 1) {
            nextSongId = songIds.get(0);
        } else {
            nextSongId = songIds.get(currentIndex + 1);
        }
        return RepositoryUtil.getSongRepository(context).get(nextSongId);
    }

    public static Song getPreviousSongFromIds(Context context, Song currentSong, List<Long> songKeys) throws EntityDoesNotExistException {
        int currentIndex = getSongIndexFromIds(songKeys, currentSong);
        if (currentIndex == -1) return null;
        Long nextSongId;
        // Get the previous song, or the last one if the current song is the first
        if (currentIndex == 0) {
            nextSongId = songKeys.get(songKeys.size() - 1);
        } else {
            nextSongId = songKeys.get(currentIndex - 1);
        }
        return RepositoryUtil.getSongRepository(context).get(nextSongId);
    }

    public static int getSongIndexFromIds(List<Long> songIds, Song song) {
        int currentIndex = -1;
        for (int i = 0; i < songIds.size(); i++) {
            Long songId = songIds.get(i);
            if (song.getId() == songId) {
                currentIndex = i;
                break;
            }
        }
        return currentIndex;
    }

    public static int getSongIndexFromSongs(List<Song> songs, Song song) {
        int currentIndex = -1;
        for (int i = 0; i < songs.size(); i++) {
            if (song.getKey().equals(songs.get(i).getKey())) {
                currentIndex = i;
                break;
            }
        }
        return currentIndex;
    }

    public static void doPlayButtonAction(Context context) {
        MediaPlayer player = MediaService.getPlayer();
        if (player == null) {
            Song song = MediaCache.getSong(context);
            if (song != null) {
                startPlayingMusic(context, song, MediaCache.getQueue(context)); // Make sure to check the cache for songs in the queue
            }
        } else {
            if (player.isPlaying()) {
                player.pause();
            } else {
                player.start();
            }
            if (MediaService.isStopped()) {
                MediaService.pauseNowPlayingThread(false);
            }
            MediaService.notifyMediaPlayStateChangeListeners();
        }
    }

    /**
     * Switches a play button's image. If you want to show the pause image, pass true. If you want to show the play image, pass false.
     *
     * @param playButton The play button to update.
     * @param isPlaying  True for the pause image, false for the play image.
     * @param context    The context.
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
    }

    public static void startAlbumActivity(Context context, Album album) {
        Intent intent = new Intent(context, AlbumActivity.class);
        intent.putExtra(IntentKey.ALBUM.name(), album);
        context.startActivity(intent);
    }

    public static void startArtistActivity(Context context, Artist artist) {
        Intent intent = new Intent(context, ArtistActivity.class);
        intent.putExtra(IntentKey.ARTIST.name(), artist);
        context.startActivity(intent);
    }

    public static void startArtistImageChooserActivity(Context context, Artist artist) {
        Intent intent = new Intent(context, ArtistImageChooserActivity.class);
        intent.putExtra(IntentKey.ARTIST.name(), artist);
        context.startActivity(intent);
    }

    /**
     * Start playing music! Use this when you want to play a single song, with no other songs in the queue.
     *
     * @param context The context.
     * @param song    The song to play.
     */
    public static void startPlayingMusic(Context context, Song song) {
        Intent intent = new Intent(context, MediaService.class);
        intent.putExtra(IntentKey.SONG.name(), song);
        context.startService(intent);
    }

    /**
     * Start playing music! Use this when you want to play a song in an album. The rest of the songs in that album will be hooked up into the queue.
     *
     * @param context The context.
     * @param song    The song to play.
     * @param album   The album of the song to play, which contains the other songs to go into the queue.
     */
    public static void startPlayingMusic(Context context, Song song, Album album) {
        // Get all the song ids in the album. They need to be converted into strings in order to be sent as extras.
        List<Song> songsForAlbum = RepositoryUtil.getSongRepository(context).getSongsForAlbum(album.getId());
        List<Long> queue = new ArrayList<>();
        for (Song songForAlbum : songsForAlbum) {
            queue.add(songForAlbum.getId());
        }
        startPlayingMusic(context, song, queue);
    }

    /**
     * Start playing music! Use this when you want to play a song, with a list of other songs to hook into the queue.
     *
     * @param context The context.
     * @param song    The song to play.
     * @param queue   The queue of other song ids.
     */
    public static void startPlayingMusic(Context context, Song song, List<Long> queue) {
        List<String> queueAsStringList = new ArrayList<>();
        for (Long id : queue) {
            queueAsStringList.add(String.valueOf(id));
        }
        Intent intent = new Intent(context, MediaService.class);
        intent.putExtra(IntentKey.SONG.name(), song);
        intent.putExtra(IntentKey.QUEUE.name(), new ArrayList<>(queueAsStringList));
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

    /**
     * Source: http://stackoverflow.com/questions/20536566/creating-a-random-string-with-a-z-and-0-9-in-java
     *
     * @return
     */
    public static String generateCacheKey() {
        String SALT_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) {
            int index = (int) (rnd.nextFloat() * SALT_CHARS.length());
            salt.append(SALT_CHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr.toLowerCase();
    }

}
