package com.mcnedward.bramble.view.nowPlaying;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
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
import com.mcnedward.bramble.utils.listener.MediaPlayingListener;

/**
 * Created by edward on 27/12/15.
 */
public class NowPlayingView extends SlidingView implements AlbumLoadListener {
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

    private MediaPlayingListener listener;

    public void register(MediaPlayingListener l) {
        listener = l;
    }

    public void notifyMediaStarted(Song song) {
        this.song = song;
        notifyAlbumLoadReady();

        titleBar.setSongTitle(song.getTitle());
        bottomControls.setSongTitle(song.getTitle());

        loaded = true;

        listener.notifyMediaPlaying();
    }

    public void updateTitle(String title) {
        titleBar.setSongTitle(title);
    }

    public void simpleUIUpdate(String passed, String current) {
        txtPassed.setText(passed);
        txtDuration.setText(current);
    }

    public void updateUIThread() {
        final Handler handler = new Handler(Looper.getMainLooper());
        final MediaPlayer player = MediaService.getPlayer();
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                while (player != null && player.getCurrentPosition() < player.getDuration()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                    if (isContentFocused()) {
//                        txtPassed.setText(getTimeString(player.getCurrentPosition()));
//                        txtDuration.setText(getTimeString(player.getDuration()));
                    }
                }
            }
        });
    }

    public void updateUIThread(final MediaPlayer player) {
        if (context != null && isContentFocused()) {
//            fActivity.runOnUiThread(
//            new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                    if (player != null && player.getCurrentPosition() < player.getDuration()) {
//                        if (player.isPlaying() && player.getCurrentPosition() < player.getDuration()) {
            // Sleep for 100 milliseconds
//                            try {
//                                Thread.sleep(100);
//                            } catch (InterruptedException e) {
//                                Log.e(TAG, e.getMessage(), e);
//                            }
            Log.d(TAG, "CURRENT TIME: " + player.getCurrentPosition());
//            txtPassed.setText(getTimeString(player.getCurrentPosition()));
//            txtDuration.setText(getTimeString(player.getDuration()));
//                            seekBar.setMax(player.getDuration());
//                            seekBar.setProgress(player.getCurrentPosition());
//                            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                                @Override
//                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                                    if (fromUser == true) {
//                                        player.seekTo(seekBar.getProgress());
//                                    } else {
//                                        // Do nothing
//                                    }
//                                }
//
//                                @Override
//                                public void onStartTrackingTouch(SeekBar seekBar) {
//
//                                }
//
//                                @Override
//                                public void onStopTrackingTouch(SeekBar seekBar) {
//
//                                }
//                            });
//                            String currentTime = getTimeString(player.getCurrentPosition());
//                            String duration = getTimeString(player.getDuration());
//
//                            // Find the TextViews from the NowPlayingActivity and update UI
//                            txtPassed.setText(currentTime);
//
//                            txtDuration.setText(String.valueOf(duration));
//                        }
//                    }
//                    } catch (InterruptedException e) {
//                        Log.e(TAG, e.getMessage(), e);
//                    }
//                }
//            });
//            }
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
                bottomControls.updateAlbumArt(album);
                titleBar.setAlbumTitle(album.getAlbumName());
                // Load album art
                Extension.updateAlbumArt(album, imgAlbumArt);
            }
        } catch (MediaNotFoundException e) {
            Log.d(TAG, e.getMessage(), e);
        }
    }

    private void initialize(Context context) {
        this.context = context;

        MediaCache.registerAlbumLoadListener(this);
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