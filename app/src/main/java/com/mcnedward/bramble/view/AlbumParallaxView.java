package com.mcnedward.bramble.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.RippleDrawable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.activity.MainActivity;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Song;
import com.mcnedward.bramble.utils.Extension;
import com.mcnedward.bramble.utils.listener.ScrollViewListener;

import java.io.File;
import java.util.List;

/**
 * Created by edward on 23/12/15.
 */
public class AlbumParallaxView extends LinearLayout {
    final private static String TAG = "AlbumParallaxView";

    private Context context;
    private DisplayMetrics dm;

    private ParallaxScrollView mBgScrollView;
    private ParallaxScrollView mContentScrollView;

    private Album album;

    public AlbumParallaxView(Album album, Context context) {
        super(context);
        this.context = context;
        this.album = album;
        inflate(context, R.layout.album_view, this);

        dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);

        setScrollViews();

        setForegroundContent(context);
        setBackgroundContent(context);
    }

    private void setScrollViews() {
        mBgScrollView = (ParallaxScrollView) findViewById(R.id.background_sv);
        mContentScrollView = (ParallaxScrollView) findViewById(R.id.content_sv);
        mContentScrollView.setScrollViewListener(new ScrollViewListener() {
            @Override
            public void onScrollChanged(ParallaxScrollView scrollView, int x, int y, int oldX, int oldY) {
                mBgScrollView.scrollTo(0, (int) (y * 0.8f));
            }
        });
    }

    private void setForegroundContent(Context context) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.parallax_main_content);

        // Set the empty space to push list below album art
        int spaceHeight = (dm.heightPixels / 2);
        Space space = new Space(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, spaceHeight);
        space.setLayoutParams(layoutParams);
        layout.addView(space);

        // Set the album and artist name
        LinearLayout albumTitleView = (LinearLayout) inflate(context, R.layout.album_title_view, null);
        TextView txtAlbumName = (TextView) albumTitleView.findViewById(R.id.album_title_title);
        txtAlbumName.setText(album.getAlbumName());
        TextView txtArtistName = (TextView) albumTitleView.findViewById(R.id.album_title_artist);
        txtArtistName.setText(album.getArtist());
        layout.addView(albumTitleView);

        // Set the list of songs
        List<Song> songs = MainActivity.mediaService.getSongsForAlbum(album);
        for (Song song : songs) {
            AlbumSongItem songItem = new AlbumSongItem(song, context);
            layout.addView(songItem);
        }
    }

    private void setBackgroundContent(Context context) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.parallax_background_content);
        ImageView imgAlbumArt = (ImageView) findViewById(R.id.bg_album_art);

        int spaceHeight = (int) (dm.heightPixels / 1.3f);
        int imageHeight = (dm.heightPixels / 2);

        Space space = new Space(context);
        space.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, spaceHeight));
        layout.addView(space);

        imgAlbumArt.setLayoutParams(new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, imageHeight));
        String albumArt = album.getAlbumArt();
        if (albumArt != null) {
            // Create the album art bitmap and scale it to fit properly and avoid over using memory
            File imageFile = new File(album.getAlbumArt());
            Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            imgAlbumArt.setImageBitmap(imageBitmap);
        }
    }

}
