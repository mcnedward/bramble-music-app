package com.mcnedward.bramble.adapter.grid;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mcnedward.bramble.entity.media.Media;
import com.mcnedward.bramble.utils.PicassoUtil;
import com.mcnedward.bramble.utils.RippleUtil;
import com.mcnedward.bramble.view.MediaCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 5/14/2016.
 */
public abstract class MediaGridAdapter<T extends Media> extends BaseAdapter {

    protected List<T> mGroups;
    protected Context mContext;
    protected LayoutInflater inflater;
    private LruCache<String, Bitmap> mLruCache;

    public MediaGridAdapter(Context context, List<T> groups) {
        mGroups = groups;
        mContext = context;
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

    public MediaGridAdapter(Context context) {
        this(context, new ArrayList<T>());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        MediaCard mediaCard;
        final T item = getItem(position);
        if (convertView == null) {
            mediaCard = new MediaCard(mContext, item, mLruCache);
            convertView = mediaCard;

            holder = new ViewHolder(mediaCard);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            mediaCard = holder.mediaCard;
        }

        // Check if there is a bitmap path first
        String mediaImageUrl = item.getImageUrl();
        if (mediaImageUrl != null && !mediaImageUrl.equals("")){
            mediaCard.update(item, false);
            PicassoUtil.getPicasso(mContext).with(mContext).load(mediaImageUrl).fit().centerInside().into(mediaCard.getImageView());
        } else {
            mediaCard.update(item);
        }
        RippleUtil.setRippleBackground(mediaCard, mContext);
        mediaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOnClickAction(item, v);
            }
        });
        return convertView;
    }

    public void updateItem(T item) {
        if (mGroups == null || mGroups.isEmpty()) return;
        for (int i = 0; i < mGroups.size(); i++) {
            T groupItem = mGroups.get(i);
            if (groupItem.getCacheKey().equals(item.getCacheKey())) {
                mGroups.set(i, item);
                return;
            }
        }
    }

    public void setGroups(List<T> groups) {
        mGroups = groups;
    }

    public void reset() {
        mGroups = new ArrayList<>();
    }

    protected abstract void doOnClickAction(T media, View view);

    @Override
    public int getCount() {
        return mGroups.size();
    }

    @Override
    public T getItem(int position) {
        return mGroups.get(position);
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
