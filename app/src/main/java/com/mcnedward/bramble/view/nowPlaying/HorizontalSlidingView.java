package com.mcnedward.bramble.view.nowPlaying;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward on 27/12/15.
 * <p/>
 * This is a view that can be slid in a horizontal motion.
 */
public abstract class HorizontalSlidingView<T> extends RelativeLayout {
    private final static String TAG = "SlidingView";

    private static final int MOVE_DURATION = 300;

    protected Context mContext;
    private List<T> mItems;  // The list of items to use in this view
    private int mActiveIndex;   // The currently active index of this view
    private View[] mRecycleViews;   // An array of Views that can be recycled
    private ViewGroup mRoot;    // The root of this view
    private View mSlider;   // The main slider
    private View mSliderReplacement;    // The replacement slider
    private int mMoveAnchor;    // An anchor for where the movement last took place
    private int mAnchorX;   // The anchor position of the x on the MotionEvent.DOWN
    private boolean mMovingRight;  // Flag for determining if the view if moving to right or not

    public HorizontalSlidingView(Context context, List<T> songs) {
        super(context);
        initialize(context, songs);
    }

    public HorizontalSlidingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, new ArrayList<T>());
    }

    private void initialize(Context context, List<T> songs) {
        this.mContext = context;
        mRoot = this;
        mActiveIndex = 0;
        mRecycleViews = new View[2];
        mItems = songs;
        mSlider = getOrCreateView(mActiveIndex, mSlider, 0);
        setClipToPadding(false);
    }

    /**
     * Method for creating and updating the View that should be used in this view.
     *
     * @param position    The position of the View. This can be used to get the item that should be placed in this View.
     * @param convertView The View to create or update.
     * @return The created or updated View.
     */
    protected abstract View getView(int position, View convertView);

    protected abstract void notifyMainViewUpdated(T newItem, View mainView, boolean isMovingRight);

    /**
     * Gets or creates the View at the current position. If the View has not been created yet, it will be added to the ViewGroup and the list of
     * Views to be recycled.
     *
     * @param position     The position of the View.
     * @param convertView  The View to create or update.
     * @param recycleIndex The index of the View in the array of Views to be recycled.
     * @return The created or updated View.
     */
    private View getOrCreateView(int position, View convertView, int recycleIndex) {
        View view = getView(position, convertView);
        boolean exists = false;
        for (int i = 0; i < mRecycleViews.length; i++) {
            if (mRecycleViews[i] == view) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            addView(view);
            mRecycleViews[recycleIndex] = view;
        }
        return view;
    }

    /**
     * Sets the position and content of the view that is being slid out and might replace the main view.
     *
     * @param oldViewX The previous x-position of the view.
     */
    private void setupReplacement(int oldViewX) {
        boolean placeOnRight = mSlider.getX() > 0;
        if (placeOnRight) {
            int index = mActiveIndex - 1 < 0 ? mItems.size() - 1 : Math.abs(mActiveIndex - 1);
            mSliderReplacement = getOrCreateView(index, mRecycleViews[1], 1);
            // Setup the content for the right
            mSliderReplacement.setX(oldViewX - mSliderReplacement.getWidth());
        } else {
            int index = mActiveIndex + 1 > mItems.size() - 1 ? 0 : mActiveIndex + 1;
            mSliderReplacement = getOrCreateView(index, mRecycleViews[1], 1);
            // Setup the content for the left
            mSliderReplacement.setX(oldViewX + mSliderReplacement.getWidth());
        }
        mSlider.bringToFront(); // This should have this as always being on the top, right?
    }

    /**
     * Performs the action for this view on the MotionEvent.DOWN. This is necessary for setting the anchor points.
     *
     * @param eventX The x-position of the event.
     */
    public void doDownAction(int eventX) {
        mMoveAnchor = eventX;
        mAnchorX = eventX;
    }

    /**
     * Performs the action for this view on the MotionEvent.MOVE. This will set the x-position of both the main view and the replacement, as well
     * as update the replacement view content.
     *
     * @param eventX The x-position of the event.
     */
    public void doMoveAction(int eventX) {
        if (mItems.size() <= 1) return;

        int sliderX = (int) mSlider.getX();
        int newX;
        int moveDiff = Math.abs(mMoveAnchor - eventX);
        if (eventX > mMoveAnchor) {
            // Moving right
            newX = sliderX + moveDiff;
            mMovingRight = true;
        } else {
            // Moving left
            newX = sliderX - moveDiff;
            mMovingRight = false;
        }
        // Move the slider
        mSlider.setX(newX);
        // Setup the replacement view
        setupReplacement(newX);
        mMoveAnchor = eventX;
    }

    /**
     * Performs the action for this view on the MotionEvent.UP. This will slide the views into their correct position, either off the screen for
     * the main and into the main position for the replacement, or back to the default position for the main and off the screen for the replacement.
     *
     * @param eventX The x-position of the event.
     */
    public void doUpAction(int eventX) {
        if (mItems.size() <= 1) return;

        int sliderX = (int) mSlider.getX();
        int sliderWidth = mSlider.getWidth();
        int rightBounds = (int) (sliderX + sliderWidth * 0.66);
        int leftBounds = sliderX + (sliderWidth / 3);
        int rootWidth = mRoot.getWidth();
        ViewConfiguration vc = ViewConfiguration.get(mContext);
        int touchSlop = vc.getScaledTouchSlop();
        int moveDiff = Math.abs(mAnchorX - eventX); // Check the slop to avoid single clicks
        if (moveDiff > touchSlop) {
            if (mMovingRight) { // TODO This should probably be done better, but it works for the most part now, so I'm leaving it
                if (rightBounds > rootWidth) {
                    // Slide off screen
                    slideViews();
                } else {
                    // Move back to default
                    moveToDefault();
                }
            } else {
                // Slide off screen
                if (leftBounds < 0) {
                    slideViews();
                } else {
                    // Move back to default
                    moveToDefault();
                }
            }
        }
    }

    /**
     * Performs the action for this view on a fling gesture.
     *
     * @param isRightFling True if this is a fling to the right, false if it is a fling to the left.
     * @return
     */
    public boolean doFlingAction(boolean isRightFling) {
        if (isRightFling) {
            slideViews(false, 100);
        } else {
            slideViews(true, 100);
        }
        return true;
    }

    /**
     * Gets the item at the specified position.
     *
     * @param position The index of the item.
     * @return The item.
     */
    public T getItem(int position) {
        if (mItems.isEmpty()) return null;
        return mItems.get(position);
    }

    /**
     * Sets the list of items to use in this view. This will also create or update the main view with the item at the currently active index.
     *
     * @param songs The list of items.
     */
    public void setItems(List<T> songs, int currentIndex) {
        mItems = songs;
        mActiveIndex = currentIndex;
        mSlider = getOrCreateView(mActiveIndex, mSlider, 0);
    }

    public void moveToDefault() {
        moveToDefault(mMovingRight);
    }

    /**
     * Moves all of the views to their default positions.
     */
    public void moveToDefault(boolean movingRight) {
        if (mSlider != null && mSliderReplacement != null && mRoot != null) {
            mSlider.animate().translationX(0);
            if (movingRight) {
                // Slide replacement back to the left
                mSliderReplacement.animate().translationX(mSliderReplacement.getWidth() * -1).setListener(null);
            } else {
                // Slide replacement back to the right
                mSliderReplacement.animate().translationX(mRoot.getWidth()).setListener(null);
            }
        }
    }

    public void slideViews() {
        slideViews(mMovingRight, MOVE_DURATION);
    }

    /**
     * Performs the animations for moving the views when a complete slide has occurred.
     */
    public void slideViews(boolean movingRight, int duration) {
        if (mSlider != null && mSliderReplacement != null && mRoot != null) {
            // Move the replacement
            mSliderReplacement.animate().translationX(0).setDuration(duration)
                    .setListener(getAnimatorListener(mSlider, 0, true));
            if (movingRight) {
                // Slide the slider off to the right
                mSlider.animate().translationX(mRoot.getWidth()).setDuration(duration)
                        .setListener(getAnimatorListener(mSliderReplacement, mRoot.getWidth(), false));
            } else {
                // Slide the slider off to the left
                mSlider.animate().translationX(mSlider.getWidth() * -1).setDuration(duration)
                        .setListener(getAnimatorListener(mSliderReplacement, mSliderReplacement.getWidth() * -1, false));
            }
        }
    }

    /**
     * Creates an AnimatorListener for a slider. This will take the slider that should replace the animating slider, with the x-position that this
     * replacement slider should be set to. This also contains a flag that determines if the slider is being placed in the main position, and
     * should therefore update the main view content.
     *
     * @param slider      The slider to replace the animating slider.
     * @param endPosition The x-position of the slider.
     * @param isMain      True if this should update the main view, false otherwise.
     * @return
     */
    private Animator.AnimatorListener getAnimatorListener(final View slider, final int endPosition, final boolean isMain) {
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                slider.setX(endPosition);
                if (isMain) {
                    updateMainView();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        };
    }

    /**
     * Updates the content of the main view.
     */
    protected void updateMainView() {
        if (mMovingRight) {
            mActiveIndex = mActiveIndex - 1 < 0 ? mItems.size() - 1 : Math.abs(mActiveIndex - 1);
        } else {
            mActiveIndex = mActiveIndex + 1 > mItems.size() - 1 ? 0 : mActiveIndex + 1;
        }
        mSlider = getOrCreateView(mActiveIndex, mSlider, 0);
        notifyMainViewUpdated(getItem(mActiveIndex), mSlider, mMovingRight);
    }

}
