package com.mcnedward.bramble.view.nowPlaying;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.mcnedward.bramble.R;

/**
 * Created by edward on 27/12/15.
 */
public abstract class SlidingView extends RelativeLayout implements View.OnTouchListener {
    private final static String TAG = "SlidingView";

    private Context context;

    private ViewGroup root;
    private View mSlider;
    private View mContent;
    private View bottom;
    private boolean mLockToBottom;

    private boolean mControlsTouched;
    protected boolean contentFocused = false;

    public SlidingView(int resourceId, Context context) {
        super(context);
        initialize(resourceId, context);
    }

    public SlidingView(int resourceId, Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(resourceId, context);
    }

    private void initialize(int resourceId, Context context) {
        this.context = context;
        inflate(context, resourceId, this);
        setOnTouchListener(this);
        bottom = findViewById(R.id.now_playing_bottom_control);
    }

    protected abstract void switchSliderIcon(boolean top);

    public boolean doTouchAction(View v, MotionEvent event) {
        int action = event.getAction();
        int eventY = (int) event.getRawY();
        int anchorY = 0;
        int contentY = (int) mContent.getY();
        int sliderHeight = mSlider.getHeight();
        int topBounds = root.getHeight() / 5;
        int bottomBounds = (int) (root.getHeight() * 0.8);

        Log.d(TAG, "X: " + event.getX() + "; Y: " + eventY);

        ViewConfiguration vc = ViewConfiguration.get(mSlider.getContext());
        int touchSlop = vc.getScaledTouchSlop();

        switch (action) {
            case MotionEvent.ACTION_MOVE:
//                if (mControlsTouched) {
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
                    if (root != null && eventY >= root.getHeight()) {
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
                    if (root != null && eventY > bottomBounds) {
                        // Content is in bottom half
                        switchSliderIcon(false);
                    } else {
                        switchSliderIcon(true);
                    }
//                    return true;
//                }
                break;
            case MotionEvent.ACTION_UP:
//                if (mControlsTouched) {
                    if (mLockToBottom) {
                        // Moving from top to bottom, but finger went off screen
                        mLockToBottom = false;
                    } else if (eventY > touchSlop) {
                        // Handle a single tap
                        if (contentFocused) {
                            animateToBottom();
                        } else {
                            animateToTop();
                        }
                    } else {
                        if (eventY > root.getHeight()) {    // Finger moved off bottom of screen
                            snapToBottom();
                            mControlsTouched = false;
                            return true;
                        }
                        if (contentFocused) {   // View is up on top
                            if (root != null && eventY < topBounds) {
                                animateToTop();
                            } else {
                                animateToBottom();
                            }
                        } else {
                            if (root != null && eventY < bottomBounds) {
                                animateToTop();
                            } else {
                                animateToBottom();
                            }
                        }
                        mControlsTouched = false;
                        return true;
                    }
//                }
                mControlsTouched = false;
                break;
        }
        mContent.invalidate();
        // Allow the underlying view to handle touch events when the sliding view is not focused
        return contentFocused;
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
        if (root != null) {
            int contentY = (int) (mContent.getY() + mSlider.getHeight());
            int animationDistance = Math.abs(contentY - root.getHeight());
            mContent.animate().translationYBy(animationDistance);
            mSlider.animate().alpha(1.0f).setDuration(duration);
            contentFocused = false;
        }
    }

    public void animateToTop() {
        switchSliderIcon(true);
        mContent.animate().translationY(0);
        contentFocused = true;
    }

    public void snapToBottom() {
        snapToBottom(root.getHeight() - mSlider.getHeight());
    }

    public void snapToBottom(int position) {
        switchSliderIcon(false);
        mContent.setY(position);
        contentFocused = false;
    }

    public boolean isContentFocused() {
        return contentFocused;
    }

    public boolean isControlsTouched() {
        return mControlsTouched;
    }

    public void setControlsTouched(boolean controlsTouched) {
        this.mControlsTouched = controlsTouched;
    }

    public void updateViewMeasures(ViewGroup root) {
        this.root = root;
    }

    public void setSlidable(View slidable) {
        this.mSlider = slidable;
    }

    public int getSlidableHeight() {
        return mSlider.getHeight();
    }

    public void setContent(View content) {
        this.mContent = content;
    }
}
