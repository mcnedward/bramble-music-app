package com.mcnedward.bramble.adapter.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mcnedward.bramble.media.Media;
import com.mcnedward.bramble.service.MediaService;
import com.mcnedward.bramble.utils.RippleUtil;
import com.mcnedward.bramble.view.MediaCard;
import com.mcnedward.bramble.view.mediaItem.MediaItem;
import com.mcnedward.bramble.view.mediaItem.SongMediaItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward on 24/12/15.
 */
public abstract class BaseMediaAdapter<T extends Media> extends BaseAdapter {

    protected List<T> groups;
    protected Context mContext;
    protected LayoutInflater inflater;

    public BaseMediaAdapter(Context context) {
        this.groups = new ArrayList<>();
        mContext = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public BaseMediaAdapter(List<T> groups, Context context) {
        this.groups = groups;
        mContext = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setGroups(List<T> groups) {
        this.groups = groups;
    }

    public void reset() {
        groups = new ArrayList<>();
    }

    protected abstract MediaItem getCustomView(T media);

    protected abstract void doOnClickAction(T media, MediaItem view);

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        final MediaItem mediaView;
        T item = getItem(position);
        if (convertView == null) {
            mediaView = getCustomView(item);
            convertView = mediaView;

            holder = new ViewHolder(mediaView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            mediaView = holder.mediaView;
        }
        mediaView.update(item);
        RippleUtil.setRippleBackground(mediaView, mContext);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOnClickAction(getItem(position), mediaView);
            }
        });
        return convertView;
    }

    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public T getItem(int position) {
        return groups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    protected static class ViewHolder {
        public MediaItem mediaView;

        public ViewHolder(MediaItem mediaView) {
            this.mediaView = mediaView;
        }
    }

}
