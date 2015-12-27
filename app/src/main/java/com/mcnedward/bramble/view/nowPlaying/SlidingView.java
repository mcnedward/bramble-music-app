package com.mcnedward.bramble.view.nowPlaying;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.mcnedward.bramble.R;

/**
 * Created by edward on 27/12/15.
 */
public abstract class SlidingView extends RelativeLayout implements View.OnTouchListener {
    private final static String TAG = "SlidingView";

    private ViewGroup root;
    private View slidable;
    private View content;

    private boolean controlsTouched;
    private int anchorY = 0;

    public SlidingView(int resourceId, Context context) {
        super(context);
        inflate(context, resourceId, this);
        root = (ViewGroup) findViewById(R.id.now_playing_root);
        root.setOnTouchListener(this);
    }

    protected abstract void switchSlidable(boolean top);

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        int slidableY = (int) content.getY();
        int slidableHeight = slidable.getHeight();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                anchorY = (int) event.getY();
                if (anchorY > slidableY && anchorY < (slidableY + slidableHeight))
                    controlsTouched = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (controlsTouched) {
                    int newY;
                    int eventY = (int) event.getY();
                    if (eventY > (slidableY + slidableHeight)) {
                        // Moving down
                        newY = eventY - slidableHeight;
                    } else {
                        // Moving up
                        int diff = Math.abs(eventY - slidableY);
                        newY = eventY - diff;
                    }
                    content.setY(newY);
                    if (eventY >= root.getHeight()) {
                        // Prevent moving below bottom of screen
                        content.setY(root.getHeight() - slidableHeight);
                    }
                    if (eventY < 0) {
                        // Prevent moving above top of screen
                        // -5 to take shadow into account
                        content.setY(-5);
                        switchSlidable(true);
                    }
                    if (eventY > root.getHeight() / 4) {
                        // Content is in bottom half
                        switchSlidable(false);
                        float opacity = ((float) eventY) / ((float) root.getHeight());
                        slidable.setAlpha(opacity);
                    } else {
                        switchSlidable(true);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (controlsTouched) {
                    if (event.getY() < root.getHeight() / 2) {
                        // Stick to top of screen
                        // -5 to take shadow into account
                        switchSlidable(true);
                        content.animate().translationY(0);
                    } else {
                        // Stick to bottom of screen
                        switchSlidable(false);
                        int contentY = (int) (content.getY() + slidableHeight);
                        int animationDistance = Math.abs(contentY - root.getHeight());
                        content.animate().translationYBy(animationDistance);
                        slidable.animate().alpha(1.0f);
                    }
                }
                controlsTouched = false;
                break;
        }
        return true;
    }

    private void animateView(final View view, final int fromYDelta, final int toYDelta) {
        TranslateAnimation anim = new TranslateAnimation(0, 0, fromYDelta, toYDelta);
        anim.setDuration(1000);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d(TAG, "STARTING ANIMATION!");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, "ENDING ANIMATION!");
                view.setY(toYDelta);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        anim.setFillAfter(false);
        view.startAnimation(anim);
    }

    public void setSlidable(View slidable) {
        this.slidable = slidable;
    }

    public void setContent(View content) {
        this.content = content;
    }
}
