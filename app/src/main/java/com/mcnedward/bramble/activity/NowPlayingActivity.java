package com.mcnedward.bramble.activity;

import android.app.Activity;
import android.os.Bundle;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Song;
import com.mcnedward.bramble.utils.listener.AlbumLoadListener;
import com.mcnedward.bramble.view.nowPlaying.NowPlayingView;

/**
 * Created by edward on 26/12/15.
 */
public class NowPlayingActivity extends Activity implements AlbumLoadListener {
    private final static String TAG = "NowPlayingActivity";

    private Song song;
    private Album album;

    private NowPlayingView nowPlayingView;

    private boolean paused = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        song = (Song) getIntent().getSerializableExtra("song");

        nowPlayingView = (NowPlayingView) findViewById(R.id.now_playing);
        setContentView(nowPlayingView);

        MainActivity.mediaCache.registerAlbumLoadListener(this);
    }

    @Override
    public void notifyAlbumLoadReady() {
        if (!nowPlayingView.isLoaded())
            nowPlayingView.loadNowPlaying();
    }

    @Override
    public void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        paused = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public boolean isPaused() {
        return paused;
    }
}
