package com.mcnedward.bramble.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.activity.fragment.NowPlayingFragment;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Song;
import com.mcnedward.bramble.listener.ScrollViewListener;
import com.mcnedward.bramble.repository.SongRepository;
import com.mcnedward.bramble.service.MediaService;
import com.mcnedward.bramble.view.nowPlaying.NowPlayingView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by edward on 23/12/15.
 */
public class AlbumParallaxView extends LinearLayout {
    final private static String TAG = "AlbumParallaxView";

    private Context mContext;
    private SongRepository mSongRepository;
    private Album mAlbum;

    private ParallaxScrollView mBgScrollView;
    private ParallaxScrollView mContentScrollView;
    private NowPlayingView mNowPlayingView;

    private DisplayMetrics dm;

    public AlbumParallaxView(Album album, Context context) {
        super(context);
        mContext = context;
        mSongRepository = new SongRepository(mContext);
        mAlbum = album;
        inflate(context, R.layout.view_album, this);

        dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);

        setScrollViews();

        setForegroundContent(context);
        setBackgroundContent(context);

        NowPlayingFragment nowPlayingFragment = (NowPlayingFragment) ((FragmentActivity) context).getSupportFragmentManager().findFragmentById(R.id
                .album_now_playing);
        mNowPlayingView = nowPlayingFragment.getNowPlayingView();

        adjustForNowPlayingTitleBar();
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
        LinearLayout albumTitleView = (LinearLayout) inflate(context, R.layout.view_album_title, null);
        TextView txtAlbumName = (TextView) albumTitleView.findViewById(R.id.album_title_title);
        txtAlbumName.setText(mAlbum.getAlbumName());
        TextView txtArtistName = (TextView) albumTitleView.findViewById(R.id.album_title_artist);
        txtArtistName.setText(mAlbum.getArtist());
        layout.addView(albumTitleView);

        // Set the list of songs
        List<Song> songs = mSongRepository.getSongsForAlbum(mAlbum.getId());
        mAlbum.setAlbumSongIds(songs);
        for (Song song : songs) {
            AlbumSongItem songItem = new AlbumSongItem(mAlbum, song, context);
            layout.addView(songItem);
            MediaService.attachMediaChangeListener(songItem);
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
        String albumArt = mAlbum.getAlbumArt();
        if (albumArt != null) {
            // Create the album art bitmap and scale it to fit properly and avoid over using memory
            File imageFile = new File(mAlbum.getAlbumArt());
            Picasso.with(context).load(imageFile).into(imgAlbumArt);
        }
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mNowPlayingView.updateViewMeasures((ViewGroup) findViewById(R.id.album_now_playing_container));
    }

    /**
     * Adjust height of container to account for the NowPlaying bar
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
            }
        });
    }

    public NowPlayingView getNowPlayingView() {
        return mNowPlayingView;
    }

}
