package com.mcnedward.bramble.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.GridView;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Artist;
import com.mcnedward.bramble.utils.adapter.AlbumGridAdapter;

import java.util.List;

/**
 * Created by edward on 22/12/15.
 */
public class AlbumPopup extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_popup);

        initializeWindow();

        Artist artist = (Artist) getIntent().getSerializableExtra("artist");
        TextView txtArtistName = (TextView) findViewById(R.id.artistName);
        txtArtistName.setText(artist.getArtistName());

        if (MainActivity.mediaService.isLoadingAlbums()) {
            txtArtistName.setText("LOADING ALBUMS");
        } else {
            GridView gridView = (GridView) findViewById(R.id.albumView);
            List<Album> albums = MainActivity.mediaService.getAlbumsForArtist(artist);
            AlbumGridAdapter adapter = new AlbumGridAdapter(albums, this);
            gridView.setAdapter(adapter);
            gridView.setGravity(Gravity.CENTER);
        }
    }

    private void initializeWindow() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = (int) (dm.widthPixels * 0.75);
        int height = (int) (dm.heightPixels * 0.6);

        getWindow().setLayout(width, height);
    }

}
