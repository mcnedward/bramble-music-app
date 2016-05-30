package com.mcnedward.bramble.async;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.mcnedward.bramble.entity.media.Album;
import com.mcnedward.bramble.entity.media.Song;
import com.mcnedward.bramble.enums.IntentKey;
import com.mcnedward.bramble.service.MediaService;
import com.mcnedward.bramble.utils.RepositoryUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 5/30/2016.
 */
public class StartMusicTask extends AsyncTask<Void, Void, Void> {

    private Context mContext;
    private Song mSong;
    private Album mAlbum;
    private List<String> mSongKeys;

    public StartMusicTask(Context context, Song song) {
        mContext = context;
        mSong = song;
    }

    public StartMusicTask(Context context, Song song, Album album) {
        mContext = context;
        mSong = song;
        mAlbum = album;
    }

    public StartMusicTask(Context context, Song song, List<String> songKeys) {
        mContext = context;
        mSong = song;
        mSongKeys = songKeys;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Intent intent = new Intent(mContext, MediaService.class);
        intent.putExtra(IntentKey.SONG.name(), mSong);

        // If the song keys are already set, use them.
        // Otherwise, check if this is coming from an album, and then use the album's song keys if that is true
        if (mSongKeys != null) {
            intent.putExtra(IntentKey.SONG_KEYS.name(), new ArrayList<>(mSongKeys));
        } else if (mAlbum != null) {
            // Get all the song ids in the album
            List<Song> songsForAlbum = RepositoryUtil.getSongRepository(mContext).getSongsForAlbum(mAlbum.getId());
            List<String> songKeys = new ArrayList<>();
            for (Song songForAlbum : songsForAlbum) {
                songKeys.add(songForAlbum.getKey());
            }
            intent.putExtra(IntentKey.SONG_KEYS.name(), new ArrayList<>(songKeys));
        }

        mContext.startService(intent);
        return null;
    }

}
