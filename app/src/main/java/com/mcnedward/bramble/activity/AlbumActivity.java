package com.mcnedward.bramble.activity;

import android.app.Activity;
import android.os.Bundle;

import com.mcnedward.bramble.view.AlbumParallaxView;

/**
 * Created by edward on 23/12/15.
 */
public class AlbumActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new AlbumParallaxView(this));
    }

}
