package com.mcnedward.bramble.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.view.AlbumParallaxView;

/**
 * Created by edward on 23/12/15.
 */
public class AlbumActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Album album = (Album) getIntent().getSerializableExtra("album");

        setContentView(new AlbumParallaxView(album, this));
    }

}
