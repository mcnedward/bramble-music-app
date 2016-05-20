package com.mcnedward.bramble.utils;

import android.content.Context;

import com.mcnedward.bramble.repository.AlbumRepository;
import com.mcnedward.bramble.repository.SongRepository;

/**
 * Created by Edward on 5/20/2016.
 */
public class RepositoryUtil {

    private static AlbumRepository mAlbumRepository;
    private static SongRepository mSongRepository;

    public static AlbumRepository getAlbumRepository(Context context) {
        if (mAlbumRepository == null) {
            mAlbumRepository = new AlbumRepository(context);
        }
        return mAlbumRepository;
    }

    public static SongRepository getSongRepository(Context context) {
        if (mSongRepository == null) {
            mSongRepository = new SongRepository(context);
        }
        return mSongRepository;
    }

}
