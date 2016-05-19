package com.mcnedward.bramble.listener;

import android.view.View;

import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Song;

/**
 * Created by Edward on 5/19/2016.
 */
public interface MediaPlayingListener {

    void update(Song song, Album album);

    View getView();
}
