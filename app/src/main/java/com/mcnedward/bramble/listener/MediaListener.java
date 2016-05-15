package com.mcnedward.bramble.listener;

import android.view.View;

/**
 * Created by edward on 27/12/15.
 */
public interface MediaListener {

    void notifyMediaStarted();

    void update();

    View getView();

}
