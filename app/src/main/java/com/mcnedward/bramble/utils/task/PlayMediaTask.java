package com.mcnedward.bramble.utils.task;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.mcnedward.bramble.activity.NowPlayingActivity;
import com.mcnedward.bramble.media.Song;

import java.io.IOException;

/**
 * Created by edward on 26/12/15.
 */
public class PlayMediaTask extends AsyncTask<Song, Integer, Void> {
    private final static String TAG = "PlayMediaTask";

    private NowPlayingActivity nowPlaying;
    private MediaPlayer player;
    private SeekBar seekBar;

    public PlayMediaTask(NowPlayingActivity nowPlaying) {
        this.nowPlaying = nowPlaying;

        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        seekBar = nowPlaying.getSeekBar();

        setButtonOnClickEvents();
    }

    private void setButtonOnClickEvents() {
        nowPlaying.getBtnPrevious().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        nowPlaying.getBtnPlay().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player.isPlaying())
                    player.pause();
                else
                    player.start();
            }
        });
        nowPlaying.getBtnForward().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected Void doInBackground(Song... songList) {
        Song song = songList[0];
        Log.i(TAG, String.format("Starting to play media for %s", song));

        final Uri songUri = Uri.parse(song.getData());
        playMedia(songUri);

        return null;
    }

    /**
     * This is used to play the media for the current song. A thread is started to display the current progress of the
     * song as text and in the seek bar.
     */
    public void playMedia(Uri songUri) {
        try {
            if (player.isPlaying()) {
                player.stop();
            }
            player.setDataSource(nowPlaying, songUri);
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
        new MediaThread().start();
    }

    final class MediaThread extends Thread {

        @Override
        public void start() {
            while (player != null && player.getCurrentPosition() < player.getDuration()) {
                // Sleep for 100 milliseconds
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
                seekBar.setMax(player.getDuration());
                seekBar.setProgress(player.getCurrentPosition());
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser == true) {
                            player.seekTo(seekBar.getProgress());
                        } else {
                            // Do nothing
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                nowPlaying.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Get the current time and total duration and display on the UI
                        String currentTime = getTimeString(player.getCurrentPosition());
                        String duration = getTimeString(player.getDuration());

                        // Find the TextViews from the NowPlayingActivity and update UI
                        nowPlaying.getTxtPassed().setText(currentTime);

                        nowPlaying.getTxtDuration().setText(String.valueOf(duration));
                    }
                });
            }
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
