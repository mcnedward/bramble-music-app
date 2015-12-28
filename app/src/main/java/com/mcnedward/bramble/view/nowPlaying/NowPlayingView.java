package com.mcnedward.bramble.view.nowPlaying;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.activity.MainActivity;
import com.mcnedward.bramble.activity.NowPlayingActivity;
import com.mcnedward.bramble.exception.MediaNotFoundException;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Song;
import com.mcnedward.bramble.service.MediaService;
import com.mcnedward.bramble.utils.Extension;

/**
 * Created by edward on 27/12/15.
 */
public class NowPlayingView extends SlidingView {
    private final static String TAG = "NowPlayingView";

    private Context context;

    private Song song;
    private Album album;

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

        initialize();

        setSlidable(bottomControls);
        setContent(findViewById(R.id.now_playing_content));
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

    public void loadNowPlaying() {
        try {
            album = MainActivity.mediaCache.getAlbumForSong(song);
            bottomControls.setBottomControlsInfo(song, album);
            titleBar.setTitleText(song.getTitle(), album.getAlbumName());
        } catch (MediaNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        // Load album art
        Extension.updateAlbumArt(album, imgAlbumArt);

        // Load controls
        seekBar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.Red), PorterDuff.Mode.MULTIPLY));
        seekBar.getThumb().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.Red), PorterDuff.Mode.SRC_IN));
        txtPassed.setText("0:00");
        txtDuration.setText(song.getDuration());

        int rippleColor = R.color.FireBrick;
        int backgroundColor = 0;
        btnPrevious.setBackground(Extension.rippleDrawable(rippleColor, backgroundColor, context));
        btnPlay.setBackground(Extension.rippleDrawable(rippleColor, backgroundColor, context));
        btnForward.setBackground(Extension.rippleDrawable(rippleColor, backgroundColor, context));

//        startPlayingMedia();

        loaded = true;
    }

    private void startPlayingMedia() {
        // Start playing music!
        Log.d(TAG, String.format("Starting to play '%s' from %s!", song, TAG));
        Intent intent = new Intent(context, MediaService.class);
        intent.putExtra("song", song);
        context.startService(intent);
        MediaService.getInstance().registerNowPlayingActivity((NowPlayingActivity)context);
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

    public void setSong(Song song) {
        this.song = song;
    }

}
