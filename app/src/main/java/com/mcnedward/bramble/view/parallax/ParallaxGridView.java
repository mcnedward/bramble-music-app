package com.mcnedward.bramble.view.parallax;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.mcnedward.bramble.adapter.grid.MediaGridAdapter;
import com.mcnedward.bramble.entity.media.Album;
import com.mcnedward.bramble.entity.media.Artist;
import com.mcnedward.bramble.utils.MusicUtil;
import com.mcnedward.bramble.utils.RepositoryUtil;

import java.util.List;

/**
 * Created by Edward on 5/29/2016.
 *
 * A GridView for use in the ArtistParallaxView.
 */
public class ParallaxGridView extends GridView {

    public ParallaxGridView(Context context, Artist artist) {
        super(context);
        initialize(context, artist);
    }

    private void initialize(final Context context, Artist artist) {
        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setVerticalScrollBarEnabled(false);
        setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        setNumColumns(2);

        List<Album> albums = RepositoryUtil.getAlbumRepository(context).getAlbumsForArtist(artist.getId());
        MediaGridAdapter<Album> albumGridAdapter = new MediaGridAdapter<Album>(context, albums) {
            @Override
            protected void doOnClickAction(Album media, View view) {
                MusicUtil.openAlbum(context, media);
            }
        };
        setAdapter(albumGridAdapter);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
