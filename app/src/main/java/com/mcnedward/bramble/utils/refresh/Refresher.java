package com.mcnedward.bramble.utils.refresh;

import android.app.Activity;
import android.content.Context;
import android.widget.ListView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.utils.MediaType;
import com.mcnedward.bramble.utils.adapter.MediaListAdapter;

import java.util.List;

/**
 * Created by edward on 24/12/15.
 */
public class Refresher {

    protected Context context;

    public Refresher(Context context) {
        this.context = context;
    }

    public <T> void refresh(List<T> mediaList, MediaType mediaType) {
        if (mediaList != null && !mediaList.isEmpty()) {
            int resourceId;
            switch(mediaType) {
                case ARTIST:
                    resourceId = R.id.artist_list;
                    break;
                case ALBUM:
                    resourceId = R.id.album_list;
                    break;
                default:
                    resourceId = R.id.media_list;
                    break;
            }
            ListView listView = (ListView) ((Activity) context).findViewById(resourceId);
            MediaListAdapter<T> adapter = new MediaListAdapter<>(mediaList, context);
            listView.setAdapter(adapter);
        } else {
            // Clear everything out here
        }
    }

}
