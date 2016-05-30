package com.mcnedward.bramble.entity.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 5/26/2016.
 */
public class Playlist extends Data {

    List<Long> mSongIds;

    public Playlist() {
        super();
    }

    public Playlist(int id) {
        super(id);
        mSongIds = new ArrayList<>();
    }

    public Playlist(int id, List<Long> songKeys) {
        super(id);
        mSongIds = songKeys;
    }

    public Playlist(List<Long> songIds) {
        mSongIds = songIds;
    }

    public List<Long> getSongKeys() {
        return mSongIds;
    }

    public void setSongIds(List<Long> songIds) {
        mSongIds = songIds;
    }
}
