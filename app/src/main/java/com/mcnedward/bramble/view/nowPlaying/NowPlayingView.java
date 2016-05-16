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
import com.mcnedward.bramble.exception.MediaNotFoundException;
import com.mcnedward.bramble.listener.MediaListener;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Song;
import com.mcnedward.bramble.repository.AlbumRepository;
import com.mcnedward.bramble.service.MediaService;
import com.mcnedward.bramble.utils.MusicUtil;
import com.mcnedward.bramble.utils.PicassoUtil;
import com.mcnedward.bramble.utils.RippleUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward on 27/12/15.
 */
public class NowPlayingView extends SlidingView implements MediaListener {
    private final static String TAG = "NowPlayingView";

    private Context context;
    private AlbumRepository mAlbumRepository;
    private Picasso picasso;
    private Song song;

    public NowPlayingView(Context context) {
        super(R.layout.view_now_playing, context);
        if (!isInEditMode())
            initialize(context);
    }

    public NowPlayingView(Context context, AttributeSet attrs) {
        super(R.layout.view_now_playing, context, attrs);
        if (!isInEditMode())
            initialize(context);
    }

    @Override
    protected void switchSlidable(boolean top) {
        titleBar.switchPlayIcon(top);
    }

    @Override
    public void notifyMediaStarted() {
        loadAlbum();
        updateButtons();
    }

    @Override
    public void update() {
        final MediaPlayer player = MediaService.getPlayer();
        // Set the current time on the UI
        String currentTime = MusicUtil.getTimeString(player.getCurrentPosition());
        txtPassed.setText(currentTime);

        // Set the remaining time on the UI
        String remainingTime = MusicUtil.getTimeString(Math.abs(player.getDuration() - player.getCurrentPosition()));
        txtDuration.setText(remainingTime);

        // Set the seek bar max position, current position, and change listener
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
                // TODO Auto-generated method stub

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    public View getView() {
        return this;
    }

    private void loadAlbum() {
        Song song = getSong();
        if (song == null)
            Log.e(TAG, "Could not load song.");
        else {
            Album album = mAlbumRepository.get(song.getAlbumId());
            titleBar.updateAlbumTitle(album.getAlbumName());
            // Load album art
            titleBar.updateAlbumArt(album);
            MusicUtil.loadAlbumArt(album.getAlbumArt(), imgAlbumArt, context);
        }
        titleBar.updateSongTitle(getSong().getTitle());
    }

    private void updateButtons() {
        setButtonListeners();
        MusicUtil.switchPlayButton(playButtons, false, context);
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
                MusicUtil.setPlayButtonListener(playButtons, player, context);
            }
        });
        btnForward.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        titleBar.setPlayButtonListener(playButtons, player);
    }

    private Song getSong() {
        Song song = null;
        try {
            song = MediaService.getCurrentSong();
        } catch (MediaNotFoundException e) {
            Log.w(TAG, e.getMessage(), e);
        }
        return song;
    }

    private void initialize(Context context) {
        this.context = context;
        mAlbumRepository = new AlbumRepository(context);
        picasso = PicassoUtil.getPicasso(context);

        setupViews();

        // Register this as listeners
        MediaService.attachMediaListener(this);
    }

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

    private void setupViews() {
        playButtons = new ArrayList<>();

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
        seekBar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.Red), PorterDuff.Mode
                .MULTIPLY));
        seekBar.getThumb().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.Red), PorterDuff.Mode.SRC_IN));

        int rippleColor = R.color.FireBrick;
        RippleUtil.setRippleBackground(btnPrevious, rippleColor, 0, context);
        RippleUtil.setRippleBackground(btnPlay, rippleColor, 0, context);
        RippleUtil.setRippleBackground(btnForward, rippleColor, 0, context);
    }

    public NowPlayingTitleBar getTitleBar() {
        return titleBar;
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