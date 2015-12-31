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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward on 27/12/15.
 */
public class NowPlayingView extends SlidingView implements AlbumLoadListener, MediaStartedListener {
    private final static String TAG = "NowPlayingView";

    private Context context;

    private Song song;

    private NowPlayingTitleBar titleBar;
    private TextView txtSongTitle;
    private TextView txtAlbum;
    private ImageView imgAlbumArt;
    private TextView txtPassed;
    private SeekBar seekBar;
    private TextView txtDuration;
    private ImageView btnPrevious;
    private ImageView btnPlay;
    private ImageView btnForward;
    private List<ImageView> playButtons;

    private boolean albumLoaded = false;
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
            titleBar.switchPlayIcon(INVISIBLE);
        } else {
            titleBar.switchPlayIcon(VISIBLE);
        }
    }

    @Override
    public void notifyMediaStarted() {
        loaded = false;
        albumLoaded = false;
        updateUIThread();
    }

    public void updateUIThread() {
        try {
            final MediaPlayer player = MediaService.getPlayer();
            song = MediaService.getCurrentSong();
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
                                    if (!albumLoaded) {
                                        notifyAlbumLoadReady();
                                        titleBar.updateSongTitle(song.getTitle());
                                    }
                                    if (!loaded) {
                                        setButtonListeners();
                                        Extension.switchPlayButton(playButtons, false, context);
                                        loaded = true;
                                    }
                                    player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                        @Override
                                        public void onPrepared(MediaPlayer mp) {
                                            // Set the duration on the UI
                                            String duration = Extension.getTimeString(player.getDuration());
                                            txtDuration.setText(duration);
                                            seekBar.setMax(player.getDuration());
                                        }
                                    });
                                    // Set the current time on the UI
                                    String currentTime = Extension.getTimeString(player.getCurrentPosition());
                                    txtPassed.setText(currentTime);
                                    // Set the seek bar max position, current position, and change listener
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
                                            // TODO Auto-generated method stub

                                        }

                                        @Override
                                        public void onStopTrackingTouch(SeekBar seekBar) {
                                            // TODO Auto-generated method stub

                                        }
                                    });
                                }
                            });
                        }
                    }
                }
            }).start();
        } catch (MediaNotFoundException e) {
            Log.w(TAG, e.getMessage(), e);
        }
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
                titleBar.updateAlbumArt(album);
                titleBar.updateAlbumTitle(album.getAlbumName());
                // Load album art
                Extension.updateAlbumArt(album, imgAlbumArt);
                // Albums loaded!
                albumLoaded = true;
            }
        } catch (MediaNotFoundException e) {
            Log.d(TAG, e.getMessage(), e);
        }
    }

    private void setButtonListeners() {
        final MediaPlayer player = MediaService.getPlayer();
        btnPrevious.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Extension.setPlayButtonListener(playButtons, player, context);
            }
        });
        btnForward.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        titleBar.setPlayButtonListener(playButtons, player);
    }

    private void initialize(Context context) {
        this.context = context;
        playButtons = new ArrayList<>();

        // Register this as listeners
        MediaCache.registerAlbumLoadListener(this);
        MediaService.registerNowPlayingView(this);

        titleBar = (NowPlayingTitleBar) findViewById(R.id.now_playing_title_bar);

        txtSongTitle = (TextView) findViewById(R.id.now_playing_title);
        txtAlbum = (TextView) findViewById(R.id.now_playing_album);
        imgAlbumArt = (ImageView) findViewById(R.id.now_playing_album_art);

        txtPassed = (TextView) findViewById(R.id.now_playing_passed);
        txtDuration = (TextView) findViewById(R.id.now_playing_duration);
        seekBar = (SeekBar) findViewById(R.id.now_playing_seek);

        // Set the buttons
        btnPrevious = (ImageView) findViewById(R.id.btn_previous);
        btnPlay = (ImageView) findViewById(R.id.btn_play);
        btnForward = (ImageView) findViewById(R.id.btn_forward);

        playButtons.add(btnPlay);
        playButtons.add(titleBar.getPlayButton());

        setSlidable(titleBar);
        setContent(findViewById(R.id.now_playing_content));

        // Load controls
        seekBar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.Red), PorterDuff.Mode.MULTIPLY));
        seekBar.getThumb().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.Red), PorterDuff.Mode.SRC_IN));

        int rippleColor = R.color.FireBrick;
        Extension.setRippleBackground(btnPrevious, rippleColor, 0, context);
        Extension.setRippleBackground(btnPlay, rippleColor, 0, context);
        Extension.setRippleBackground(btnForward, rippleColor, 0, context);
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