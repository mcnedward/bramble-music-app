package com.mcnedward.bramble.view.mediaItem;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.mcnedward.bramble.R;

/**
 * Created by Edward on 5/17/2016.
 */
public class GifView extends ImageView {

    private AnimationDrawable mAnimation;

    public GifView(Context context) {
        super(context);
        initialize(context);
    }

    public GifView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public GifView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    public GifView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context);
    }

    private void initialize(Context context) {
        setBackgroundResource(R.drawable.now_playing_anim);
        mAnimation = (AnimationDrawable) getBackground();
        post(new Runnable() {
            @Override
            public void run() {
                mAnimation.start();
            }
        });

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnimation.start();
            }
        });
    }

    public void play(final boolean play) {
        post(new Runnable() {
            @Override
            public void run() {
                if (play) {
                    setVisibility(VISIBLE);
                    mAnimation.start();
                } else {
                    setVisibility(GONE);
                    mAnimation.stop();
                }
            }
        });
    }

}
