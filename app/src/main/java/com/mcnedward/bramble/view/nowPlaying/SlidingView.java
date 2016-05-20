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
    private View mSlider;
    private View mContent;
    private boolean mLockToBottom;
    private boolean mControlsTouched;
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

    public boolean doTouchAction(View v, MotionEvent event) {
        int action = event.getAction();
        int eventY = (int) event.getRawY();
        int contentY = (int) mContent.getY();
        int sliderHeight = mSlider.getHeight();
        int topBounds = mRoot.getHeight() / 5;
        int bottomBounds = (int) (mRoot.getHeight() * 0.8);

        ViewConfiguration vc = ViewConfiguration.get(mSlider.getContext());
        int touchSlop = vc.getScaledTouchSlop();

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                int newY;
                if (eventY > (contentY + sliderHeight)) {
                    // Moving down
                    newY = eventY - sliderHeight;
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
                break;
            case MotionEvent.ACTION_UP:
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
                        mControlsTouched = false;
                        return true;
                    }
                    if (mContentFocused) {   // View is up on top
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
                    mControlsTouched = false;
                    return true;
                }
                mControlsTouched = false;
                break;
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
            int contentY = (int) (mContent.getY() + mSlider.getHeight());
            int animationDistance = Math.abs(contentY - mRoot.getHeight());
            mContent.animate().translationYBy(animationDistance);
            mSlider.animate().alpha(1.0f).setDuration(duration);
            mContentFocused = false;
        }
    }

    public void animateToTop() {
        switchSliderIcon(true);
        mContent.animate().translationY(0);
        mContentFocused = true;
    }

    public void snapToBottom() {
        snapToBottom(mRoot.getHeight() - mSlider.getHeight());
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
        this.mRoot = root;
    }

    public void setSlider(View slider) {
        this.mSlider = slider;
    }

    public void setContent(View content) {
        this.mContent = content;
    }
}
