package com.mcnedward.bramble.activity.artist;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.entity.media.Artist;
import com.mcnedward.bramble.enums.ArtistImageChooserType;
import com.mcnedward.bramble.enums.IntentKey;

/**
 * Created by Edward on 5/29/2016.
 */
public abstract class ArtistImageChooserFragment extends Fragment {

    protected ArtistImageChooserType mType;
    protected Context mContext;
    protected Artist mArtist;

    public ArtistImageChooserFragment(ArtistImageChooserType type) {
        mType = type;
    }

    public static ArtistImageChooserFragment newInstance(Artist artist, ArtistImageChooserType type) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.ARTIST.name(), artist);
        ArtistImageChooserFragment fragment;
        switch (type) {
            case THUMBNAIL:
                fragment = new ArtistThumbnailChooserFragment();
                break;
            case BACKGROUND:
                fragment = new ArtistBackgroundChooserFragment();
                break;
            default:
                fragment = null;
                break;
        }
        if (fragment != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View thisView = inflater.inflate(R.layout.fragment_artist_image_chooser, container, false);
        mContext = thisView.getContext();

        mArtist = (Artist) getArguments().getSerializable(IntentKey.ARTIST.name());

        setupTitle((TextView) thisView.findViewById(R.id.text_chooser_title));
        setupGridView((GridView) thisView.findViewById(R.id.grid_artist_images));

        return thisView;
    }

    protected abstract void setupTitle(TextView txtChooserTitle);

    protected abstract void setupGridView(GridView gridView);

}
