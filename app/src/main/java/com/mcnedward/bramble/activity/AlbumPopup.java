package com.mcnedward.bramble.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.adapter.AlbumPopUpGridAdapter;
import com.mcnedward.bramble.listener.AlbumLoadListener;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Artist;
import com.mcnedward.bramble.utils.MediaCache;

import java.util.List;

/**
 * Created by edward on 22/12/15.
 */
public class AlbumPopup extends Activity implements AlbumLoadListener {
    private final static String TAG = "AlbumPopup";

    private Artist artist;

    private ProgressBar progressBar;
    private TextView txtProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_popup);

        initializeWindow();

        artist = (Artist) getIntent().getSerializableExtra("artist");
        TextView txtArtistName = (TextView) findViewById(R.id.artistName);
        txtArtistName.setText(artist.getArtistName());
        txtArtistName.setFocusable(true);

        ((TextView) findViewById(R.id.album_popup_progress_text)).setText(getString(R.string.album_popup_loading_text));

        MediaCache.registerAlbumLoadListener(this);
    }

    private void initializeWindow() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = (int) (dm.widthPixels * 0.75);
        int height = (int) (dm.heightPixels * 0.6);

        getWindow().setLayout(width, height);
    }

    @Override
    public void notifyAlbumLoadReady() {
        Log.d(TAG, "Albums ready...");
        GridView gridView = (GridView) findViewById(R.id.albumView);
        List<Album> albums = MainActivity.mediaCache.getAlbumsForArtist(artist);
        AlbumPopUpGridAdapter adapter = new AlbumPopUpGridAdapter(albums, this);
        gridView.setAdapter(adapter);
        gridView.setGravity(Gravity.CENTER);

        findViewById(R.id.album_popup_progress_bar).setVisibility(View.GONE);
        findViewById(R.id.album_popup_progress_text).setVisibility(View.GONE);
    }

}
