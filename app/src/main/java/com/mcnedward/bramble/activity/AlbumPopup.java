package com.mcnedward.bramble.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.adapter.grid.AlbumGridAdapter;
import com.mcnedward.bramble.entity.media.Album;
import com.mcnedward.bramble.entity.media.Artist;
import com.mcnedward.bramble.utils.RepositoryUtil;

import java.util.List;

/**
 * Created by edward on 22/12/15.
 */
public class AlbumPopup extends FrameLayout {
    private final static String TAG = "AlbumPopup";

    public AlbumPopup(Context context, Artist artist) {
        super(context);
        inflate(context, R.layout.screen_popup, this);

        TextView txtArtistName = (TextView) findViewById(R.id.artistName);
        txtArtistName.setText(artist.getArtistName());
        txtArtistName.setFocusable(true);

        List<Album> albums = RepositoryUtil.getAlbumRepository(context).getAlbumsForArtist(artist.getId());
        AlbumGridAdapter adapter = new AlbumGridAdapter(context, albums);

        GridView gridView = (GridView) findViewById(R.id.albumView);
        gridView.setAdapter(adapter);
        gridView.setGravity(Gravity.CENTER);
    }

    public static void startAlbumPopup(Context context, Artist artist) {
        AlbumPopup albumPopup = new AlbumPopup(context, artist);

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);

        int windowWidth = dm.widthPixels;
        int windowHeight = dm.heightPixels;
        int width = (int) (windowWidth * 0.75);
        int height = (int) (windowHeight * 0.65);
        int offsetX = (windowWidth - width) / 2;
        int offsetY = (windowHeight - height) / 3;

        // Creating the PopupWindow
        PopupWindow popupWindow = new PopupWindow(context);
        popupWindow.setContentView(albumPopup);
        popupWindow.setWidth(width);
        popupWindow.setHeight(height);
        popupWindow.setFocusable(true);
        popupWindow.setElevation(10);

        // Clear the default translucent background
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        // Displaying the popup at the specified location, + offsets.
        popupWindow.showAtLocation(albumPopup, Gravity.NO_GRAVITY, offsetX, offsetY);
    }

}
