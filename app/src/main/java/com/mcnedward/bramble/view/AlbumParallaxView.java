package com.mcnedward.bramble.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.utils.listener.ScrollViewListener;

/**
 * Created by edward on 23/12/15.
 */
public class AlbumParallaxView extends LinearLayout {
    final private static String TAG = "AlbumParallaxView";

    private Context context;
    private DisplayMetrics dm;

    private ParallaxScrollView mBgScrollView;
    private ParallaxScrollView mContentScrollView;

    public AlbumParallaxView(Context context) {
        super(context);
        this.context = context;
        inflate(context, R.layout.album_view, this);

        dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);

        mBgScrollView = (ParallaxScrollView) findViewById(R.id.background_sv);
        mContentScrollView = (ParallaxScrollView) findViewById(R.id.content_sv);
        mContentScrollView.setScrollViewListener(new ScrollViewListener() {
            @Override
            public void onScrollChanged(ParallaxScrollView scrollView, int x, int y, int oldX, int oldY) {
                Log.d(TAG, "SCROLLING mContent!");
                mBgScrollView.scrollTo(0, (int) (y * 1.25f));
            }
        });
        setForegroundContent(context);
        setBackgroundContent(context);
    }

    private void setForegroundContent(Context context) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.parallax_main_content);
        int spaceHeight = (dm.heightPixels / 2);
        Space space = new Space(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, spaceHeight);
        space.setLayoutParams(layoutParams);
        layout.addView(space);

        for (int x = 0; x < 51; x++) {
            TextView textView = new TextView(context);
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50));
            textView.setText("SOMETHING HERE!");
            layout.addView(textView);
        }
    }

    private void setBackgroundContent(Context context) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.parallax_background_content);
        ImageView image = (ImageView) findViewById(R.id.albumArt);

        int spaceHeight = (int)(dm.heightPixels / 1.3f);
        int imageHeight = (dm.heightPixels / 2);

        Space space = new Space(context);
        space.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, spaceHeight));
        layout.addView(space);

        image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, imageHeight));
        image.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_launcher));
    }

}
