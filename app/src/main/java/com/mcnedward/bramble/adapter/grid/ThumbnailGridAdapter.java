package com.mcnedward.bramble.adapter.grid;

import android.content.Context;
import android.view.View;

import com.mcnedward.bramble.entity.data.Thumbnail;

import java.util.List;

/**
 * Created by Edward on 5/14/2016.
 */
public class ThumbnailGridAdapter extends MediaGridAdapter<Thumbnail> {

    public ThumbnailGridAdapter(Context context, List<Thumbnail> groups) {
        super(context, groups);
    }

    @Override
    protected void doOnClickAction(Thumbnail media, View view) {

    }

}
