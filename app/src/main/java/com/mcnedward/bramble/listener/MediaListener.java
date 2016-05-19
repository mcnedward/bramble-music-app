package com.mcnedward.bramble.listener;

import android.view.View;

import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Song;

/**
 * Created by edward on 27/12/15.
 * <p/>
 * A listener for handling media changes. This will notify when the media status changes, and notify for updates, and return the view that needs to
 * be updated.
 */
public interface MediaListener {

    void notifyUpdateMediaStatus(boolean playing);

    void update(Song song, Album album);

    View getView();

}
