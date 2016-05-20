package com.mcnedward.bramble.view.nowPlaying;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcnedward.bramble.R;

/**
 * Created by edward on 27/12/15.
 */
public abstract class HorizontalSlidingView extends RelativeLayout implements View.OnTouchListener {
    private final static String TAG = "SlidingView";

    private Context context;

    private ViewGroup mRoot;
    private View mSlider;
    private View mSliderReplacement;

    private int mAnchorX;
    private boolean mMovingRight;
    private boolean mUpdateLock;

    private boolean controlsTouched;
    protected boolean contentFocused = false;

    public HorizontalSlidingView(int resourceId, Context context) {
        super(context);
        initialize(resourceId, context);
    }

    public HorizontalSlidingView(int resourceId, Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(resourceId, context);
    }

    private void initialize(int resourceId, Context context) {
        this.context = context;
        mRoot = this;
        mSliderReplacement = new RelativeLayout(context);

        setOnTouchListener(this);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1 = inflater.inflate(resourceId, (RelativeLayout) mSlider);
        View view2 = inflater.inflate(resourceId, (RelativeLayout) mSliderReplacement);
        addView(mSliderReplacement);
        ((TextView) view1.findViewById(R.id.now_playing_title)).setText("Main");
        ((TextView) view2.findViewById(R.id.now_playing_title)).setText("Replacement");
    }

    private void setupReplacement(int oldViewX) {
        boolean placeOnRight = mSlider.getX() > 0;
        if (placeOnRight) {
            // Setup the content for the right
            mSliderReplacement.setX(oldViewX - mSliderReplacement.getWidth());
            if (!mUpdateLock) {
                updateRightContent(mSliderReplacement);
            }
        } else {
            // Setup the content for the left
            mSliderReplacement.setX(oldViewX + mSliderReplacement.getWidth());
            if (!mUpdateLock) {
                updateLeftContent(mSliderReplacement);
            }
        }
        mUpdateLock = true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
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
            case MotionEvent.ACTION_DOWN:
                mAnchorX = eventX;
                if (eventY > sliderY && eventY < (sliderY + sliderHeight)) {
                    controlsTouched = true;
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (controlsTouched) {
                    int newX;
                    int moveDiff = Math.abs(mAnchorX - eventX);
                    if (eventX > mAnchorX) {
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
                    mAnchorX = eventX;
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (controlsTouched) {
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
                controlsTouched = false;
                break;
        }
        // Allow the underlying view to handle touch events when the sliding view is not focused
        return contentFocused;
    }

    public void moveToDefault() {
        if (mSlider != null && mSliderReplacement != null && mRoot != null) {
            mSlider.animate().translationX(0);
            if (mMovingRight) {
                // Slide replacement back to the left
                mSliderReplacement.animate().translationX(mSliderReplacement.getWidth() * -1);
            } else {
                // Slide replacement back to the right
                mSliderReplacement.animate().translationX(mRoot.getWidth());
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

    protected abstract void updateMainView();

    protected abstract void updateReplacementView();

    protected abstract void updateLeftContent(View view);

    protected abstract void updateRightContent(View view);
}
