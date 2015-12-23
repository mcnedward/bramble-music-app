package com.mcnedward.bramble.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Artist;

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
        TextView textView = (TextView) findViewById(R.id.artistName);
        textView.setText(artist.getArtistName());

        GridView gridView = (GridView) findViewById(R.id.albumView);
        ArrayAdapter<Album> adapter = new ArrayAdapter<>(this, R.layout.simple_list_item, artist.getAlbums());
        gridView.setAdapter(adapter);
    }

    private void initializeWindow() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = (int) (dm.widthPixels * 0.75);
        int height = (int) (dm.heightPixels * 0.6);

        getWindow().setLayout(width, height);
    }

}
