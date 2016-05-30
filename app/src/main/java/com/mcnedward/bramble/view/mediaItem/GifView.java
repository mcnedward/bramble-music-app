package com.mcnedward.bramble.view.mediaItem;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.listener.MediaChangeListener;
import com.mcnedward.bramble.entity.media.Song;

/**
 * Created by Edward on 5/17/2016.
 *
 * A class for playing the NowPlaying music dancing gif.
 */
public class GifView extends ImageView implements MediaChangeListener {

    private Context mContext;
    private Song mSong;
    private static Song mCurrentSong;
    private static boolean mIsPlaying;
    private AnimationDrawable mAnimation;

    public GifView(Context context) {
        super(context);
        if (!isInEditMode())
            initialize(context);
    }

    public GifView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            initialize(context);
    }

    public GifView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            initialize(context);
    }

    private void initialize(Context context) {
        mContext = context;
        setBackgroundResource(R.drawable.now_playing_anim);
        mAnimation = (AnimationDrawable) getBackground();
        post(new Runnable() {
            @Override
            public void run() {
                mAnimation.start();
            }
        });

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnimation.start();
            }
        });
    }

    /**
     * Set the current playing song. This is needed to ensure that the current song is set before the start() and stop() methods are called, since
     * it may take some time to start the MediaPlayer in the service (which is what calls the notifySongChange()).
     *
     * @param song The current song.
     */
    public void setCurrentSong(Song song) {
        mCurrentSong = song;
    }

    /**
     * Updates the song. Since this view is being used in an adapter, it needs to update the song and check the icon state on every view recycle.
     *
     * @param song The song.
     */
    public void update(Song song) {
        mSong = song;
        switchMediaIcon();
    }

    @Override
    public void onMediaPlayStateChange(Song currentSong, boolean playing) {
        mCurrentSong = currentSong;
        mIsPlaying = playing;
        switchMediaIcon();
    }

    @Override
    public void onMediaChange(Song currentSong, boolean playing) {
        mCurrentSong = currentSong;
        mIsPlaying = playing;
        switchMediaIcon();
    }

    @Override
    public void onMediaStop(Song song) {
        stop();
    }

    /**
     * Switches the state of the MediaIcon. Starts or pauses if this GifView's song is the current song, and stops the animation if it is not.
     */
    public void switchMediaIcon() {
        if (mSong != null && mCurrentSong != null) {
            if (mSong.getId() == mCurrentSong.getId()) {
                play(mIsPlaying);
            } else {
                stop();
            }
        }
    }

    /**
     * Plays or pauses the icon.
     * @param play If true, the icon will be played. Paused otherwise.
     */
    public void play(final boolean play) {
        setVisibility(VISIBLE);
        post(new Runnable() {
            @Override
            public void run() {
                if (play) {
                    mAnimation.start();
                } else {
                    mAnimation.stop();
                }
            }
        });
    }

    /**
     * Stops the icon animation and hides the view.
     */
    public void stop() {
        post(new Runnable() {
            @Override
            public void run() {
                mAnimation.stop();
                setVisibility(GONE);
            }
        });
    }

}
