package com.mcnedward.bramble.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.mcnedward.bramble.enums.IntentKey;
import com.mcnedward.bramble.entity.media.Album;
import com.mcnedward.bramble.service.MediaService;
import com.mcnedward.bramble.view.parallax.AlbumParallaxView;
import com.mcnedward.bramble.view.nowPlaying.NowPlayingView;

/**
 * Created by edward on 23/12/15.
 */
public class AlbumActivity extends FragmentActivity {

    private NowPlayingView mNowPlayingView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Album album = (Album) getIntent().getSerializableExtra(IntentKey.ALBUM.name());

        AlbumParallaxView albumParallaxView = new AlbumParallaxView(this, album);
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
        MediaService.removeMediaChangeListener(mNowPlayingView);
        MediaService.removeMediaPlayingListener(mNowPlayingView.getBottomControl());
        super.onDestroy();
    }

    @Override
    public void onBackPressed()  {
        if (mNowPlayingView.isContentFocused()) {
            mNowPlayingView.animateToBottom();
        } else {
            super.onBackPressed();
        }
    }

}
