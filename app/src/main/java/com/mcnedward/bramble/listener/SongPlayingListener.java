package com.mcnedward.bramble.listener;

import com.mcnedward.bramble.media.Song;

/**
 * Created by Edward on 5/17/2016.
 */
public interface SongPlayingListener {

    void notifySongChange(Song currentSong, boolean isPlaying);

}
