package com.mcnedward.bramble.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.service.MediaService;
import com.mcnedward.bramble.view.AlbumParallaxView;
import com.mcnedward.bramble.view.nowPlaying.NowPlayingView;

/**
 * Created by edward on 23/12/15.
 */
public class AlbumActivity extends FragmentActivity {

    private NowPlayingView mNowPlayingView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Album album = (Album) getIntent().getSerializableExtra("album");

        AlbumParallaxView albumParallaxView = new AlbumParallaxView(album, this);
        mNowPlayingView = albumParallaxView.getNowPlayingView();
        setContentView(albumParallaxView);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        MediaService.removeMediaListener(mNowPlayingView);
        super.onDestroy();
    }

}
