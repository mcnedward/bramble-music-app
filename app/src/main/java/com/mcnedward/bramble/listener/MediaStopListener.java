package com.mcnedward.bramble.listener;

import com.mcnedward.bramble.entity.media.Song;

/**
 * Created by edward on 27/12/15.
 *
 * A listener for handling media stop changes. This will notify when playing media stops.
 */
public interface MediaStopListener {

    void notifyMediaStop(Song song);

}
