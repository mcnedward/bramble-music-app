package com.mcnedward.bramble.activity.artist;

import android.widget.GridView;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.adapter.grid.ArtistImageGridAdapter;
import com.mcnedward.bramble.enums.ArtistImageChooserType;

/**
 * Created by Edward on 5/29/2016.
 */
public class ArtistBackgroundChooserFragment extends ArtistImageChooserFragment {

    public ArtistBackgroundChooserFragment() {
        super(ArtistImageChooserType.BACKGROUND);
    }

    @Override
    protected void setupTitle(TextView txtChooserTitle) {
        txtChooserTitle.setText(getString(R.string.choose_artist_background) + " " + mArtist.getArtistName());
    }

    @Override
    protected void setupGridView(GridView gridView) {
        ArtistImageGridAdapter adapter = new ArtistImageGridAdapter(mContext, mArtist.getArtistImages());
        gridView.setAdapter(adapter);
    }
}
