package com.mcnedward.bramble.repository.data;

import java.util.List;

/**
 * Created by Edward on 5/28/2016.
 */
public interface IPlaylistRepository {

    void saveCurrentPlaylist(List<Long> songKeys);

    List<Long> getCurrentPlaylist();

}
