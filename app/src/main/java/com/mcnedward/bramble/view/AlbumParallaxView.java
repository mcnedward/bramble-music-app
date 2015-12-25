package com.mcnedward.bramble.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
        space.setBackgroundColor(ContextCompat.getColor(context, R.color.Red));

        AlbumPopupListAdapter adapter = new AlbumPopupListAdapter(spaceHeight, context);
        List<Song> songs = MainActivity.mediaService.getSongsForAlbum(album);
        adapter.setGroups(songs);

        for (Song song : songs) {
            TextView textView = new TextView(context);
            textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setPadding(15, 15, 15, 15);
            textView.setText(song.getTitle());
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.WhiteSmoke));
            textView.setClickable(true);
            textView.setFocusable(true);
            textView.setFocusableInTouchMode(true);
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

//        final ListView listView = new ListView(context);
//        listView.setAdapter(adapter);
//        listView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_DISABLED);
//        listView.setNestedScrollingEnabled(true);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d(TAG, listView.getItemAtPosition(position) + " CLICKED!");
//            }
//        });
//        layout.addView(listView);

//        ListView listView = (ListView) findViewById(R.id.album_contents_list);
//        listView.setAdapter(adapter);
//        for (int x = 0; x < 51; x++) {
//            TextView textView = new TextView(context);
//            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50));
//            textView.setText("SOMETHING HERE!");
//            layout.addView(textView);
//        }
    }

    private void setBackgroundContent(Context context) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.parallax_background_content);
        ImageView imgAlbumArt = (ImageView) findViewById(R.id.bg_album_art);

        int spaceHeight = (int) (dm.heightPixels / 1.3f);
        int imageHeight = (dm.heightPixels / 2);

        Space space = new Space(context);
        space.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, spaceHeight));
        layout.addView(space);

        imgAlbumArt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, imageHeight));
        String albumArt = album.getAlbumArt();
        if (albumArt != null) {
            // Create the album art bitmap and scale it to fit properly and avoid over using memory
            File imageFile = new File(album.getAlbumArt());
            Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            imgAlbumArt.setImageBitmap(imageBitmap);
        }
    }

}
