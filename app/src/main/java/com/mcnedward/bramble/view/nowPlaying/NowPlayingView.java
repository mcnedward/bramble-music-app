package com.mcnedward.bramble.view.nowPlaying;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.exception.EntityDoesNotExistException;
import com.mcnedward.bramble.listener.MediaChangeListener;
import com.mcnedward.bramble.entity.media.Album;
import com.mcnedward.bramble.entity.media.Song;
import com.mcnedward.bramble.repository.media.AlbumRepository;
import com.mcnedward.bramble.service.MediaService;
import com.mcnedward.bramble.utils.MediaCache;
import com.mcnedward.bramble.utils.MusicUtil;
import com.mcnedward.bramble.utils.RepositoryUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward on 27/12/15.
 */
public class NowPlayingView extends CrossSlidingView implements MediaChangeListener {
    private final static String TAG = "NowPlayingView";

    private Context mContext;
    private AlbumRepository mAlbumRepository;

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
    protected void switchSliderIcon(boolean top) {
        mTitleBar.slideUp(top);
        mNowPlayingTitleBarSliderView.slideUp(top);
    }

    private void loadAlbum() {
        // TODO I think this is being called twice? At least on next song start
        Song song = MediaCache.getSong(mContext);
        if (song == null) {
            Log.w(TAG, "No song is setup for play.");
        } else {
            Album album = null;
            try {
                album = mAlbumRepository.get(song.getAlbumId());
            } catch (EntityDoesNotExistException e) {
                Log.w(TAG, e.getMessage());
                return;
            }
            mTitleBar.update(song, album);

            List<String> songKeys = MediaCache.getSongKeys(mContext);
            if (songKeys != null) {
                List<Song> currentPlaylist = RepositoryUtil.getSongRepository(mContext).getSongsForKeys(songKeys);
                int currentIndex = MusicUtil.getSongIndexFromSongs(currentPlaylist, song);
                if (currentIndex != -1)
                    mNowPlayingTitleBarSliderView.setItems(currentPlaylist, currentIndex);
            }
            // Load album art
            MusicUtil.loadAlbumArt(mContext, album.getAlbumArt(), imgAlbumArt);
        }
    }

    private void initialize(Context context) {
        mContext = context;
        mAlbumRepository = new AlbumRepository(context);

        setupViews();
        loadAlbum();

        // Register this as listeners
        MediaService.attachMediaChangeListener(this);
    }

    private NowPlayingTitleBarView mTitleBar;
    private NowPlayingBottomControlView mBottomControl;
    private NowPlayingTitleBarSliderView mNowPlayingTitleBarSliderView;
    private ImageView imgAlbumArt;

    private void setupViews() {
        mTitleBar = (NowPlayingTitleBarView) findViewById(R.id.now_playing_title_bar);
        mBottomControl = (NowPlayingBottomControlView) findViewById(R.id.now_playing_bottom_control);
        imgAlbumArt = (ImageView) findViewById(R.id.now_playing_album_art);

        // Setup the slider
        RelativeLayout container = (RelativeLayout) findViewById(R.id.view_slider_bar);
        mNowPlayingTitleBarSliderView = new NowPlayingTitleBarSliderView(mContext, new ArrayList<Song>());
        container.addView(mNowPlayingTitleBarSliderView);

        setTitleBar(mTitleBar);
        setHorizontalSlider(mNowPlayingTitleBarSliderView);
        setContent(findViewById(R.id.now_playing_content));
    }

    public NowPlayingTitleBarView getTitleBar() {
        return mTitleBar;
    }

    public NowPlayingBottomControlView getBottomControl() {
        return mBottomControl;
    }

    @Override
    public void onMediaPlayStateChange(Song currentSong, boolean playing) {

    }

    @Override
    public void onMediaChange(Song song, boolean playing) {
        loadAlbum();
    }

    @Override
    public void onMediaStop(Song song) {

    }
}