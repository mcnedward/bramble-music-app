package com.mcnedward.bramble.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.entity.media.Media;
import com.mcnedward.bramble.utils.BitmapUtil;
import com.mcnedward.bramble.utils.RippleUtil;

/**
 * Created by Edward on 5/14/2016.
 */
public class MediaCard<T extends Media> extends LinearLayout {
    final private static String TAG = "MediaCard";

    private T mItem;
    private LruCache<String, Bitmap> cache;
    private Context context;
    private TextView txtMediaTitle;
    private SquareImageCard imgMediaIcon;

    public MediaCard(Context context, T item) {
        super(context);
        initialize(context, item, null);
    }

    public MediaCard(Context context, T item, LruCache<String, Bitmap> cache) {
        super(context);
        initialize(context, item, cache);
        update(item);
    }

    private void initialize(Context context, T item, LruCache<String, Bitmap> cache) {
        this.mItem = item;
        this.cache = cache;
        this.context = context;
        inflate(context, R.layout.item_media_grid, this);

        RippleUtil.setRippleBackground(this, R.color.FireBrick, R.color.White, context);
        txtMediaTitle = (TextView) findViewById(R.id.txt_media_title);
        imgMediaIcon = (SquareImageCard) findViewById(R.id.media_icon);
    }

    public void update(T item) {
        update(item, true);
    }

    public void update(T item, boolean updateImages) {
        mItem = item;
        txtMediaTitle.setText(item.getTitle());
        if (updateImages) {
            if (item.getImagePath() == null || item.getImagePath().equals("")) {
                imgMediaIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.no_album_art));
            } else {
                BitmapUtil.startBitmapLoadTask(context, item, this, cache);
            }
        }
    }

    public void updateAfterBitmapLoadTaskFinished(T item, Bitmap bitmap) {
        if (item.getCacheKey().equals(mItem.getCacheKey())) {
            txtMediaTitle.setText(item.getTitle());
            setImage(bitmap);
        }
        mItem = item;
    }

    public ImageView getImageView() {
        return imgMediaIcon;
    }

    public void setImage(Bitmap bitmap) {
        if (bitmap == null) {
            imgMediaIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.no_album_art));
        } else {
            imgMediaIcon.setImageBitmap(bitmap);
        }
    }
}