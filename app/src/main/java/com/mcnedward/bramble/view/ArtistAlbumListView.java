package com.mcnedward.bramble.view;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Artist;

import java.util.List;

/**
 * Created by edward on 22/12/15.
 */
public class ArtistAlbumListView extends LinearLayout {
    private static String TAG = "ArtistAlbumListView";

    private Context context;
    private TextView txtArtist;
    private GridLayout gridLayout;

    public ArtistAlbumListView(Artist artist, Context context) {
        super(context);
        this.context = context;

        setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        inflate(context, R.layout.artist_albums_list_view, this);

        txtArtist = (TextView) findViewById(R.id.txtArtist);

//        List<Album> albums = artist.getAlbums();
//        int rowCount = albums.size() / 2;
//        gridLayout = new GridLayout(context);
//        gridLayout.setColumnCount(2);
//        gridLayout.setRowCount(rowCount);
//        addView(gridLayout);
    }

    public void addAlbum(Album album) {
        TextView textView = new TextView(context);
        textView.setText(album.getAlbumName());
        gridLayout.addView(textView);
    }

}
