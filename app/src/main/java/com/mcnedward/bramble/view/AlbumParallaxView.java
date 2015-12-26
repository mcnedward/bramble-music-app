package com.mcnedward.bramble.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.RippleDrawable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.activity.MainActivity;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Song;
import com.mcnedward.bramble.utils.Extension;
import com.mcnedward.bramble.utils.adapter.AlbumPopupListAdapter;
import com.mcnedward.bramble.utils.adapter.MediaListAdapter;
import com.mcnedward.bramble.utils.adapter.SongListAdapter;
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
        int spaceHeight = (dm.heightPixels / 2);
        Space space = new Space(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, spaceHeight);
        space.setLayoutParams(layoutParams);
        layout.addView(space);

        List<Song> songs = MainActivity.mediaService.getSongsForAlbum(album);

        for (Song song : songs) {
            TextView textView = (TextView) inflate(context, R.layout.simple_list_item, null);
            textView.setText(song.getTitle());
            textView.setClickable(true);
            textView.setFocusable(true);

            RippleDrawable newImage = Extension.rippleDrawable(context);
            textView.setBackground(newImage);

            final Context c = context;
            final String title = song.getTitle();
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, title + " CLICKED!");
                }
            });
            layout.addView(textView);
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
