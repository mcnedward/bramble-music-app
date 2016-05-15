package com.mcnedward.bramble.adapter.grid;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mcnedward.bramble.media.Media;
import com.mcnedward.bramble.utils.RippleUtil;
import com.mcnedward.bramble.view.MediaCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 5/14/2016.
 */
public abstract class MediaGridAdapter<T extends Media> extends BaseAdapter {

    protected List<T> groups;
    protected Context context;
    protected LayoutInflater inflater;
    private LruCache<String, Bitmap> mLruCache;

    public MediaGridAdapter(Context context) {
        this.context = context;
        groups = new ArrayList<>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory()) / 1024;
        final int cacheSize = maxMemory / 4;

        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        MediaCard mediaCard;
        T item = getItem(position);
        if (convertView == null) {
            mediaCard = new MediaCard(item, mLruCache, context);
            convertView = mediaCard;

            holder = new ViewHolder(mediaCard);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            mediaCard = holder.mediaCard;
        }

        mediaCard.update(item);
        RippleUtil.setRippleBackground(mediaCard, context);
        setOnClickListener(item, mediaCard);
        return convertView;
    }

    public void setGroups(List<T> groups) {
        this.groups = groups;
    }

    public void reset() {
        groups = new ArrayList<>();
    }

    protected abstract void setOnClickListener(T media, View view);

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
        public MediaCard mediaCard;

        public ViewHolder(MediaCard mediaCard) {
            this.mediaCard = mediaCard;
        }
    }
}
