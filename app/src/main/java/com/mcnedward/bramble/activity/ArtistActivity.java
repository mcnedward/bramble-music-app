package com.mcnedward.bramble.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.mcnedward.bramble.entity.media.Artist;
import com.mcnedward.bramble.enums.IntentKey;
import com.mcnedward.bramble.service.MediaService;
import com.mcnedward.bramble.view.nowPlaying.NowPlayingView;
import com.mcnedward.bramble.view.parallax.ArtistParallaxView;

/**
 * Created by edward on 23/12/15.
 */
public class ArtistActivity extends FragmentActivity {

    private NowPlayingView mNowPlayingView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Artist artist = (Artist) getIntent().getSerializableExtra(IntentKey.ARTIST.name());

        final ArtistParallaxView artistParallaxView = new ArtistParallaxView(this, artist);
        mNowPlayingView = artistParallaxView.getNowPlayingView();
        setContentView(artistParallaxView);
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
    public void onBackPressed() {
        if (mNowPlayingView.isContentFocused()) {
            mNowPlayingView.animateToBottom();
        } else {
            super.onBackPressed();
        }
    }

}
