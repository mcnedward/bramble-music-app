package com.mcnedward.bramble.utils.refresh;

import android.app.Activity;
import android.content.Context;
import android.widget.ListView;

import com.mcnedward.bramble.utils.adapter.MediaListAdapter;

import java.util.List;

/**
 * Created by edward on 24/12/15.
 */
public abstract class Refresher<T> {

    protected Context context;

    public Refresher(Context context) {
        this.context = context;
    }

    protected abstract void setupListView(ListView view, MediaListAdapter<T> adapter, List<T> mediaList);

    protected abstract int getResourceId();

    public void refresh(List<T> mediaList) {
        if (mediaList != null && !mediaList.isEmpty()) {
            ListView listView = (ListView) ((Activity) context).findViewById(getResourceId());
            MediaListAdapter<T> adapter = new MediaListAdapter<>(mediaList, context);

            listView.setAdapter(adapter);
        } else {
            // Clear everything out here
        }
    }

}
