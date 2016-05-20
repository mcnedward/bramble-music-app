package com.mcnedward.bramble.view.nowPlaying;

import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Song;

/**
 * Created by Edward on 5/20/2016.
 */
public interface IScrollView {

    void update(Song song, Album album);

    void slideUp(boolean top);

    void setSize(int width, int height);

}
