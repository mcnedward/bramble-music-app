package com.mcnedward.bramble.view.nowPlaying;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by edward on 27/12/15.
 * <p/>
 * A view that can be slid across the screen in a cross pattern. This means that it can move in either a vertical or horizontal motion. This
 * consists of a view for holding the content of everything, a title bar view that, when dragged up or down, can move the content up or down, and a
 * HorizontalSlidingView, that contains items that can be moved in a horizontal motion.
 */
public abstract class CrossSlidingView extends RelativeLayout implements View.OnTouchListener {
    private final static String TAG = "SlidingView";

    private static final int SWIPE_MIN_DISTANCE = 5;
    private static final int SWIPE_THRESHOLD_VELOCITY = 300;

    private ViewGroup mRoot;
    private View mContent;  // The main view that holds all the other views
    private View mTitleBar; // The title bar view that drags the content up or down
    private HorizontalSlidingView mHSlider; // The view that contains items to be moved in a horizontal motion
    private int mAnchorX, mAnchorY; // Anchor points that are set on MotionEvent.DOWN
    private boolean mLockToBottom;    // Flag for determining if the content should move down the screen any further
    private boolean mControlsTouched;   // Flag for determining if the title bar view is being touched, and therefore a drag can occur
    // Flag for determining if the main portion of the content is focused. Used so that content below this view can also be accessed, like if a
    // ListView or some other intractable view is available underneath
    protected boolean mContentFocused;
    private boolean mIsScrollingHorizontal; // Flag for determining if the horizontal view is being scrolled.
    // Flag for determining if the user has dragged the horizontal view, and the MotionEvent.UP action should be delegated to the
    // HorizontalSlidingView.
    private boolean mHorizontalMoveForUpAction;
    private GestureDetector mGestureDetector;

    public CrossSlidingView(int resourceId, Context context) {
        super(context);
        initialize(resourceId, context);
    }

    public CrossSlidingView(int resourceId, Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(resourceId, context);
    }

    private void initialize(int resourceId, Context context) {
        inflate(context, resourceId, this);
        setOnTouchListener(this);
        mGestureDetector = new GestureDetector(context, new HorizontalGestureDetector());
    }

    /**
     * Notifies that changes can be made to the view, for either a state when the view is on top and focused, or on the bottom and the underlying
     * content is focused.
     *
     * @param top True if this view is on top, false otherwise.
     */
    protected abstract void switchSliderIcon(boolean top);

    /**
     * Performs the action for the main content on the MotionEvent.MOVE.
     *
     * @param eventY         The y point of the move event.
     * @param contentY       The y position of the main content.
     * @param titleBarHeight The height of the draggable title bar.
     * @param bottomBounds   The bottom bounds of the main content view.
     */
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

    /**
     * Performs the action for the main content on the MotionEvent.UP.
     *
     * @param eventY       The y position of the event.
     * @param touchSlop    The touch slop, for determining if a single tap occurred.
     * @param topBounds    The top bounds of the main content view.
     * @param bottomBounds The bottom bounds of the main content view.
     */
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

    /**
     * Handles the touch action from the user, and delegates the actions to the proper views.
     *
     * @param v     The View that was touched.
     * @param event The MotionEvent.
     * @return True if this event was handled, false otherwise.
     */
    public boolean doTouchAction(View v, MotionEvent event) {
        // If this is a swipe
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }

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
                    mHSlider.doDownAction(eventX);  // Needed for setting the move anchor on the horizontal slider
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mControlsTouched) {
                    // If content focused, then we know we should only handle content move actions, the horizontal bar should be disabled
                    if (mContentFocused) {
                        doContentMoveAction(eventY, contentY, titleBarHeight, bottomBounds);
                        mHorizontalMoveForUpAction = false;
                    } else if (mIsScrollingHorizontal) {
                        mHSlider.doMoveAction(eventX);
                    } else {
                        int moveDiffX = Math.abs(mAnchorX - eventX);
                        int moveDiffY = Math.abs(mAnchorY - eventY);

                        if (moveDiffY > touchSlop) {
                            mIsScrollingHorizontal = false;
                            mHorizontalMoveForUpAction = false;
                            doContentMoveAction(eventY, contentY, titleBarHeight, bottomBounds);
                            mHSlider.moveToDefault();
                        } else if (moveDiffX > touchSlop) {
                            mIsScrollingHorizontal = true;
                            mHorizontalMoveForUpAction = true;
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
                } else if (mIsScrollingHorizontal || mHorizontalMoveForUpAction) {
                    mHSlider.doUpAction(eventX);
                } else {
                    doContentUpAction(eventY, touchSlop, topBounds, bottomBounds);
                    mHSlider.moveToDefault();
                }
                mIsScrollingHorizontal = false;
                mHorizontalMoveForUpAction = false;
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

    /**
     * Animates this view to the bottom position at an animation duration rate of 300 ms.
     */
    public void animateToBottom() {
        animateToBottom(300);
    }

    /**
     * Animates this view to the bottom position.
     *
     * @param duration The duration of the move animation, in milliseconds.
     */
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

    /**
     * Animates this view to the top position.
     */
    public void animateToTop() {
        switchSliderIcon(true);
        mContent.animate().translationY(0);
        mContentFocused = true;
    }

    /**
     * Forces this view into the bottom position, with no animation.
     */
    public void snapToBottom() {
        snapToBottom(mRoot.getHeight() - mTitleBar.getHeight());
    }

    /**
     * Forces this view to the specified y-position, with no animation.
     *
     * @param position The y-position of the view.
     */
    public void snapToBottom(int position) {
        switchSliderIcon(false);
        mContent.setY(position);
        mContentFocused = false;
    }

    /**
     * Determines if the main content of this view is currently focused, or if the view is in the bottom. When the view is in the bottom,
     * underlying content may be interacted with.
     *
     * @return True if the content is focused, false otherwise.
     */
    public boolean isContentFocused() {
        return mContentFocused;
    }

    /**
     * Updates the root view. This should be called in the onMeasure method of the Activity that contains this view.
     *
     * @param root The ViewGroup to set the root to.
     */
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

    final class HorizontalGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                // Right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    return mHSlider.doFlingAction(true);
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    return mHSlider.doFlingAction(false);
                }
            } catch (Exception e) {
                Log.e(TAG, "Something went wrong when processing the Fling event: " + e.getMessage());
            }
            return false;
        }
    }
}
