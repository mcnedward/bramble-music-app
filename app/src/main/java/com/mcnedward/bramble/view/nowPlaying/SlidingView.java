package com.mcnedward.bramble.view.nowPlaying;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
                        switchSlidable(false);
                    }
                    if (eventY < 0) {
                        // Prevent moving above top of screen
                        // -5 to take shadow into account
                        content.setY(-5);
                        switchSlidable(true);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (controlsTouched) {
                    // TODO Need animations here!
                    if (event.getY() < root.getHeight() / 2) {
                        // Stick to top of screen
                        // -5 to take shadow into account
                        content.setY(-5);
                        switchSlidable(true);
                    } else {
                        // Stick to bottom of screen
                        content.setY(root.getHeight() - slidableHeight);
                        switchSlidable(false);
                    }
                }
                controlsTouched = false;
                break;
        }
        return true;
    }

    public void setSlidable(View slidable) {
        this.slidable = slidable;
    }

    public void setContent(View content) {
        this.content = content;
    }
}
