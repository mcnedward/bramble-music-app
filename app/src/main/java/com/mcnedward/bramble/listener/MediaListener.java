package com.mcnedward.bramble.listener;

import android.view.View;

import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Song;

/**
 * Created by edward on 27/12/15.
 */
public interface MediaListener {

    void notifyMediaStarted();

    void update(Song song, Album album);

    View getView();

}
