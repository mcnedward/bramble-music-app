package com.mcnedward.bramble.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.mcnedward.bramble.listener.SongPlayingListener;
import com.mcnedward.bramble.listener.MediaListener;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Song;
import com.mcnedward.bramble.utils.MediaCache;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by edward on 26/12/15.
 *
 * A Service to play media. Contains two threads: one for starting and stopping the music, and another for handling the UI updates.
 * Uses listeners to notify views that updates should be made based on the currently playing music.
 */
public class MediaService extends Service {
    private final static String TAG = "MediaService";

    private static MediaPlayer mPlayer;
    private static Song mSong;
    private static Album mAlbum;
    private static Set<MediaListener> mListeners;
    private static Set<SongPlayingListener> mSongPlayingListeners;

    private static MediaThread mMediaThread;
    private static NowPlayingThread mNowPlayingThread;

    @Override
    public void onCreate() {
        Log.d(TAG, "Creating MediaService!");
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        if (mMediaThread == null)
            mMediaThread = new MediaThread();
        if (mNowPlayingThread == null)
            mNowPlayingThread = new NowPlayingThread();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Starting MediaService!");
        if (intent != null) {
            mSong = (Song) intent.getSerializableExtra("song");
            mAlbum = (Album) intent.getSerializableExtra("album");

            MediaCache.saveSong(mSong, getApplicationContext());
            MediaCache.saveAlbum(mAlbum, getApplicationContext());
            mMediaThread.startThread();
            mNowPlayingThread.startThread();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Destroying MediaService!");
        mNowPlayingThread.stopThread();
        mMediaThread.stopThread();
    }

    public static void attachMediaListener(MediaListener listener) {
        if (mListeners == null)
            mListeners = new HashSet<>();
        mListeners.add(listener);
        if (mSong != null)
            listener.notifyMediaStarted();
    }

    public static void removeMediaListener(MediaListener listener) {
        if (mListeners == null)
            mListeners = new HashSet<>();
        if (mListeners.contains(listener))
            mListeners.remove(listener);
    }

    public static void unRegisterListeners() {
        for (MediaListener listener : mListeners)
            removeMediaListener(listener);
    }

    public static void notifyMediaListeners() {
        if (mSong == null) return;
        for (MediaListener listener : mListeners)
            listener.notifyMediaStarted();
    }

    public static void attachSongPlayingListener(SongPlayingListener listener) {
        if (mSongPlayingListeners == null)
            mSongPlayingListeners = new HashSet<>();
        mSongPlayingListeners.add(listener);
        if (mSong != null && mPlayer != null)
            listener.notifySongChange(mSong, mPlayer.isPlaying());
    }

    public static void removeMediaListener(SongPlayingListener listener) {
        if (mSongPlayingListeners == null)
            mSongPlayingListeners = new HashSet<>();
        if (mSongPlayingListeners.contains(listener))
            mSongPlayingListeners.remove(listener);
    }

    public static void notifySongPlayingListeners() {
        if (mSongPlayingListeners == null || mPlayer == null) return;
        for (SongPlayingListener listener : mSongPlayingListeners) {
            listener.notifySongChange(mSong, mPlayer.isPlaying());
        }
    }

    public static void pauseNowPlayingView(boolean pause) {
        if (mNowPlayingThread != null)
            mNowPlayingThread.pauseThread(pause);
    }

    public static MediaPlayer getPlayer() {
        return mPlayer;
    }

    public static boolean isPlaying() {
        return mPlayer != null && mPlayer.isPlaying();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    final class MediaThread extends BaseThread {
        private static final String TAG = "MediaThread";

        public MediaThread() {
            super("Media");
        }

        @Override
        public void doRunAction() {

        }

        @Override
        public void doStartAction() {
            startPlayingMusic();
        }

        @Override
        public void doStopAction() {
            mPlayer.stop();
            mPlayer.release();
        }

        private void startPlayingMusic() {
            Log.d(TAG, String.format("Starting to play media for %s", mSong));
            final Uri songUri = Uri.parse(mSong.getData());
            try {
                if (mPlayer.isPlaying()) {
                    MediaPlayer nextPlayer = MediaPlayer.create(getApplicationContext(), songUri);
                    nextPlayer.setOnPreparedListener(mOnPreparedListener);
                    mPlayer.setNextMediaPlayer(nextPlayer);
                    mPlayer.stop();
                    mPlayer.reset();
                } else {
                    mPlayer = MediaPlayer.create(getApplicationContext(), songUri);
                    mPlayer.setOnPreparedListener(mOnPreparedListener);
                }
            } catch (IllegalArgumentException e) {
                Log.e(TAG, e.getMessage(), e);
            } catch (SecurityException e) {
                Log.e(TAG, e.getMessage(), e);
            } catch (IllegalStateException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }

        private final MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mPlayer != mp) {
                    mPlayer.reset();
                    mPlayer.release();
                    mPlayer = mp;
                }
                mPlayer.start();
                for (MediaListener listener : mListeners) {
                    listener.notifyMediaStarted();
                }
                notifySongPlayingListeners();
            }
        };
    }

    final class NowPlayingThread extends BaseThread implements IThread {

        public NowPlayingThread() {
            super("NowPlaying");
        }

        @Override
        public void doRunAction() {
            for (final MediaListener listener : mListeners) {
                View view = listener.getView();
                if (view == null || view.getHandler() == null) return;
                view.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        listener.update(mSong, mAlbum);
                    }
                });
            }
        }

        @Override
        public void doStartAction() {

        }

        @Override
        public void doStopAction() {

        }
    }

}
