package com.mcnedward.bramble.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mcnedward.bramble.media.Song;

import java.io.IOException;

/**
 * Created by edward on 26/12/15.
 */
public class MediaService extends Service {
    private final static String TAG = "MediaService";

    private MediaPlayer player;
    private MediaThread mediaThread;

    @Override
    public void onCreate() {
        Log.d(TAG, "Creating MediaService!");
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaThread = new MediaThread();
        mediaThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Starting MediaService!");

        Song song = (Song) intent.getSerializableExtra("song");
        mediaThread.startMusic(song);

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
        mediaThread = null;
        stopSelf();
    }

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
            }
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
            boolean retry = false;
            while (retry) {
                try {
                    join();
                    retry = false;
                } catch (InterruptedException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
    }

}
