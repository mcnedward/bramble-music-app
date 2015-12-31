package com.mcnedward.bramble.view.nowPlaying;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.activity.MainActivity;
import com.mcnedward.bramble.exception.MediaNotFoundException;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Song;
import com.mcnedward.bramble.service.MediaService;
import com.mcnedward.bramble.utils.Extension;
import com.mcnedward.bramble.utils.MediaCache;
import com.mcnedward.bramble.utils.listener.AlbumLoadListener;
import com.mcnedward.bramble.utils.listener.MediaStartedListener;

/**
 * Created by edward on 27/12/15.
 */
public class NowPlayingView extends SlidingView implements AlbumLoadListener, MediaStartedListener {
    private final static String TAG = "NowPlayingView";

    private Context context;

    private Song song;

    private NowPlayingTitleBar titleBar;
    private NowPlayingSubControlsView bottomControls;
    private TextView txtSongTitle;
    private TextView txtAlbum;
    private ImageView imgAlbumArt;
    private TextView txtPassed;
    private SeekBar seekBar;
    private TextView txtDuration;
    private ImageView btnPrevious;
    private ImageView btnPlay;
    private ImageView btnForward;

    private boolean loaded = false;

    public NowPlayingView(Context context) {
        super(R.layout.now_playing_view, context);

        initialize(context);
    }

    public NowPlayingView(Context context, AttributeSet attrs) {
        super(R.layout.now_playing_view, context, attrs);

        initialize(context);
    }

    @Override
    protected void switchSlidable(boolean top) {
        if (top) {
            titleBar.setVisibility(VISIBLE);
            bottomControls.setVisibility(INVISIBLE);
        } else {
            titleBar.setVisibility(INVISIBLE);
            bottomControls.setVisibility(VISIBLE);
        }
    }

    @Override
    public void notifyMediaStarted(Song song) {
        this.song = song;
        notifyAlbumLoadReady();

        titleBar.setSongTitle(song.getTitle());
        bottomControls.setSongTitle(song.getTitle());

        loaded = true;
        updateUIThread();
    }

    public void updateUIThread() {
        final MediaPlayer player = MediaService.getPlayer();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (player != null
                        && player.getCurrentPosition() < player.getDuration()) {
                    // Sleep for 100 milliseconds
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (!isControlsTouched() || isContentFocused()) {
                        post(new Runnable() {
                            @Override
                            public void run() {
                                // Get the current time and total duration and display on the UI
                                String currentTime = Extension.getTimeString(player.getCurrentPosition());
                                String duration = Extension.getTimeString(player.getDuration());
                                txtPassed.setText(currentTime);
                                txtDuration.setText(duration);
                            }
                        });
                    }
                }
            }
        }).start();
    }

    @Override
    public void notifyAlbumLoadReady() {
        Log.d(TAG, "Albums ready...");
        Album album;
        try {
            if (song == null)
                Log.w(TAG, "Albums loaded but no media selected.");
            else {
                album = MainActivity.mediaCache.getAlbumForSong(song);
                bottomControls.updateAlbumArt(album);
                titleBar.setAlbumTitle(album.getAlbumName());
                // Load album art
                Extension.updateAlbumArt(album, imgAlbumArt);
            }
        } catch (MediaNotFoundException e) {
            Log.d(TAG, e.getMessage(), e);
        }
    }

    private void setButtonListeners() {
        btnPrevious.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnForward.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initialize(Context context) {
        this.context = context;

        // Register this as listeners
        MediaCache.registerAlbumLoadListener(this);
        MediaService.registerNowPlayingView(this);

        titleBar = (NowPlayingTitleBar) findViewById(R.id.now_playing_title_bar);
        bottomControls = (NowPlayingSubControlsView) findViewById(R.id.now_playing_sub_controls);

        txtSongTitle = (TextView) findViewById(R.id.now_playing_title);
        txtAlbum = (TextView) findViewById(R.id.now_playing_album);
        imgAlbumArt = (ImageView) findViewById(R.id.now_playing_album_art);

        txtPassed = (TextView) findViewById(R.id.now_playing_passed);
        txtDuration = (TextView) findViewById(R.id.now_playing_duration);
        seekBar = (SeekBar) findViewById(R.id.now_playing_seek);

        btnPrevious = (ImageView) findViewById(R.id.btn_previous);
        btnPlay = (ImageView) findViewById(R.id.btn_play);
        btnForward = (ImageView) findViewById(R.id.btn_forward);
        setButtonListeners();

        setSlidable(bottomControls);
        setContent(findViewById(R.id.now_playing_content));

        // Load controls
        seekBar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.Red), PorterDuff.Mode.MULTIPLY));
        seekBar.getThumb().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.Red), PorterDuff.Mode.SRC_IN));

        int rippleColor = R.color.FireBrick;
        Extension.setRippleBackground(btnPrevious, rippleColor, 0, context);
        Extension.setRippleBackground(btnPlay, rippleColor, 0, context);
        Extension.setRippleBackground(btnForward, rippleColor, 0, context);
    }

    public boolean isLoaded() {
        return loaded;
    }

    public TextView getTxtPassed() {
        return txtPassed;
    }

    public SeekBar getSeekBar() {
        return seekBar;
    }

    public TextView getTxtDuration() {
        return txtDuration;
    }

    public ImageView getBtnPrevious() {
        return btnPrevious;
    }

    public ImageView getBtnPlay() {
        return btnPlay;
    }

    public ImageView getBtnForward() {
        return btnForward;
    }

}