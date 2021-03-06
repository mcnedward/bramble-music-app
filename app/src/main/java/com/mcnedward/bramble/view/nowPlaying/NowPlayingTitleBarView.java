package com.mcnedward.bramble.view.nowPlaying;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.exception.EntityDoesNotExistException;
import com.mcnedward.bramble.listener.MediaChangeListener;
import com.mcnedward.bramble.entity.media.Album;
import com.mcnedward.bramble.entity.media.Song;
import com.mcnedward.bramble.service.MediaService;
import com.mcnedward.bramble.utils.MediaCache;
import com.mcnedward.bramble.utils.MusicUtil;
import com.mcnedward.bramble.utils.RepositoryUtil;
import com.mcnedward.bramble.utils.RippleUtil;

/**
 * Created by edward on 27/12/15.
 */
public class NowPlayingTitleBarView extends LinearLayout implements MediaChangeListener, IScrollView {
    private final static String TAG = "NowPlayingTitleBar";

    private Context mContext;
    private Song mSong;

    private ImageView mImgAlbumArt;
    private TextView mTxtNowPlayingTitle;
    private TextView mTxtNowPlayingAlbum;
    private ImageView mBtnPlay;

    public NowPlayingTitleBarView(Context context, Song song) {
        super(context);
        initialize(context);
        update(song, null);
    }

    public NowPlayingTitleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    private void initialize(Context context) {
        mContext = context;
        inflate(context, R.layout.view_now_playing_title_bar, this);
        mImgAlbumArt = (ImageView) findViewById(R.id.now_playing_bottom_album_art);
        mTxtNowPlayingTitle = (TextView) findViewById(R.id.now_playing_title);
        mTxtNowPlayingAlbum = (TextView) findViewById(R.id.now_playing_album);
        mBtnPlay = (ImageView) findViewById(R.id.now_playing_title_play);
        RippleUtil.setRippleBackground(mBtnPlay, R.color.FireBrick, 0, context);

        MediaService.attachMediaChangeListener(this);
        mBtnPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicUtil.doPlayButtonAction(mContext);
            }
        });
    }

    @Override
    public void update(Song song, Album album) {
        if (song == null) {
            song = MediaCache.getSong(mContext);
        }
        if (album == null || song == null) {
            try {
                album = RepositoryUtil.getAlbumRepository(mContext).get(song.getAlbumId());
            } catch (EntityDoesNotExistException e) {
                Log.w(TAG, e.getMessage());
                return;
            }
        }
        MusicUtil.loadAlbumArt(mContext, album.getAlbumArt(), mImgAlbumArt);
        mTxtNowPlayingAlbum.setText(album.getTitle());
        mTxtNowPlayingTitle.setText(song.getTitle());
    }

    @Override
    public void slideUp(boolean top) {
        if (top) {
            updatePlayButton(0f, GONE);
            updateOpacity(0.75f);
        } else {
            updatePlayButton(1f, VISIBLE);
            updateOpacity(1f);
        }
    }

    @Override
    public void setSize(int width, int height) {
        setLayoutParams(new LayoutParams(width, height));
    }

    private void updatePlayButton(float alpha, int visibility) {
        mBtnPlay.animate().alpha(alpha).setDuration(100);
        mBtnPlay.setVisibility(visibility);
    }

    private void updateOpacity(float alpha) {
        animate().alpha(alpha).setDuration(100);
    }

    @Override
    public void onMediaPlayStateChange(Song currentSong, boolean playing) {
        MusicUtil.switchPlayButton(mContext, mBtnPlay, playing);
    }

    @Override
    public void onMediaChange(Song currentSong, boolean playing) {
        update(currentSong, null);
    }

    @Override
    public void onMediaStop(Song song) {

    }
}
