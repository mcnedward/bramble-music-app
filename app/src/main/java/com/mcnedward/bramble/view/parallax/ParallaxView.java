package com.mcnedward.bramble.view.parallax;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.activity.fragment.NowPlayingFragment;
import com.mcnedward.bramble.listener.ScrollViewListener;
import com.mcnedward.bramble.view.ParallaxScrollView;
import com.mcnedward.bramble.view.nowPlaying.NowPlayingView;

/**
 * Created by Edward on 5/29/2016.
 * <p/>
 * A View that contains 2 scrollable layouts. One in the background is used for displaying an image, one in the foreground for displaying content
 * that should be interacted with.
 * The background image will be hidden when the foreground content is scrolled up, then shown again when the foreground content has moved back down
 * a certain amount.
 */
public abstract class ParallaxView<T> extends LinearLayout {
    private static final String TAG = "ParallaxView";
    private static final float FOREGROUND_SPACE_SCALE_HEIGHT = 2f;
    private static final float BACKGROUND_SPACE_SCALE_HEIGHT = 1.3f;
    private static final float IMAGE_SCALE_HEIGHT = 2f;

    protected Context mContext;
    protected ParallaxScrollView mBgScrollView;
    protected ParallaxScrollView mContentScrollView;
    protected NowPlayingView mNowPlayingView;

    private DisplayMetrics dm;

    public ParallaxView(Context context) {
        super(context);
        mContext = context;
        inflate(context, R.layout.view_parallax, this);

        dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);

        setScrollViews();

        NowPlayingFragment nowPlayingFragment = (NowPlayingFragment) ((FragmentActivity) context).getSupportFragmentManager().findFragmentById(R.id
                .album_now_playing);
        mNowPlayingView = nowPlayingFragment.getNowPlayingView();

        adjustForNowPlayingTitleBar();
    }

    /**
     * Initialize should be called in the view that extends this.
     */
    protected void initialize() {
        initializeForegroundContent();
        initializeBackgroundContent();
    }

    /**
     * This method is called after Space has been added to the foreground content. This should be used to setup anything that might need to go in
     * the foreground.
     *
     * @param layout
     */
    protected abstract void afterAddingForegroundSpace(LinearLayout layout);

    /**
     * This method is called at the end of the background content initialization. This should be used to setup anything that might need to go in
     * the background.
     *
     * @param layout
     */
    protected abstract void setupBackgroundContent(LinearLayout layout);

    /**
     * This is used to load the image displayed in the background.
     *
     * @param imageView The background image that needs to be loaded.
     */
    protected abstract void loadBackgroundImage(ImageView imageView);

    /**
     * This method is called at the end of the layout changes. This should be used to setup anything that can only be done when all other layout
     * changes have been made.
     * An example is for the ArtistParallaxView, the GridView needs to be added only once the background is loaded and the NowPlayingView is locked
     * in place.
     */
    protected abstract void onGlobalLayoutChange();

    /**
     * Initializes and sets the listener for the ScrollViews.
     */
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

    private void initializeForegroundContent() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.parallax_main_content);
        // Set the empty space to push list below album art
        int spaceHeight = (int) (dm.heightPixels / getForegroundSpaceScaleHeight());
        Space space = new Space(mContext);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, spaceHeight);
        space.setLayoutParams(layoutParams);
        layout.addView(space);

        afterAddingForegroundSpace(layout);
    }

    private void initializeBackgroundContent() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.parallax_background_content);
        ImageView imgBackground = (ImageView) findViewById(R.id.bg_album_art);

        int spaceHeight = (int) (dm.heightPixels / getBackgroundSpaceScaleHeight());
        int imageHeight = (int) (dm.heightPixels / getImageScaleHeight());

        Space space = new Space(mContext);
        space.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, spaceHeight));
        layout.addView(space);

        imgBackground.setLayoutParams(new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, imageHeight));
        loadBackgroundImage(imgBackground);

        setupBackgroundContent(layout);
    }

    /**
     * Gets the amount to scale the Space to push the background image up.
     * Can be overriden to change the layout of the background image.
     *
     * @return The space scale height
     */
    protected float getForegroundSpaceScaleHeight() {
        return FOREGROUND_SPACE_SCALE_HEIGHT;
    }

    /**
     * Gets the amount to scale the Space to push the background image up.
     * Can be overriden to change the layout of the background image.
     *
     * @return The space scale height
     */
    protected float getBackgroundSpaceScaleHeight() {
        return BACKGROUND_SPACE_SCALE_HEIGHT;
    }

    /**
     * Gets the amount to scale the background image.
     * Can be overriden to change the layout of the background image.
     *
     * @return The image scale height
     */
    protected float getImageScaleHeight() {
        return IMAGE_SCALE_HEIGHT;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mNowPlayingView.updateViewMeasures((ViewGroup) findViewById(R.id.album_now_playing_container));
    }

    /**
     * Adjust height of container to account for the NowPlaying bar.
     */
    private void adjustForNowPlayingTitleBar() {
        ViewTreeObserver observer = mNowPlayingView.getViewTreeObserver();
        final RelativeLayout container = (RelativeLayout) findViewById(R.id.container_parallax);

        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mNowPlayingView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int padding = mNowPlayingView.getTitleBar().getHeight();
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams
                        .MATCH_PARENT, container.getHeight() - padding));
                container.setLayoutParams(layoutParams);
                mNowPlayingView.snapToBottom();

                onGlobalLayoutChange();
            }
        });
    }

    /**
     * Returns the NowPlayingView for this view.
     *
     * @return The NowPlayingView
     */
    public NowPlayingView getNowPlayingView() {
        return mNowPlayingView;
    }

}
