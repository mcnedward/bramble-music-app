package com.mcnedward.bramble.view.nowPlaying;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.listener.MediaChangeListener;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Song;
import com.mcnedward.bramble.repository.AlbumRepository;
import com.mcnedward.bramble.service.MediaService;
import com.mcnedward.bramble.utils.MediaCache;
import com.mcnedward.bramble.utils.MusicUtil;
import com.mcnedward.bramble.utils.RepositoryUtil;

import java.util.ArrayList;

/**
 * Created by edward on 27/12/15.
 */
public class NowPlayingView extends SlidingView implements MediaChangeListener {
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
//        mHorBar.slideUp(top);
    }

    @Override
    public void notifyMediaChange(Song song, boolean playing) {
        loadAlbum();
    }

    private void loadAlbum() {
        // TODO I think this is being called twice? At least on next song start
        Song song = MediaCache.getSong(mContext);
        if (song == null) {
            Log.w(TAG, "No song is setup for play.");
        } else {
            Album album = mAlbumRepository.get(song.getAlbumId());
            if (album == null) return;
            mTitleBar.update(song, album);
            mSlider.setItems(RepositoryUtil.getSongRepository(mContext).getSongsForAlbum(album.getId()));
            // Load album art
            MusicUtil.loadAlbumArt(album.getAlbumArt(), imgAlbumArt, mContext);
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
    private Slider mSlider;
    private ImageView imgAlbumArt;

    private void setupViews() {
        mTitleBar = (NowPlayingTitleBarView) findViewById(R.id.now_playing_title_bar);
        mBottomControl = (NowPlayingBottomControlView) findViewById(R.id.now_playing_bottom_control);
        imgAlbumArt = (ImageView) findViewById(R.id.now_playing_album_art);

        // Setup the slider
        RelativeLayout container = (RelativeLayout) findViewById(R.id.view_slider_bar);
        mSlider = new Slider(mContext, new ArrayList<Song>());
        container.addView(mSlider);

        setTitleBar(mTitleBar);
        setHorizontalSlider(mSlider);
        setContent(findViewById(R.id.now_playing_content));
    }

    public NowPlayingTitleBarView getTitleBar() {
        return mTitleBar;
    }

    public NowPlayingBottomControlView getBottomControl() { return mBottomControl; }

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        if (mIsMoving) {
//            if (mMovingHorizontal) {
//                return mSlider.onTouch(v, event);
//            } else {
//                return doTouchAction(v, event, mAnchorX, mAnchorY);
//            }
//        }
//        return false;
//    }
//
//    private int mAnchorX, mAnchorY;
//    private boolean mIsMoving;
//    private boolean mMovingHorizontal;
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent event) {
//        final int action = MotionEventCompat.getActionMasked(event);
//        // Always handle the case of the touch gesture being complete.
//        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
//            return false; // Do not intercept touch event, let the child handle it
//        }
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                mAnchorX = (int) event.getX();
//                mAnchorY = (int) event.getY();
//                return false;
//            case MotionEvent.ACTION_MOVE: {
//                int moveDiffX = (int) Math.abs(mAnchorX - event.getX());
//                int moveDiffY = (int) Math.abs(mAnchorY - event.getY());
//
//                ViewConfiguration vc = ViewConfiguration.get(mContext);
//                int touchSlop = vc.getScaledTouchSlop();
//
//                if (moveDiffX > touchSlop) {
//                    Log.d(TAG, "MOVING HORIZONTAL");
//                    mMovingHorizontal = true;
//                    mIsMoving = true;
//                    return true;
//                }
//                if (moveDiffY > touchSlop) {
//                    Log.d(TAG, "MOVING VERTICAL");
//                    mMovingHorizontal = false;
//                    mIsMoving = true;
//                    return true;
//                }
//                break;
//            }
//        }
//        return false;
//    }

}