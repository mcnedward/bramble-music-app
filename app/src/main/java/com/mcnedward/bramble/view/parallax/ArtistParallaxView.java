package com.mcnedward.bramble.view.parallax;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.entity.data.ArtistImage;
import com.mcnedward.bramble.entity.media.Artist;
import com.mcnedward.bramble.listener.MediaGridChangeListener;
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
        loadImage(imageView);
    }

    private void loadImage(ImageView imageView) {
        Bitmap bitmap = mArtist.getBitmap();
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            String imageUrl = mArtist.getImageUrl();
            if (imageUrl == null) {
                imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.no_album_art));
            } else {
                PicassoUtil.getPicasso(mContext).with(mContext).load(imageUrl).centerInside().fit().into(imageView);
            }
        }
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
    protected float getImageScaleHeight() {
        return 3f;
    }

    @Override
    public void notifyMediaGridChange(ArtistImage item) {
        mArtist.setSelectedImage(item);
        loadImage(mImgBackground);
    }
}
