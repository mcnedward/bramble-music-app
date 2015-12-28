package com.mcnedward.bramble.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.view.nowPlaying.NowPlayingView;

/**
 * Created by edward on 28/12/15.
 */
public class MainView extends FrameLayout {

    private Context context;

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
        this.context = context;
        inflate(context, R.layout.activity_main, this);
        nowPlayingView = ((NowPlayingView) findViewById(R.id.now_playing));
        nowPlayingView.setRoot((ViewGroup) findViewById(R.id.now_playing_container));
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        nowPlayingView.snapToBottom();
    }

}
