package com.mcnedward.bramble.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mcnedward.bramble.exception.MediaNotFoundException;
import com.mcnedward.bramble.media.Song;
import com.mcnedward.bramble.view.nowPlaying.NowPlayingView;

import java.io.IOException;

/**
 * Created by edward on 26/12/15.
 */
public class MediaService extends Service {
    private final static String TAG = "MediaService";

    private static MediaService instance;
    private static MediaPlayer player;
    private static Song song;
    private static boolean playingMusic;

    private MediaThread mediaThread;

    @Override
    public void onCreate() {
        Log.d(TAG, "Creating MediaService!");
        instance = this;

        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaThread = new MediaThread();
        mediaThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Starting MediaService!");

        song = (Song) intent.getSerializableExtra("song");
        mediaThread.startMusic(song);
        nowPlayingView.notifyMediaStarted(song);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Destroying MediaService!");
        mediaThread.stopMusic();

        boolean retry = false;
        while (retry) {
            try {
                mediaThread.join();
                retry = false;
            } catch (InterruptedException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        mediaThread = null;
        stopSelf();
    }

    public static MediaService getInstance() {
        return instance;
    }

    private static NowPlayingView nowPlayingView;

    public static void registerNowPlayingView(NowPlayingView view) {
        nowPlayingView = view;
    }

    public static Song getCurrentSong() throws MediaNotFoundException {
        if (song == null)
            throw new MediaNotFoundException("Could not find the current song from " + TAG);
        return song;
    }

    public static boolean isPlayingMusic() {
        return playingMusic;
    }

    Handler handler = new Handler(Looper.getMainLooper());

    final class MediaThread extends Thread {

        private boolean running, playSong;
        private Song song;

        public MediaThread() {
            running = false;
            playSong = false;
        }

        @Override
        public void run() {
            while (running) {
                if (playSong) {
                    startPlayingMusic();
                    playSong = false;
                }
                if (player.isPlaying()) {
                    handler.post(nowPlayingView.updateUIThread(player));

                }
            }
            player.stop();
        }

        private void startPlayingMusic() {
            Log.d(TAG, String.format("Starting to play media for %s", song));
            final Uri songUri = Uri.parse(song.getData());
            try {
                if (player.isPlaying()) {
                    player.stop();
                    player.reset();
                }
                player.setDataSource(getApplicationContext(), songUri);
                player.prepare();
                player.start();

                playingMusic = true;
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, e.getMessage(), e);
            } catch (SecurityException e) {
                Log.e(TAG, e.getMessage(), e);
            } catch (IllegalStateException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }

        @Override
        public void start() {
            running = true;
            super.start();
        }

        public void startMusic(Song song) {
            this.song = song;
            playSong = true;
        }

        public void stopMusic() {
            running = false;
        }
    }

}
