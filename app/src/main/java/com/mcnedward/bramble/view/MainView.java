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

    private NowPlayingView mNowPlayingView;

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
        mNowPlayingView = nowPlayingFragment.getNowPlayingView();

        adjustForNowPlayingTitleBar();
    }

    /**
     * Adjust height of container to account for the NowPlaying bar
     */
    private void adjustForNowPlayingTitleBar() {
        ViewTreeObserver observer = mNowPlayingView.getViewTreeObserver();
        final View container = findViewById(R.id.container_main);

        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mNowPlayingView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int padding = mNowPlayingView.getTitleBar().getHeight();
                CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, container.getHeight() - padding));
                container.setLayoutParams(layoutParams);
                mNowPlayingView.snapToBottom();
            }
        });
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mNowPlayingView.updateViewMeasures((ViewGroup) findViewById(R.id.now_playing_container));
    }

    public NowPlayingView getNowPlayingView() {
        return mNowPlayingView;
    }

}
