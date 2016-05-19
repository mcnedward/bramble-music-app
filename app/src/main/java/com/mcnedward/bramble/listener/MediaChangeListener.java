package com.mcnedward.bramble.listener;

import com.mcnedward.bramble.media.Song;

/**
 * Created by edward on 27/12/15.
 *
 * A listener for handling media changes. This will notify when the media status changes.
 */
public interface MediaChangeListener {

    void notifyMediaChange(Song currentSong, boolean playing);

}
