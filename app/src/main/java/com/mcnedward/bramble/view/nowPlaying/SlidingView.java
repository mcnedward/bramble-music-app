package com.mcnedward.bramble.view.nowPlaying;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by edward on 27/12/15.
 */
public abstract class SlidingView extends RelativeLayout implements View.OnTouchListener {
    private final static String TAG = "SlidingView";

    private ViewGroup mRoot;
    private View mTitleBar;
    private View mContent;
    private HorizontalSlidingView mHSlider;
    private int mAnchorX, mAnchorY;
    private boolean mLockToBottom, mControlsTouched;
    protected boolean mContentFocused = false;

    public SlidingView(int resourceId, Context context) {
        super(context);
        initialize(resourceId, context);
    }

    public SlidingView(int resourceId, Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(resourceId, context);
    }

    private void initialize(int resourceId, Context context) {
        inflate(context, resourceId, this);
        setOnTouchListener(this);
    }

    protected abstract void switchSliderIcon(boolean top);

    private void doContentMoveAction(int eventY, int contentY, int titleBarHeight, int bottomBounds) {
        int newY;
        if (eventY > (contentY + titleBarHeight)) {
            // Moving down
            newY = eventY - titleBarHeight;
        } else {
            // Moving up
            int diff = Math.abs(eventY - contentY);
            newY = eventY - diff;
        }
        mContent.setY(newY);
        if (mRoot != null && eventY >= mRoot.getHeight()) {
            // Prevent moving below bottom of screen
            snapToBottom();
            mLockToBottom = true;
        }
        if (eventY < 0) {
            // Prevent moving above top of screen
            // -5 to take shadow into account
            mContent.setY(-5);
            switchSliderIcon(true);
        }
        if (mRoot != null && eventY > bottomBounds) {
            // Content is in bottom half
            switchSliderIcon(false);
        } else {
            switchSliderIcon(true);
        }
    }

    private void doContentUpAction(int eventY, int touchSlop, int topBounds, int bottomBounds) {
        if (mLockToBottom) {
            // Moving from top to bottom, but finger went off screen
            mLockToBottom = false;
        } else if (eventY > touchSlop) {
            // Handle a single tap
            if (mContentFocused) {
                animateToBottom();
            } else {
                animateToTop();
            }
        } else {
            if (eventY > mRoot.getHeight()) {    // Finger moved off bottom of screen
                snapToBottom();
            } else if (mContentFocused) {   // View is up on top
                if (mRoot != null && eventY < topBounds) {
                    animateToTop();
                } else {
                    animateToBottom();
                }
            } else {
                if (mRoot != null && eventY < bottomBounds) {
                    animateToTop();
                } else {
                    animateToBottom();
                }
            }
        }
    }

    private boolean mHorizontalMove = false;

    public boolean doTouchAction(View v, MotionEvent event) {
        int action = event.getAction();
        int eventX = (int) event.getX();
        int eventY = (int) event.getY();
        int contentY = (int) mContent.getY();
        int titleBarHeight = mTitleBar.getHeight();
        int topBounds = mRoot.getHeight() / 5;
        int bottomBounds = (int) (mRoot.getHeight() * 0.95);

        ViewConfiguration vc = ViewConfiguration.get(getContext());
        int touchSlop = vc.getScaledTouchSlop();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mAnchorX = eventX;
                mAnchorY = eventY;
                if (eventY > contentY && eventY < (contentY + titleBarHeight)) {
                    mControlsTouched = true;
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mControlsTouched) {
                    // If content focused, then we know we should only handle content move actions, the horizontal bar should be disabled
                    if (mContentFocused) {
                        doContentMoveAction(eventY, contentY, titleBarHeight, bottomBounds);
                        mHorizontalMove = false;
                    } else {
                        int moveDiffX = Math.abs(mAnchorX - eventX);
                        int moveDiffY = Math.abs(mAnchorY - eventY);

                        if (moveDiffY > touchSlop) {
                            mHorizontalMove = false;
                            doContentMoveAction(eventY, contentY, titleBarHeight, bottomBounds);
                            mHSlider.moveToDefault();
                        } else if (moveDiffX > touchSlop) {
                            mHorizontalMove = true;
                            mHSlider.doMoveAction(eventX);
                        }
                    }
                    return true;
                }
                break;
        }
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            if (mControlsTouched) {
                if (mContentFocused) {
                    doContentUpAction(eventY, touchSlop, topBounds, bottomBounds);
                } else {
                    if (mHorizontalMove) {
                        mHSlider.doUpAction(eventX);
                    } else {
                        doContentUpAction(eventY, touchSlop, topBounds, bottomBounds);
                        mHSlider.moveToDefault();
                    }
                }
                mHorizontalMove = false;
                return true;    // Event handled
            }
        }
        // Allow the underlying view to handle touch events when the sliding view is not focused
        return mContentFocused;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return doTouchAction(v, event);
    }

    public void animateToBottom() {
        animateToBottom(300);
    }

    public void animateToBottom(int duration) {
        switchSliderIcon(false);
        if (mRoot != null) {
            int contentY = (int) (mContent.getY() + mTitleBar.getHeight());
            int animationDistance = Math.abs(contentY - mRoot.getHeight());
            mContent.animate().translationYBy(animationDistance);
            mTitleBar.animate().alpha(1.0f).setDuration(duration);
            mContentFocused = false;
        }
    }

    public void animateToTop() {
        switchSliderIcon(true);
        mContent.animate().translationY(0);
        mContentFocused = true;
    }

    public void snapToBottom() {
        snapToBottom(mRoot.getHeight() - mTitleBar.getHeight());
    }

    public void snapToBottom(int position) {
        switchSliderIcon(false);
        mContent.setY(position);
        mContentFocused = false;
    }

    public boolean isContentFocused() {
        return mContentFocused;
    }

    public void updateViewMeasures(ViewGroup root) {
        mRoot = root;
    }

    public void setTitleBar(View titleBar) {
        mTitleBar = titleBar;
    }

    public void setHorizontalSlider(HorizontalSlidingView horizontalSlider) {
        mHSlider = horizontalSlider;
    }

    public void setContent(View content) {
        mContent = content;
    }
}
