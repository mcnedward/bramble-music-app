package com.mcnedward.bramble.utils.listener;

import com.mcnedward.bramble.view.ParallaxScrollView;

/**
 * Created by edward on 23/12/15.
 */
public interface ScrollViewListener {

    void onScrollChanged(ParallaxScrollView scrollView, int x, int y, int oldX, int oldY);

}
