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

    public static void doPreviousButtonAction(Context context) {
        Song song = MediaCache.getSong(context);
        List<String> songKeys = MediaCache.getSongKeys(context);
        if (song == null) return;

        // First check if the current song should just be restarted
        // If the player is null, then all songs have stopped, or the app is freshly opened, so the song should not be restarted, but should go to the previous
        MediaPlayer player = MediaService.getPlayer();
        if (player != null) {
            int currentPosition = getTimeInSeconds(player.getCurrentPosition());
            if (currentPosition > 2) {
                startPlayingMusic(context, song, songKeys);
                return;
            }
        }

        if (songKeys.isEmpty()) return; // TODO Restart song if there are no other songs in list? Or should there always be at least one song in the list? Yes there should.


        Song nextSong = null;
        try {
            nextSong = getPreviousSongFromKeys(context, song, songKeys);
        } catch (EntityDoesNotExistException e) {
            Log.e(TAG, e.getMessage(), e);
            return;
        }
        startPlayingMusic(context, nextSong);
    }

    public static void doForwardButtonAction(Context context) {
        Song song = MediaCache.getSong(context);
        List<String> songKeys = MediaCache.getSongKeys(context);
        if (song == null) return;

        if (songKeys.isEmpty()) return; // TODO Restart song if there are no other songs in list? Or should there always be at least one song in the list? Yes there should.


        Song nextSong = null;
        try {
            nextSong = getNextSongFromKeys(context, song, songKeys);
        } catch (EntityDoesNotExistException e) {
            Log.e(TAG, e.getMessage(), e);
            return;
        }
        startPlayingMusic(context, nextSong);
    }

    public static Song getNextSongFromKeys(Context context, Song currentSong, List<String> songKeys) throws EntityDoesNotExistException {
        int currentIndex = getSongIndexFromKeys(songKeys, currentSong);
        if (currentIndex == -1) return null;
        String nextSongKey;
        // Get the next song, or the first if the current song is the last
        if (currentIndex == songKeys.size() - 1) {
            nextSongKey = songKeys.get(0);
        } else {
            nextSongKey = songKeys.get(currentIndex + 1);
        }
        return RepositoryUtil.getSongRepository(context).get(nextSongKey);
    }

    public static Song getPreviousSongFromKeys(Context context, Song currentSong, List<String> songKeys) throws EntityDoesNotExistException {
        int currentIndex = getSongIndexFromKeys(songKeys, currentSong);
        if (currentIndex == -1) return null;
        String nextSongKey;
        // Get the previous song, or the last one if the current song is the first
        if (currentIndex == 0) {
            nextSongKey = songKeys.get(songKeys.size() - 1);
        } else {
            nextSongKey = songKeys.get(currentIndex - 1);
        }
        return RepositoryUtil.getSongRepository(context).get(nextSongKey);
    }

    public static int getSongIndexFromKeys(List<String> songKeys, Song song) {
        int currentIndex = -1;
        for (int i = 0; i < songKeys.size(); i++) {
            String songKey = songKeys.get(i);
            if (song.getKey().equals(songKey)) {
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

    public static void doPlayButtonAction(Context context, ImageView playButton) {
        MediaPlayer player = MediaService.getPlayer();
        if (player == null) {
            Song song = MediaCache.getSong(context);
            if (song != null) {
                startPlayingMusic(context, song);
            }
        } else {
            if (player.isPlaying()) {
                player.pause();
                switchPlayButton(context, playButton, false);
            } else {
                player.start();
                switchPlayButton(context, playButton, true);
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

    public static void startPlayingMusic(Context context, Song song) {
        // Start playing music!
        Intent intent = new Intent(context, MediaService.class);
        intent.putExtra(IntentKey.SONG.name(), song);
        context.startService(intent);
    }

    public static void startPlayingMusic(Context context, Song song, Album album) {
        // Get all the song ids in the album
        List<Song> songsForAlbum = RepositoryUtil.getSongRepository(context).getSongsForAlbum(album.getId());
        List<String> songKeys = new ArrayList<>();
        for (Song songForAlbum : songsForAlbum) {
            songKeys.add(songForAlbum.getKey());
        }
        startPlayingMusic(context, song, songKeys);
    }

    public static void startPlayingMusic(Context context, Song song, List<String> songKeys) {
        Intent intent = new Intent(context, MediaService.class);
        intent.putExtra(IntentKey.SONG.name(), song);
        intent.putExtra(IntentKey.SONG_KEYS.name(), new ArrayList<>(songKeys));
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
