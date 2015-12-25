package com.mcnedward.bramble.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import com.mcnedward.bramble.utils.listener.ScrollViewListener;

/**
 * Created by edward on 23/12/15.
 */
public class ParallaxScrollView extends ScrollView {
    private final static String TAG = "ParallaxScrollView";

    private ScrollViewListener scrollViewListener = null;

    public ParallaxScrollView(Context context) {
        super(context);
    }

    public ParallaxScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if(scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }
}
