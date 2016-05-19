package com.mcnedward.bramble.view.nowPlaying;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.listener.MediaChangeListener;
import com.mcnedward.bramble.listener.MediaPlayingListener;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Song;
import com.mcnedward.bramble.service.MediaService;
import com.mcnedward.bramble.utils.MusicUtil;
import com.mcnedward.bramble.utils.RippleUtil;

import java.util.List;

/**
 * Created by Edward on 5/19/2016.
 */
public class NowPlayingBottomControlView extends LinearLayout implements MediaPlayingListener, MediaChangeListener {

    private Context mContext;
    private SeekBar mSeekBar;
    private TextView mTxtPassed;
    private TextView mTxtDuration;
    private ImageView mBtnPrevious;
    private ImageView mBtnPlay;
    private ImageView mBtnForward;
    private ImageView mBtnLoop;
    private ImageView mBtnShuffle;

    public NowPlayingBottomControlView(Context context) {
        super(context);
        initialize(context);
    }

    public NowPlayingBottomControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public void update() {
        final MediaPlayer player = MediaService.getPlayer();
        // Set the current time on the UI
        String currentTime = MusicUtil.getTimeString(player.getCurrentPosition());
        mTxtPassed.setText(currentTime);

        // Set the remaining time on the UI
        String remainingTime = MusicUtil.getTimeString(Math.abs(player.getDuration() - player.getCurrentPosition()));
        mTxtDuration.setText(remainingTime);

        // Set the seek bar max position, current position, and change listener
        mSeekBar.setMax(player.getDuration());
        mSeekBar.setProgress(player.getCurrentPosition());
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

    private void initialize(Context context) {
        mContext = context;
        inflate(mContext, R.layout.view_now_playing_bottom_control, this);

        mTxtPassed = (TextView) findViewById(R.id.now_playing_passed);
        mTxtDuration = (TextView) findViewById(R.id.now_playing_duration);
        mSeekBar = (SeekBar) findViewById(R.id.now_playing_seek);

        // Set the buttons
        mBtnPrevious = (ImageView) findViewById(R.id.btn_previous);
        mBtnPlay = (ImageView) findViewById(R.id.btn_play);
        mBtnForward = (ImageView) findViewById(R.id.btn_forward);
        mBtnLoop = (ImageView) findViewById(R.id.btn_loop);
        mBtnShuffle = (ImageView) findViewById(R.id.btn_shuffle);

        setButtonListeners();

        // Load controls
        mSeekBar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(mContext, R.color.Red), PorterDuff.Mode
                .MULTIPLY));
        mSeekBar.getThumb().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(mContext, R.color.Red), PorterDuff.Mode.SRC_IN));

        int rippleColor = R.color.FireBrick;
        RippleUtil.setRippleBackground(mBtnPrevious, rippleColor, 0, mContext);
        RippleUtil.setRippleBackground(mBtnPlay, rippleColor, 0, mContext);
        RippleUtil.setRippleBackground(mBtnForward, rippleColor, 0, mContext);
        RippleUtil.setRippleBackground(mBtnLoop, rippleColor, 0, mContext);
        RippleUtil.setRippleBackground(mBtnShuffle, rippleColor, 0, mContext);

        MediaService.attachMediaPlayingListener(this);
        MediaService.attachMediaChangeListener(this);
    }

    private void setButtonListeners() {
        mBtnPrevious.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicUtil.doPreviousButtonAction(mContext);
            }
        });
        mBtnPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicUtil.doPlayButtonAction(mBtnPlay, mContext);
            }
        });
        mBtnForward.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicUtil.doForwardButtonAction(mContext);
            }
        });
    }

    @Override
    public void update(Song song, Album album) {
        update();
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void notifyMediaChange(Song currentSong, boolean playing) {
        MusicUtil.switchPlayButton(mBtnPlay, playing, mContext);
    }
}
