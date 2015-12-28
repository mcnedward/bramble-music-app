package com.mcnedward.bramble.view.nowPlaying;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
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

/**
 * Created by edward on 27/12/15.
 */
public class NowPlayingView extends SlidingView implements AlbumLoadListener {
    private final static String TAG = "NowPlayingView";

    private Context context;
    private Activity activity;

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

    public NowPlayingView(Context context, AttributeSet attrs) {
        super(R.layout.now_playing_view, context, attrs);
        this.context = context;
        activity = (Activity) context;

        initialize();

        setSlidable(bottomControls);
        setContent(findViewById(R.id.now_playing_content));

        MediaCache.registerAlbumLoadListener(this);
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

    public void notifyMediaStarted(Song song) {
        this.song = song;
        titleBar.setSongTitle(song.getTitle());
        bottomControls.setSongTitle(song);

        // Load controls
        seekBar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.Red), PorterDuff.Mode.MULTIPLY));
        seekBar.getThumb().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.Red), PorterDuff.Mode.SRC_IN));
        txtDuration.setText(song.getDuration());

        int rippleColor = R.color.FireBrick;
        Extension.setRippleBackground(btnPrevious, rippleColor, 0, context);
        Extension.setRippleBackground(btnPlay, rippleColor, 0, context);
        Extension.setRippleBackground(btnForward, rippleColor, 0, context);

        loaded = true;

        startUIDisplayThread();
    }

    private void startUIDisplayThread() {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final MediaPlayer player = MediaService.getMediaPlayer();
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
                        String currentTime = getTimeString(player.getCurrentPosition());
                        String duration = getTimeString(player.getDuration());

                        // Find the TextViews from the NowPlayingActivity and update UI
                        txtPassed.setText(currentTime);

                        txtDuration.setText(String.valueOf(duration));
                    }
                }
            });
        }
    }

    @Override
    public void notifyAlbumLoadReady() {
        Log.d(TAG, "Albums ready...");
        Album album;
        try {
            if (song == null) {
                MediaService.getCurrentSong();
                throw new MediaNotFoundException("Could not find the song...");
            }
            album = MainActivity.mediaCache.getAlbumForSong(song);
            bottomControls.updateAlbumArt(album);
            titleBar.setAlbumTitle(album.getAlbumName());
            // Load album art
            Extension.updateAlbumArt(album, imgAlbumArt);
        } catch (MediaNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void initialize() {
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

    /**
     * Used to format the time of the current media
     *
     * @param millis - The time in milliseconds to format
     * @return - The formatted time
     */
    private String getTimeString(long millis) {
        int minutes = (int) (millis / (1000 * 60));
        int seconds = (int) ((millis / 1000) % 60);
        return String.format("%d:%02d", minutes, seconds);
    }
}
