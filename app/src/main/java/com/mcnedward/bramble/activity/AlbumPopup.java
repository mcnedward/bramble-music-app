package com.mcnedward.bramble.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.adapter.grid.AlbumGridAdapter;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Artist;
import com.mcnedward.bramble.repository.AlbumRepository;

import java.util.List;

/**
 * Created by edward on 22/12/15.
 */
public class AlbumPopup extends Activity {
    private final static String TAG = "AlbumPopup";

    private AlbumRepository mAlbumRepository;
    private Artist mArtist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_popup);

        initializeWindow();

        mAlbumRepository = new AlbumRepository(this);
        mArtist = (Artist) getIntent().getSerializableExtra("artist");
        TextView txtArtistName = (TextView) findViewById(R.id.artistName);
        txtArtistName.setText(mArtist.getArtistName());
        txtArtistName.setFocusable(true);

        loadAlbums();
    }

    private void loadAlbums() {
        List<Album> albums = mAlbumRepository.getAlbumsForArtist(mArtist.getId());
        AlbumGridAdapter adapter = new AlbumGridAdapter(albums, this);

        GridView gridView = (GridView) findViewById(R.id.albumView);
        gridView.setAdapter(adapter);
        gridView.setGravity(Gravity.CENTER);
    }

    private void initializeWindow() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = (int) (dm.widthPixels * 0.75);
        int height = (int) (dm.heightPixels * 0.65);

        getWindow().setLayout(width, height);
    }

}
