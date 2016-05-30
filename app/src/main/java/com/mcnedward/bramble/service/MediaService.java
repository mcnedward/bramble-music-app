package com.mcnedward.bramble.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.mcnedward.bramble.enums.IntentKey;
import com.mcnedward.bramble.exception.EntityDoesNotExistException;
import com.mcnedward.bramble.listener.MediaChangeListener;
import com.mcnedward.bramble.listener.MediaPlayingListener;
import com.mcnedward.bramble.entity.media.Album;
import com.mcnedward.bramble.entity.media.Song;
import com.mcnedward.bramble.utils.MediaCache;
import com.mcnedward.bramble.utils.MusicUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by edward on 26/12/15.
 * <p/>
 * A Service to play media. Contains two threads: one for starting and stopping the music, and another for handling the UI updates.
 * Uses listeners to notify views that updates should be made based on the currently playing music.
 */
public class MediaService extends Service {
    private final static String TAG = "MediaService";

    private static MediaPlayer mPlayer;
    private static Song mSong;
    private static ArrayList<Long> mQueue;
    private static Album mAlbum;
    private static Set<MediaChangeListener> mMediaChangeListeners;
    private static Set<MediaPlayingListener> mMediaPlayingListeners;

    private static MediaThread mMediaThread;
    private static NowPlayingThread mNowPlayingThread;
    private static boolean mStopped;

    @Override
    public void onCreate() {
        Log.d(TAG, "Creating MediaService!");
        mQueue = new ArrayList<>();
        if (mMediaThread == null)
            mMediaThread = new MediaThread();
        if (mNowPlayingThread == null)
            mNowPlayingThread = new NowPlayingThread();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Starting MediaService!");
        if (intent != null) {
            mSong = (Song) intent.getSerializableExtra(IntentKey.SONG.name());
            mAlbum = (Album) intent.getSerializableExtra(IntentKey.ALBUM.name());
            List<String> songIdsAsString = intent.getStringArrayListExtra(IntentKey.QUEUE.name());
            if (songIdsAsString != null) {
                for (String id : songIdsAsString) {
                    mQueue.add(Long.parseLong(id));
                }
            }

            MediaCache.saveSong(getApplicationContext(), mSong);
            MediaCache.saveAlbum(getApplicationContext(), mAlbum);
            MediaCache.saveQueue(getApplicationContext(), mQueue);

            mMediaThread.startPlayingMusic();
            mNowPlayingThread.startThread();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Destroying MediaService!");
        mMediaThread.stopThread();
        mNowPlayingThread.stopThread();
    }

    public static void startNextSongInQueue() {
        mMediaThread.startNextSongInQueue();
    }

    public static void startPreviousSongInQueue() {
        mMediaThread.startPreviousSongInQueue();
    }

    public static void pauseNowPlayingThread(boolean pause) {
        mStopped = pause;
        if (mNowPlayingThread != null)
            mNowPlayingThread.pauseThread(pause);
    }

    public static void setPlaybackLooping(boolean looping) {
        if (mPlayer != null) {
            mPlayer.setLooping(looping);
        }
    }

    public static MediaPlayer getPlayer() {
        return mPlayer;
    }

    public static boolean isPlaying() {
        boolean isPlaying = false;
        try {
            isPlaying = mPlayer != null && mPlayer.isPlaying();
        } catch (IllegalStateException e) {
            Log.w(TAG, "The player is null or in an illegal state when checking the isPlaying status.");
        }
        return isPlaying;
    }

    public static boolean isStopped() {
        return mStopped;
    }

    public static boolean isStateOk() {
        return mMediaThread.isStateOk();
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
            pauseNowPlayingThread(false);
            startPlayingMusic();
        }

        @Override
        public void doStopAction() {
            mPlayer.stop();
            mPlayer.reset();
            mPlayer.release();
            mPlayer = null;
        }

        private Song mNextSong;
        private MediaPlayer mNextPlayer;
        private boolean mStateOk;

        private void startPlayingMusic() {
            Log.d(TAG, String.format("Starting to play media for %s", mSong));
            final Uri songUri = Uri.parse(mSong.getData());
            try {
                if (isPlaying()) {
                    MediaPlayer nextPlayer = MediaPlayer.create(getApplicationContext(), songUri);
                    setupNextSong(nextPlayer);  // Setup the next player with the next song

                    nextPlayer.setOnErrorListener(mErrorListener);
                    nextPlayer.setOnCompletionListener(mCompletionListener);
                    mPlayer.setNextMediaPlayer(nextPlayer);

                    mPlayer.stop();
                    mPlayer.reset();
                    mStateOk = false;
                } else {
                    mPlayer = MediaPlayer.create(getApplicationContext(), songUri);
                    mPlayer.setOnPreparedListener(mOnPreparedListener);

                    setupNextSong(mPlayer);
                }
                mPlayer.setOnErrorListener(mErrorListener);
                mPlayer.setOnCompletionListener(mCompletionListener);
                mPlayer.setLooping(MediaCache.isPlaybackLooping(getApplicationContext()));
            } catch (IllegalArgumentException e) {
                Log.e(TAG, e.getMessage(), e);
            } catch (SecurityException e) {
                Log.e(TAG, e.getMessage(), e);
            } catch (IllegalStateException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }

        /**
         * Prepares a MediaPlayer with the next song in an album. This uses the passed in song, sets the mNextPlayer with that
         * song's data, and sets the passed in player to have the mNextPlayer as its NextMediaPlayer. The mNextPlayer gets the default OnError and
         * OnCompletion listeners that are given to the mPlayer.
         *
         * @param player The MediaPlayer to setup with the next song.
         */
        private void setupNextSong(MediaPlayer player, Song nextSong) {
            if (mQueue != null) {
                // Setup the next song in the list
                mNextSong = nextSong;
                mNextPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(mNextSong.getData()));
                player.setNextMediaPlayer(mNextPlayer);
                mNextPlayer.setOnErrorListener(mErrorListener);
                mNextPlayer.setOnCompletionListener(mCompletionListener);
                mNextPlayer.setLooping(MediaCache.isPlaybackLooping(getApplicationContext()));
            }
        }

        /**
         * Prepares a MediaPlayer with the next song in an album. This retrieves the next song from the MusicUtil, sets the mNextPlayer with that
         * song's data, and sets the passed in player to have the mNextPlayer as its NextMediaPlayer. The mNextPlayer gets the default OnError and
         * OnCompletion listeners that are given to the mPlayer.
         *
         * @param player The MediaPlayer to setup with the next song.
         */
        private void setupNextSong(MediaPlayer player) {
            if (mQueue != null) {
                // Setup the next song in the list
                try {
                    mNextSong = MusicUtil.getNextSongFromIds(getApplicationContext(), mSong, mQueue);
                } catch (EntityDoesNotExistException e) {
                    Log.w(TAG, e.getMessage());
                    return;
                }
                mNextPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(mNextSong.getData()));
                player.setNextMediaPlayer(mNextPlayer);
                mNextPlayer.setOnErrorListener(mErrorListener);
                mNextPlayer.setOnCompletionListener(mCompletionListener);
                mNextPlayer.setLooping(MediaCache.isPlaybackLooping(getApplicationContext()));
            }
        }

