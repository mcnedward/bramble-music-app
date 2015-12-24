package com.mcnedward.bramble.utils.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.activity.AlbumPopup;
import com.mcnedward.bramble.media.Artist;

import java.util.List;

/**
 * Created by edward on 22/12/15.
 */
public class MediaListAdapter<T> extends BrambleBaseAdapter<T> {
    final private static String TAG = "MediaListAdapter";

    public MediaListAdapter(List<T> groups, Context context) {
        super(groups, context);
    }

    /**
     * This method creates a generic view for displaying items in the list view
     *
     * @return TextView
     */
    @Override
    protected View getCustomView(int position) {
        View view = inflater.inflate(R.layout.simple_list_item, null);
        return view;
    }

    @Override
    protected void setViewContent(int position, View view) {
        T object = getItem(position);
        ((TextView) view).setText(object.toString());
//        final Artist artist = (Artist) groups.get(position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO This has to be changed
//                openPopupWindow(artist);
            }
        });
    }

    private void openPopupWindow(Artist artist) {
        Intent intent = new Intent(context, AlbumPopup.class);
        intent.putExtra("artist", artist);
        context.startActivity(intent);
    }

}
