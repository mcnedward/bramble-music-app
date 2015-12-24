package com.mcnedward.bramble.utils.loader;

import android.content.Context;

import com.mcnedward.bramble.media.Artist;

import java.util.List;

/**
 * Created by edward on 23/12/15.
 */
public class ArtistDataLoader extends BaseDataLoader<Artist> {

    private String mSelection;
    private String[] mSelectionArgs;
    private String mGroupBy;
    private String mHaving;
    private String mOrderBy;

    public ArtistDataLoader(Context context, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        super(context);
    }

    @Override
    public List<Artist> buildList() {
        return null;
    }
}
