package com.mcnedward.bramble.view.nowPlaying;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.mcnedward.bramble.media.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward on 27/12/15.
 */
public abstract class HorizontalSlidingView extends RelativeLayout {//implements View.OnTouchListener {
    private final static String TAG = "SlidingView";

    private static final int LEFT_DIRECTION = -1;
    private static final int RIGHT_DIRECTION = 1;

    protected Context mContext;
    private List<Song> mItems;
    private int mActiveIndex;
    private View[] mRecycleViews;

    private ViewGroup mRoot;
    private View mSlider;
    private View mSliderReplacement;
    private LayoutInflater mInflater;
    private int mMoveAnchor;
    private int mAnchorX;
    private boolean mMovingRight;
    private boolean mUpdateLock;

    private boolean controlsTouched;

    public HorizontalSlidingView(Context context, List<Song> songs) {
        super(context);
        initialize(context, songs);
    }

    public HorizontalSlidingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, new ArrayList<Song>());
    }

    private void initialize(Context context, List<Song> songs) {
        this.mContext = context;
        mRoot = this;
        mActiveIndex = 0;
        mRecycleViews = new View[2];
        mItems = songs;

//        setOnTouchListener(this);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSlider = getOrCreateView(mActiveIndex, mSlider, 0);
    }

    protected abstract View getView(int position, View convertView);

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

    private void setupReplacement(int oldViewX) {
        boolean placeOnRight = mSlider.getX() > 0;
        if (placeOnRight) {
            int index = mActiveIndex - 1 < 0 ? mItems.size() - 1 : Math.abs(mActiveIndex - 1);
            mSliderReplacement = getOrCreateView(index, mRecycleViews[1], 1);
            // Setup the content for the right
            mSliderReplacement.setX(oldViewX - mSliderReplacement.getWidth());
            if (!mUpdateLock) {
                updateRightContent(mSliderReplacement);
            }
        } else {
            int index = mActiveIndex + 1 > mItems.size() - 1 ? 0 : mActiveIndex + 1;
            mSliderReplacement = getOrCreateView(index, mRecycleViews[1], 1);
            // Setup the content for the left
            mSliderReplacement.setX(oldViewX + mSliderReplacement.getWidth());
            if (!mUpdateLock) {
                updateLeftContent(mSliderReplacement);
            }
        }
        mUpdateLock = true;
    }

    public void doMoveAction(int eventX) {
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

    public void doUpAction(int eventX) {
        int sliderX = (int) mSlider.getX();
        int sliderWidth = mSlider.getWidth();
        int rightBounds = (int) (sliderX + sliderWidth * 0.66);
        int leftBounds = sliderX + (sliderWidth / 3);
        int rootWidth = mRoot.getWidth();
        ViewConfiguration vc = ViewConfiguration.get(mContext);
        int touchSlop = vc.getScaledTouchSlop();
        int moveDiff = Math.abs(mAnchorX - eventX); // Check the slop to avoid single clicks
        if (moveDiff > touchSlop) {
            if (mMovingRight) {
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
        mUpdateLock = false;    // Unlock the updates so they can be updated again on the first movement
    }

//    @Override
//    public boolean  onTouch(View v, MotionEvent event) {
    public boolean doTouchAction(View v, MotionEvent event) {
        int action = event.getAction();
        int eventX = (int) event.getX();
        int eventY = (int) event.getY();
        int sliderX = (int) mSlider.getX();
        int sliderY = (int) mSlider.getY();
        int sliderWidth = mSlider.getWidth();
        int sliderHeight = mSlider.getHeight();
        int rightBounds = (int) (sliderX + sliderWidth * 0.66);
        int leftBounds = sliderX + (sliderWidth / 3);
        int rootWidth = mRoot.getWidth();

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mMoveAnchor = eventX;
                mAnchorX = eventX;
//                if (eventY > sliderY && eventY < (sliderY + sliderHeight)) {
//                    controlsTouched = true;
//                    return true;
//                }
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
//                if (controlsTouched) {
//                }
                break;
            }
            case MotionEvent.ACTION_UP: {
//                if (controlsTouched) {
                ViewConfiguration vc = ViewConfiguration.get(mContext);
                int touchSlop = vc.getScaledTouchSlop();
                int moveDiff = Math.abs(mAnchorX - eventX); // Check the slop to avoid single clicks
                if (moveDiff > touchSlop) {
                    if (mMovingRight) {
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
                    return true;
                }
//                }
                mUpdateLock = false;    // Unlock the updates so they can be updated again on the first movement
                controlsTouched = false;
                break;
            }
        }
        // Allow the underlying view to handle touch events when the sliding view is not focused
        return false;
    }

    public Song getItem(int position) {
        if (mItems.isEmpty()) return null;
        return mItems.get(position);
    }

    public void setItems(List<Song> songs) {
        mItems = songs;
        mActiveIndex = 0;
        mSlider = getOrCreateView(mActiveIndex, mSlider, 0);
    }

    public void moveToDefault() {
        if (mSlider != null && mSliderReplacement != null && mRoot != null) {
            mSlider.animate().translationX(0);
            if (mMovingRight) {
                // Slide replacement back to the left
                mSliderReplacement.animate().translationX(mSliderReplacement.getWidth() * -1).setListener(null);
            } else {
                // Slide replacement back to the right
                mSliderReplacement.animate().translationX(mRoot.getWidth()).setListener(null);
            }
        }
    }

    public void slideViews() {
        if (mSlider != null && mSliderReplacement != null && mRoot != null) {
            mSliderReplacement.animate().translationX(0).setDuration(300)
                    .setListener(getAnimatorListener(mSlider, 0, true));
            if (mMovingRight) {
                // Slide the slider off to the right
                mSlider.animate().translationX(mRoot.getWidth()).setDuration(300)
                        .setListener(getAnimatorListener(mSliderReplacement, mRoot.getWidth(), false));
            } else {
                // Slide the slider off to the left
                mSlider.animate().translationX(mSlider.getWidth() * -1).setDuration(300)
                        .setListener(getAnimatorListener(mSliderReplacement, mSliderReplacement.getWidth() * -1, false));
            }
        }
    }

    private Animator.AnimatorListener getAnimatorListener(final View slider, final int endPosition, final boolean isMain) {
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                slider.setX(endPosition);
                if (isMain) {
                    updateMainView(mMovingRight ? -1 : 1);
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

    protected void updateMainView(int direction) {
        switch (direction) {
            case LEFT_DIRECTION:
                mActiveIndex = mActiveIndex - 1 < 0 ? mItems.size() - 1 : Math.abs(mActiveIndex - 1);
                break;
            case RIGHT_DIRECTION:
                mActiveIndex = mActiveIndex + 1 > mItems.size() - 1 ? 0 : mActiveIndex + 1;
                break;
        }
        mSlider = getOrCreateView(mActiveIndex, mSlider, 0);
    }

    protected abstract void updateReplacementView();

    protected abstract void updateLeftContent(View view);

    protected abstract void updateRightContent(View view);
}
