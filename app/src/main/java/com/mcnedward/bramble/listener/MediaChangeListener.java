package com.mcnedward.bramble.listener;

import com.mcnedward.bramble.entity.media.Song;

/**
 * Created by edward on 27/12/15.
 *
 * A listener for handling media changes. This will notify when the media status changes.
 */
public interface MediaChangeListener {

    /**
     * For use when the media is paused or played.
     * @param currentSong The currently playing song.
     * @param playing True if the song is playing, false if it is paused or stopped.
     */
    void onMediaPlayStateChange(Song currentSong, boolean playing);

    /**
     * For use when the media changes, for example when a new song is selected.
     * @param currentSong The currently playing song.
     * @param playing True if the song is playing, false if it is paused or stopped.
     */
    void onMediaChange(Song currentSong, boolean playing);

    /**
     * For use when the media is stopped.
     * @param song The song that is stopped.
     */
    void onMediaStop(Song song);

}
