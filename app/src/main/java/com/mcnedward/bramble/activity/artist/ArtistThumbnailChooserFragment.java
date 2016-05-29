package com.mcnedward.bramble.activity.artist;

import android.widget.GridView;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.adapter.grid.ThumbnailGridAdapter;
import com.mcnedward.bramble.entity.data.ArtistImage;
import com.mcnedward.bramble.entity.data.Thumbnail;
import com.mcnedward.bramble.enums.ArtistImageChooserType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 5/29/2016.
 */
public class ArtistThumbnailChooserFragment extends ArtistImageChooserFragment {

    public ArtistThumbnailChooserFragment() {
        super(ArtistImageChooserType.THUMBNAIL);
    }

    @Override
    protected void setupTitle(TextView txtChooserTitle) {
        txtChooserTitle.setText(getString(R.string.choose_artist_thumbnail) + " " + mArtist.getArtistName());
    }

    @Override
    protected void setupGridView(GridView gridView) {
        if (mArtist.getArtistImages() == null) return;
        List<Thumbnail> thumbnails = new ArrayList<>();
        for (ArtistImage artistImage : mArtist.getArtistImages()) {
            thumbnails.add(artistImage.getThumbnail());
        }
        ThumbnailGridAdapter adapter = new ThumbnailGridAdapter(mContext, thumbnails);
        gridView.setAdapter(adapter);
    }
}
