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
    private View slidable;
    private View content;
    private View bottom;
    private boolean mLockToBottom;

    private boolean controlsTouched;
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

    protected abstract void switchSlidable(boolean top);

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        int eventY = (int) event.getY();
        int anchorY = 0;
        int slidableY = (int) content.getY();
        int slidableHeight = slidable.getHeight();
        int topBounds = root.getHeight() / 5;
        int bottomBounds = (int) (root.getHeight() * 0.8);

        ViewConfiguration vc = ViewConfiguration.get(slidable.getContext());
        int touchSlop = vc.getScaledTouchSlop();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                anchorY = eventY;
                if (anchorY > slidableY && anchorY < (slidableY + slidableHeight)) {
                    controlsTouched = true;
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (controlsTouched) {
                    int newY;
                    if (eventY > (slidableY + slidableHeight)) {
                        // Moving down
                        newY = eventY - slidableHeight;
                    } else {
                        // Moving up
                        int diff = Math.abs(eventY - slidableY);
                        newY = eventY - diff;
                    }
                    content.setY(newY);
                    if (root != null && eventY >= root.getHeight()) {
                        // Prevent moving below bottom of screen
                        snapToBottom();
                        mLockToBottom = true;
                    }
                    if (eventY < 0) {
                        // Prevent moving above top of screen
                        // -5 to take shadow into account
                        content.setY(-5);
                        switchSlidable(true);
                    }
                    if (root != null && eventY > bottomBounds) {
                        // Content is in bottom half
                        switchSlidable(false);
                    } else {
                        switchSlidable(true);
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (controlsTouched) {
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
                            controlsTouched = false;
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
                        controlsTouched = false;
                        return true;
                    }
                }
                controlsTouched = false;
                break;
        }
        // Allow the underlying view to handle touch events when the sliding view is not focused
        return contentFocused;
    }

    public void animateToBottom() {
        animateToBottom(300);
    }

    public void animateToBottom(int duration) {
        switchSlidable(false);
        if (root != null) {
            int contentY = (int) (content.getY() + slidable.getHeight());
            int animationDistance = Math.abs(contentY - root.getHeight());
            content.animate().translationYBy(animationDistance);
            slidable.animate().alpha(1.0f).setDuration(duration);
            contentFocused = false;
        }
    }

    public void animateToTop() {
        switchSlidable(true);
        content.animate().translationY(0);
        contentFocused = true;
    }

    public void snapToBottom() {
        snapToBottom(root.getHeight() - slidable.getHeight());
    }

    public void snapToBottom(int position) {
        switchSlidable(false);
        content.setY(position);
        contentFocused = false;
    }

    public boolean isContentFocused() {
        return contentFocused;
    }

    public boolean isControlsTouched() {
        return controlsTouched;
    }

    public void updateViewMeasures(ViewGroup root) {
        this.root = root;
    }

    public void setSlidable(View slidable) {
        this.slidable = slidable;
    }

    public int getSlidableHeight() {
        return slidable.getHeight();
    }

    public void setContent(View content) {
        this.content = content;
    }
}
