package com.mcnedward.bramble.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.SeekBar;

import com.mcnedward.bramble.activity.NowPlayingActivity;
import com.mcnedward.bramble.media.Song;

import java.io.IOException;

/**
 * Created by edward on 26/12/15.
 */
public class MediaService extends Service {
    private final static String TAG = "MediaService";

    private static MediaService instance;

    private MediaPlayer player;
    private MediaThread mediaThread;

    private boolean playingMusic;

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

    private static NowPlayingActivity nowPlayingActivity;

    public static void registerNowPlayingActivity(NowPlayingActivity activity) {
        nowPlayingActivity = activity;
    }

    public MediaPlayer getMediaPlayer() {
        return player;
    }

    public boolean isPlayingMusic() {
        return playingMusic;
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
//                if (nowPlayingActivity != null && !nowPlayingActivity.isPaused()) {
//                    nowPlayingActivity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            while (player != null && player.getCurrentPosition() < player.getDuration()) {
//                                // Sleep for 100 milliseconds
//                                try {
//                                    Thread.sleep(100);
//                                } catch (InterruptedException e) {
//                                    Log.e(TAG, e.getMessage(), e);
//                                }
//                                nowPlayingActivity.getSeekBar().setMax(player.getDuration());
//                                nowPlayingActivity.getSeekBar().setProgress(player.getCurrentPosition());
//                                nowPlayingActivity.getSeekBar().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                                    @Override
//                                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                                        if (fromUser == true) {
//                                            player.seekTo(seekBar.getProgress());
//                                        } else {
//                                            // Do nothing
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onStartTrackingTouch(SeekBar seekBar) {
//
//                                    }
//
//                                    @Override
//                                    public void onStopTrackingTouch(SeekBar seekBar) {
//
//                                    }
//                                });
//                                String currentTime = getTimeString(player.getCurrentPosition());
//                                String duration = getTimeString(player.getDuration());
//
//                                // Find the TextViews from the NowPlayingActivity and update UI
//                                nowPlayingActivity.getTxtPassed().setText(currentTime);
//
//                                nowPlayingActivity.getTxtDuration().setText(String.valueOf(duration));
//                            }
//                        }
//                    });
//                }
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

    /**
     * Used to format the time of the current media
     *
     * @param millis
     *            - The time in milliseconds to format
     * @return - The formatted time
     */
    private String getTimeString(long millis) {
        int minutes = (int) (millis / (1000 * 60));
        int seconds = (int) ((millis / 1000) % 60);
        return String.format("%d:%02d", minutes, seconds);
    }

}
