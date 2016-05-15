package com.mcnedward.bramble.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mcnedward.bramble.exception.MediaNotFoundException;
import com.mcnedward.bramble.listener.MediaListener;
import com.mcnedward.bramble.media.Song;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by edward on 26/12/15.
 */
public class MediaService extends Service {
    private final static String TAG = "MediaService";

    private static MediaPlayer mPlayer;
    private static Song song;
    private static Set<MediaListener> mListeners;

    private static MediaThread mediaThread;
    private static NowPlayingThread nowPlayingThread;

    @Override
    public void onCreate() {
        Log.d(TAG, "Creating MediaService!");
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        if (mediaThread == null)
            mediaThread = new MediaThread();
        if (nowPlayingThread == null)
            nowPlayingThread = new NowPlayingThread(mListeners);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Starting MediaService!");

        song = (Song) intent.getSerializableExtra("song");
        mediaThread.startMusic(song);
        nowPlayingThread.startThread();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Destroying MediaService!");
        mediaThread.stopThread();
        mediaThread = null;
        // TODO Stop NowPlayingThread? I dont think so...?
    }

    public static void attachMediaListener(MediaListener listener) {
        if (mListeners == null)
            mListeners = new HashSet<>();
        mListeners.add(listener);

        if (nowPlayingThread != null) {
            nowPlayingThread.attachMediaListener(listener);
        }
    }

    public static void removeMediaListener(MediaListener listener) {
        if (mListeners == null)
            mListeners = new HashSet<>();
        if (mListeners.contains(listener))
            mListeners.remove(listener);

        if (nowPlayingThread != null) {
            nowPlayingThread.removeMediaListener(listener);
        }
    }

    public static void unRegisterListeners() {
        for (MediaListener listener : mListeners)
            removeMediaListener(listener);
    }

    public static void pauseNowPlayingView(boolean pause) {
        if (nowPlayingThread != null)
            nowPlayingThread.pauseThread(pause);
    }

    public static MediaPlayer getPlayer() {
        return mPlayer;
    }

    public static Song getCurrentSong() throws MediaNotFoundException {
        if (song == null)
            throw new MediaNotFoundException("Could not find the current song from " + TAG);
        return song;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    final class MediaThread extends BaseThread {
        private static final String TAG = "MediaThread";

        private Song song;

        public MediaThread() {
            super("Media");
        }

        @Override
        public void doRunAction() {
        }

        @Override
        public void doStartAction() {
            startPlayingMusic();
            for (MediaListener listener : mListeners) {
                listener.notifyMediaStarted();
            }
        }

        @Override
        public void doStopAction() {
        }

        private void startPlayingMusic() {
            Log.d(TAG, String.format("Starting to play media for %s", song));
            final Uri songUri = Uri.parse(song.getData());
            try {
                if (mPlayer.isPlaying()) {
                    mPlayer.stop();
                    mPlayer.reset();
                }
                mPlayer = MediaPlayer.create(getApplicationContext(), songUri);
                mPlayer.start();
            } catch (IllegalArgumentException e) {
                Log.e(TAG, e.getMessage(), e);
            } catch (SecurityException e) {
                Log.e(TAG, e.getMessage(), e);
            } catch (IllegalStateException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }

        public void startMusic(Song song) {
            this.song = song;
            startThread();
        }
    }

}
