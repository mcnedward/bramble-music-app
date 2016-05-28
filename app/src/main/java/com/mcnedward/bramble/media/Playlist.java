package com.mcnedward.bramble.media;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 5/26/2016.
 */
public class Playlist extends Data {

    List<String> mSongKeys;

    public Playlist() {
        super();
    }

    public Playlist(int id) {
        super(id);
        mSongKeys = new ArrayList<>();
    }

    public Playlist(int id, List<String> songKeys) {
        super(id);
        mSongKeys = songKeys;
    }

    public Playlist(List<String> songKeys) {
        mSongKeys = songKeys;
    }

    public List<String> getSongKeys() {
        return mSongKeys;
    }

    public void setSongIds(List<String> songKeys) {
        mSongKeys = songKeys;
    }
}