        /**
         * Stops the current MediaPlayer. This should have a queue setup already, so that should start right away.
         */
        protected void startNextSongInQueue() {
            mStateOk = false;
            mPlayer.seekTo(mPlayer.getDuration());  // Seek all the way to the end, so the OnCompleteListener will be called
        }

        protected void startPreviousSongInQueue() {
            try {
                Song previousSong = MusicUtil.getPreviousSongFromIds(getApplicationContext(), mSong, mQueue);
                setupNextSong(mPlayer, previousSong);
                startNextSongInQueue(); // The next song in the queue is now the song before this one
            } catch (EntityDoesNotExistException e) {
                Log.w(TAG, e.getMessage());
            }
        }

        private final MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mPlayer != mp) {
                    mPlayer.reset();
                    mPlayer.release();
                    mStateOk = false;
                    mPlayer = mp;
                }
                mPlayer.start();
                mStateOk = true;
                notifyMediaChangeListeners();
                notifyMediaPlayStateChangeListeners();
            }
        };

        private final MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mp.isLooping()) return;
                if (mNextPlayer != null) {
                    mPlayer = mNextPlayer;
                    mSong = mNextSong;
                    MediaCache.saveSong(getApplicationContext(), mSong);
                    setupNextSong(mPlayer);
                } else {
                    mStopped = true;
                    pauseNowPlayingThread(true);
                    notifyMediaStopListeners();
                }
                mPlayer.start();
                mStateOk = true;
                notifyMediaChangeListeners();
                notifyMediaPlayStateChangeListeners();
            }
        };

        private final MediaPlayer.OnErrorListener mErrorListener = new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.w(TAG, String.format("Something went wrong with the MediaPlayer %s; The what is: %s; The extra is: %s; The state ok? %s (This is my own state, so it may not always be right, but it should be...)", mp, what, extra, mStateOk));
                return false;
            }
        };

        protected boolean isStateOk() {
            return mStateOk;
        }
    }

    final class NowPlayingThread extends BaseThread implements IThread {

        public NowPlayingThread() {
            super("NowPlaying");
        }

        private void updateNowPlaying() {
            for (final MediaPlayingListener listener : mMediaPlayingListeners) {
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
        public void doRunAction() {
            updateNowPlaying();
        }

        @Override
        public void doStartAction() {
            updateNowPlaying();
        }

        @Override
        public void doStopAction() {

        }
    }

    public static void unRegisterListeners() {
        mMediaChangeListeners = new HashSet<>();
        mMediaPlayingListeners = new HashSet<>();
    }

    public static void attachMediaChangeListener(MediaChangeListener listener) {
        if (mMediaChangeListeners == null)
            mMediaChangeListeners = new HashSet<>();
        mMediaChangeListeners.add(listener);
        if (mSong != null)
            listener.onMediaChange(mSong, isPlaying());
    }

    public static void removeMediaChangeListener(MediaChangeListener listener) {
        if (mMediaChangeListeners == null)
            mMediaChangeListeners = new HashSet<>();
        if (mMediaChangeListeners.contains(listener))
            mMediaChangeListeners.remove(listener);
    }

    public static void notifyMediaChangeListeners() {
        if (mSong == null) return;
        for (MediaChangeListener listener : mMediaChangeListeners)
            listener.onMediaChange(mSong, isPlaying());
    }

    public static void notifyMediaPlayStateChangeListeners() {
        if (mSong == null) return;
        for (MediaChangeListener listener : mMediaChangeListeners)
            listener.onMediaPlayStateChange(mSong, isPlaying());
    }

    public static void notifyMediaStopListeners() {
        if (mSong == null) return;
        for (MediaChangeListener listener : mMediaChangeListeners)
            listener.onMediaStop(mSong);
    }

    public static void attachMediaPlayingListener(MediaPlayingListener listener) {
        if (mMediaPlayingListeners == null)
            mMediaPlayingListeners = new HashSet<>();
        mMediaPlayingListeners.add(listener);
    }

    public static void removeMediaPlayingListener(MediaPlayingListener listener) {
        if (mMediaPlayingListeners == null)
            mMediaPlayingListeners = new HashSet<>();
        if (mMediaPlayingListeners.contains(listener))
            mMediaPlayingListeners.remove(listener);
    }

}
