package com.mcnedward.bramble.adapter.grid;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mcnedward.bramble.entity.ITitleAndImage;
import com.mcnedward.bramble.enums.CardType;
import com.mcnedward.bramble.utils.PicassoUtil;
import com.mcnedward.bramble.utils.RippleUtil;
import com.mcnedward.bramble.view.card.MediaCard;
import com.squareup.picasso.Callback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 5/14/2016.
 * An adapter for displaying MediaCards in a grid format. The cards can be either a square or rectangle, with the default being rectangular.
 */
public abstract class MediaGridAdapter<T extends ITitleAndImage> extends BaseAdapter {

    protected List<T> mGroups;
    protected Context mContext;
    protected LayoutInflater inflater;
    private LruCache<String, Bitmap> mLruCache;
    private CardType mCardType;

    public MediaGridAdapter(Context context, List<T> groups, CardType cardType) {
        mGroups = groups;
        mContext = context;
        mCardType = cardType;
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

    public MediaGridAdapter(Context context, List<T> groups) {
        this(context, groups, CardType.RECT);
    }

    public MediaGridAdapter(Context context, CardType cardType) {
        this(context, new ArrayList<T>(), cardType);
    }

    public MediaGridAdapter(Context context) {
        this(context, new ArrayList<T>(), CardType.RECT);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        MediaCard mediaCard;
        final T item = getItem(position);
        if (convertView == null) {
            mediaCard = new MediaCard(mContext, item, mLruCache, mCardType);
            convertView = mediaCard;

            holder = new ViewHolder(mediaCard);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            mediaCard = holder.mediaCard;
        }

        updateMediaCard(item, mediaCard);

        RippleUtil.setRippleBackground(mediaCard, mContext);
        mediaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOnClickAction(item, v);
            }
        });
        return convertView;
    }

    /**
     * Updates the MediaCard. This can be overriden to allow for different image behaviour.
     * @param item The current item.
     * @param mediaCard The current MediaCard.
     */
    protected void updateMediaCard(T item, MediaCard mediaCard) {
        // Check if there is a bitmap path first
        String imagePath = item.getImagePath();
        String imageUrl = item.getImageUrl();
        if ((imagePath == null || imagePath.equals("")) && (imageUrl != null && !imageUrl.equals(""))) {
            // No path, so try for a url instead
            mediaCard.update(item, false);
            PicassoUtil.getPicasso(mContext).with(mContext).load(imageUrl).fit().centerInside().into(mediaCard.getImageView(), getPicassoCallback(item, mediaCard));
        } else {
            mediaCard.update(item);
        }
    }

    private Callback getPicassoCallback(final T item, final MediaCard mediaCard) {
        return new Callback() {
            @Override
            public void onSuccess() {
                mediaCard.update(item, false);
            }
            @Override
            public void onError() {
                mediaCard.update(item);
            }
        };
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
