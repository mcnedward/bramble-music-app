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
import com.mcnedward.bramble.utils.RepositoryUtil;

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
    private static ArrayList<String> mSongKeys;
    private static Album mAlbum;
    private static Set<MediaChangeListener> mMediaChangeListeners;
    private static Set<MediaPlayingListener> mMediaPlayingListeners;

    private static MediaThread mMediaThread;
    private static NowPlayingThread mNowPlayingThread;
    private static boolean mStopped;

    @Override
    public void onCreate() {
        Log.d(TAG, "Creating MediaService!");
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
            mSongKeys = intent.getStringArrayListExtra(IntentKey.SONG_KEYS.name());

            MediaCache.saveSong(getApplicationContext(), mSong);
            MediaCache.saveAlbum(getApplicationContext(), mAlbum);
            RepositoryUtil.getPlaylistRepository(getApplicationContext()).saveCurrentPlaylist(mSongKeys);
            List<String> p2 = RepositoryUtil.getPlaylistRepository(getApplicationContext()).getCurrentPlaylist();

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

        private void startPlayingMusic() {
            Log.d(TAG, String.format("Starting to play media for %s", mSong));
            final Uri songUri = Uri.parse(mSong.getData());
            try {
                if (isPlaying()) {
                    MediaPlayer nextPlayer = MediaPlayer.create(getApplicationContext(), songUri);
                    setupNextSong(nextPlayer);  // Setup the next player with the next song

                    nextPlayer.setOnPreparedListener(mOnPreparedListener);
                    nextPlayer.setOnErrorListener(mErrorListener);
                    nextPlayer.setOnCompletionListener(mCompletionListener);
                    mPlayer.setNextMediaPlayer(nextPlayer);

                    mPlayer.stop();
                    mPlayer.reset();
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
         * Prepares a MediaPlayer with the next song in an album. This retrieves the next song from the MusicUtil, sets the mNextPlayer with that
         * song's data, and sets the passed in player to have the mNextPlayer as its NextMediaPlayer. The mNextPlayer gets the default OnError and
         * OnCompletion listeners that are given to the mPlayer.
         *
         * @param player The MediaPlayer to setup with the next song.
         */
        private void setupNextSong(MediaPlayer player) {
            if (mSongKeys != null) {
                // Setup the next song in the list
                try {
                    mNextSong = MusicUtil.getNextSongFromKeys(context, mSong, mSongKeys);
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

        private final MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mPlayer != mp) {
                    mPlayer.reset();
                    mPlayer.release();
                    mPlayer = mp;
                }
                mPlayer.start();
                notifyMediaChangeListeners();
                notifyMediaPlayStateChangeListeners();
            }
        };

        private final MediaPlayer.OnErrorListener mErrorListener = new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.w(TAG, String.format("Something went wrong with the MediaPlayer %s; The what is: %s; The extra is: %s", mp, what, extra));
                return false;
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
                notifyMediaChangeListeners();
                notifyMediaPlayStateChangeListeners();
            }
        };
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
