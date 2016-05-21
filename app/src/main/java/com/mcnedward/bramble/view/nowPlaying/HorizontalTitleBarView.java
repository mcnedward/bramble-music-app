package com.mcnedward.bramble.view.nowPlaying;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Song;
import com.mcnedward.bramble.utils.RepositoryUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 5/20/2016.
 * Some help from: https://www.velir.com/blog/2010/11/17/android-creating-snapping-horizontal-scroll-view
 */
public class HorizontalTitleBarView extends HorizontalScrollView implements IScrollView {
    private static final String TAG = "HorizontalTitleBarView";

    private static final int SWIPE_MIN_DISTANCE = 5;
    private static final int SWIPE_THRESHOLD_VELOCITY = 300;

    private Context mContext;
    private LinearLayout mContainer;
    private List<IScrollView> mScrollViews;
    private GestureDetector mGestureDetector;
    private int mActiveIndex = 0;
    private SlidingView mParentView;
    private float mAnchorX;
    private boolean mIsScrolling;

    public HorizontalTitleBarView(Context context) {
        super(context);
        if (!isInEditMode())
            initialize(context);
    }

    public HorizontalTitleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            initialize(context);
    }

    public boolean doTouchAction(View v, MotionEvent event) {
        int action = event.getAction();

        // If this is a swipe
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
//            if (mIsScrolling) {
                int scrollX = getScrollX();
                int width = v.getMeasuredWidth();
                mActiveIndex = ((scrollX + (width / 2)) / width);
                int scrollTo = mActiveIndex * width;
                smoothScrollTo(scrollTo, 0);
                return true;
//            }
        } else {
            return false;
        }
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent event) {
//        /*
//         * This method JUST determines whether we want to intercept the motion.
//         * If we return true, onTouchEvent will be called and we do the actual
//         * scrolling there.
//         */
//        final int action = MotionEventCompat.getActionMasked(event);
//
//        // Always handle the case of the touch gesture being complete.
//        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
//            // Release the scroll.
//            mIsScrolling = false;
//            return false; // Do not intercept touch event, let the child handle it
//        }
//        switch (action) {
//            case MotionEvent.ACTION_DOWN: {
//                mAnchorX = event.getX();
//                break;
//            }
//            case MotionEvent.ACTION_MOVE: {
//                if (mIsScrolling) {
//                    // We're currently scrolling, so yes, intercept the touch event!
//                    return true;
//                }
//
//                // If the user has dragged his finger horizontally more than the touch slop, start the scroll
//                final int xDiff = (int) Math.abs(mAnchorX - event.getX());
//                ViewConfiguration vc = ViewConfiguration.get(mContext);
//                int touchSlop = vc.getScaledTouchSlop();
//                if (xDiff > touchSlop) {
//                    // Start scrolling!
//                    mIsScrolling = true;
//                    return true;
//                }
//                break;
//            }
//        }
//
//        // In general, we don't want to intercept touch events. They should be
//        // handled by the child view.
//        return false;
//    }

    private void initialize(Context context) {
        mContext = context;
        mContainer = new LinearLayout(mContext);
        mScrollViews = new ArrayList<>();

        mContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mContainer.setOrientation(LinearLayout.HORIZONTAL);
        addView(mContainer);
        mGestureDetector = new GestureDetector(mContext, new HorizontalGestureDetector());
        setOnTouchListener(null);
//        setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return doTouchAction(v, event);
//            }
//        });
    }

    public void update(Song song, Album album) {
        IScrollView activeView = getScrollView(mActiveIndex);
        if (activeView == null) {
            activeView = createView(song);
        }
        activeView.update(song, album);

        for (Song s : RepositoryUtil.getSongRepository(mContext).getSongsForAlbum(song.getAlbumId())) {
            createView(s);
        }
    }

    @Override
    public void slideUp(boolean top) {
        if (top) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
        }
    }

    @Override
    public void setSize(int width, int height) {

    }

    public IScrollView createView(Song song) {
        NowPlayingTitleBarView titleBarView = new NowPlayingTitleBarView(mContext, song);
        mContainer.addView(titleBarView);
        mScrollViews.add(titleBarView);
        return titleBarView;
    }

    public IScrollView getScrollView(int position) {
        if (mScrollViews == null || mScrollViews.isEmpty()) return null;
        return mScrollViews.get(position);
    }

    /**
     * This needs to be set in order for other touch events to work!
     *
     * @param view The parent view of the HorizontalScrollView.
     */
    public void setParentView(SlidingView view) {
        mParentView = view;
    }

    private boolean mMeasureLock;

    @Override
    public void onMeasure(int widthMeasure, int heightMeasure) {
        super.onMeasure(widthMeasure, heightMeasure);
        if (mScrollViews == null) return;
        if (!mMeasureLock) {
            for (IScrollView view : mScrollViews) {
                view.setSize(widthMeasure, LinearLayout.LayoutParams.MATCH_PARENT);
            }
            mMeasureLock = true;
        }
    }

    final class HorizontalGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                // Right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    int width = getMeasuredWidth();
                    mActiveIndex = (mActiveIndex < (mScrollViews.size() - 1)) ? mActiveIndex + 1 : mScrollViews.size() - 1;
                    smoothScrollTo(mActiveIndex * width, 0);
                    return true;
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    int width = getMeasuredWidth();
                    mActiveIndex = (mActiveIndex > 0) ? mActiveIndex - 1 : 0;
                    smoothScrollTo(mActiveIndex * width, 0);
                    return true;
                }
            } catch (Exception e) {
                Log.e(TAG, "Something went wrong when processing the Fling event: " + e.getMessage());
            }
            return false;
        }
    }
}
