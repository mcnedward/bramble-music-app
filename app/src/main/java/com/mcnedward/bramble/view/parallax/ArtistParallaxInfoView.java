package com.mcnedward.bramble.view.parallax;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.entity.media.Artist;
import com.mcnedward.bramble.utils.MusicUtil;
import com.mcnedward.bramble.utils.RippleUtil;

/**
 * Created by edward on 26/12/15.
 *
 * A view for the info of the artist on the bar in the Artist activity.
 */
public class ArtistParallaxInfoView extends RelativeLayout {
    private final static String TAG = "ArtistParallaxInfoView";

    public ArtistParallaxInfoView(Context context, Artist artist) {
        super(context);
        inflate(context, R.layout.view_artist_parallax_info, this);
        ((TextView) findViewById(R.id.artist_info_title)).setText(artist.getTitle());

        ImageView imgArtistImageChooser = (ImageView) findViewById(R.id.artist_info_image_chooser);
        imgArtistImageChooser.setClickable(true);
        imgArtistImageChooser.setFocusable(true);
        RippleUtil.setRippleBackground(imgArtistImageChooser, context);
        final Context theContext = context;
        final Artist theArtist = artist;
        imgArtistImageChooser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicUtil.startArtistImageChooserActivity(theContext, theArtist);
            }
        });
    }

}
