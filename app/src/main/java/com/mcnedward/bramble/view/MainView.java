package com.mcnedward.bramble.view;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.activity.fragment.NowPlayingFragment;
import com.mcnedward.bramble.view.nowPlaying.NowPlayingTitleBar;
import com.mcnedward.bramble.view.nowPlaying.NowPlayingView;

/**
 * Created by edward on 28/12/15.
 */
public class MainView extends FrameLayout {
    private final static String TAG = "MainView";

    private NowPlayingView nowPlayingView;

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public MainView(Context context) {
        super(context);
        initialize(context);
    }

    private void initialize(Context context) {
        inflate(context, R.layout.activity_main, this);
        NowPlayingFragment nowPlayingFragment = (NowPlayingFragment) ((FragmentActivity) context).getSupportFragmentManager().findFragmentById(R.id.now_playing);
        nowPlayingView = nowPlayingFragment.getNowPlayingView();

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int usableHeight = dm.heightPixels;
        ((Activity) context).getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        int realHeight = dm.heightPixels;
        int softButtonHeight = realHeight - usableHeight;
        int height = (usableHeight - softButtonHeight) - NowPlayingTitleBar.HEIGHT;
        nowPlayingView.snapToBottom(height);

        adjustForNowPlayingTitleBar();
    }

    /**
     * Adjust height of container to account for the NowPlaying bar
     */
    private void adjustForNowPlayingTitleBar() {
        ViewTreeObserver observer = nowPlayingView.getViewTreeObserver();
        final View container = findViewById(R.id.container_main);

        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                nowPlayingView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int padding = nowPlayingView.getTitleBar().getHeight();
                CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, container.getHeight() - padding));
                container.setLayoutParams(layoutParams);
            }
        });
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        nowPlayingView.updateViewMeasures((ViewGroup) findViewById(R.id.now_playing_container));
    }

    public NowPlayingView getNowPlayingView() {
        return nowPlayingView;
    }

}