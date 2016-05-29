package com.mcnedward.bramble.adapter.grid;

import android.content.Context;
import android.view.View;

import com.mcnedward.bramble.entity.data.ArtistImage;
import com.mcnedward.bramble.utils.PicassoUtil;
import com.mcnedward.bramble.view.card.MediaCard;

import java.util.List;

/**
 * Created by Edward on 5/14/2016.
 */
public class ArtistImageGridAdapter extends MediaGridAdapter<ArtistImage> {

    public ArtistImageGridAdapter(Context context, List<ArtistImage> groups) {
        super(context, groups);
    }

    @Override
    protected void doOnClickAction(ArtistImage media, View view) {

    }

    @Override
    protected void updateMediaCard(ArtistImage item, MediaCard mediaCard) {
        String imageUrl = item.getMediaUrl();
        if (imageUrl == null || !imageUrl.equals("")) {
            mediaCard.update(item, false);
            PicassoUtil.getPicasso(mContext).with(mContext).load(imageUrl).fit().centerInside().into(mediaCard.getImageView());
        } else {
            mediaCard.update(item);
        }
    }

}
