package com.mcnedward.bramble.view.parallax;

import android.content.Context;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.entity.data.ArtistImage;
import com.mcnedward.bramble.entity.media.Artist;
import com.mcnedward.bramble.utils.PicassoUtil;

/**
 * Created by edward on 29/5/16.
 *
 * A ParallaxView for displaying Artist information.
 */
public class ArtistParallaxView extends ParallaxView<Artist> {
    final private static String TAG = "ArtistParallaxView";

    private Artist mArtist;

    public ArtistParallaxView(Context context, Artist artist) {
        super(context);
        mArtist = artist;
        initialize();
    }

    @Override
    protected void afterAddingForegroundSpace(LinearLayout layout) {
        ArtistParallaxInfoView infoView = new ArtistParallaxInfoView(mContext, mArtist);
        layout.addView(infoView);
    }

    @Override
    protected void setupBackgroundContent(LinearLayout layout) {
    }

    @Override
    protected void loadBackgroundImage(ImageView imageView) {
        // Get the higher resolution image if it exists
        ArtistImage artistImage = mArtist.getArtistImages().get(0);
        String imageUrl = artistImage.getMediaUrl();
        if (imageUrl == null) return;
        PicassoUtil.getPicasso(mContext).with(mContext).load(imageUrl).centerInside().fit().into(imageView);
    }

    @Override
    protected void onGlobalLayoutChange() {
        // This needs to be done here, once all other layout changes are finished
        LinearLayout layout = (LinearLayout) findViewById(R.id.parallax_main_content);

        GridView gridView = new ParallaxGridView(mContext, mArtist);
        layout.addView(gridView);
    }

    @Override
    protected float getForegroundSpaceScaleHeight() {
        return 3f;
    }

    @Override
    protected float getBackgroundSpaceScaleHeight() {
        return 2f;
    }

    @Override
    protected float getImageScaleHeight() {
        return 3f;
    }

}
