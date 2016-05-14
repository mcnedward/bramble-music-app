package com.mcnedward.bramble.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Artist;
import com.mcnedward.bramble.view.ArtistAlbumListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward on 21/12/15.
 */
public class MediaExpandableListAdapter extends BaseExpandableListAdapter {
    private static String TAG = "MediaExpandableListAdapter";

    private Context context;

    private List<Artist> groups;
    private List<List<Album>> children;

    public MediaExpandableListAdapter(Context context) {
        this.context = context;
        groups = new ArrayList<>();
        children = new ArrayList<>();
    }

    public void addGroup(Artist group) {
        groups.add(group);
    }

    public ArtistAlbumListView getGenericView(Artist artist) {
        // Layout parameters for the ExpandableListView
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 98);

        TextView tv = new TextView(this.context);
        tv.setLayoutParams(lp);
        tv.setMinimumWidth(550); // Sets the width of the text view for the list
        // tv.setLongClickable(true);
        tv.setTextSize(20);
        tv.setTypeface(null, Typeface.BOLD);

        // Center the text vertically
        tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        // Set the text starting position
        tv.setPadding(45, 0, 0, 0);

        ArtistAlbumListView artistAlbumListView = new ArtistAlbumListView(artist, context);

        return artistAlbumListView;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ArtistAlbumListView artistView = getGenericView(groups.get(groupPosition));
        return artistView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Log.d(TAG, "GETTING CHILD VIEW");

        if (convertView == null) {
            convertView = new ArtistAlbumListView(getGroup(groupPosition), context);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return children.get(groupPosition).size();
    }

    @Override
    public Artist getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Album getChild(int groupPosition, int childPosition) {
        return children.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Change this?
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Change this?
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
