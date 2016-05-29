package com.mcnedward.bramble.view.parallax;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    }

    @Override
    protected void setupBackgroundContent(LinearLayout layout) {
    }

    @Override
    protected void loadBackgroundImage(ImageView imageView) {
        ArtistImage artistImage = mArtist.getArtistImage();
        if (artistImage == null || artistImage.getThumbnail() == null) return;
        String mediaUrl = artistImage.getThumbnail().getMediaUrl();
        PicassoUtil.getPicasso(mContext).with(mContext).load(mediaUrl).fit().centerInside().into(imageView);
    }

    @Override
    protected void onGlobalLayoutChange() {
        // This needs to be done here, once all other layout changes are finished
        LinearLayout layout = (LinearLayout) findViewById(R.id.parallax_main_content);

        LinearLayout artistInfoView = (LinearLayout) inflate(mContext, R.layout.view_artist_parallax_info, null);
        TextView txtArtistTitle = (TextView) artistInfoView.findViewById(R.id.artist_title);
        txtArtistTitle.setText(mArtist.getArtistName());
        layout.addView(artistInfoView);

        GridView gridView = new ParallaxGridView(mContext, mArtist);
        layout.addView(gridView);
    }

}
