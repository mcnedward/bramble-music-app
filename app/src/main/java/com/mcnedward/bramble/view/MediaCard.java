package com.mcnedward.bramble.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.media.Media;
import com.mcnedward.bramble.utils.BitmapUtil;
import com.mcnedward.bramble.utils.RippleUtil;

/**
 * Created by Edward on 5/14/2016.
 */
public class MediaCard<T extends Media> extends LinearLayout {
    final private static String TAG = "MediaCard";

    private T item;
    private LruCache<String, Bitmap> cache;
    private Context context;
    private TextView txtMediaTitle;
    private SquareImageCard imgMediaIcon;

    public MediaCard(T item, LruCache<String, Bitmap> cache, Context context) {
        super(context);
        initialize(item, cache, context);
        update(item);
    }

    private void initialize(T item, LruCache<String, Bitmap> cache, Context context) {
        this.item = item;
        this.cache = cache;
        this.context = context;
        inflate(context, R.layout.item_media_grid, this);

        RippleUtil.setRippleBackground(this, R.color.FireBrick, R.color.White, context);
        txtMediaTitle = (TextView) findViewById(R.id.txt_media_title);
        imgMediaIcon = (SquareImageCard) findViewById(R.id.media_icon);
    }

    public void update(T item) {
        if (item.getImagePath() == null) {
            imgMediaIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.no_album_art));
        } else {
            BitmapUtil.startBitmapLoadTask(context, item, this, cache);
            txtMediaTitle.setText(item.getTitle());
        }
    }

    public ImageView getImageView() {
        return imgMediaIcon;
    }

    public void setImage(Bitmap bitmap) {
        if (bitmap == null) return;
        imgMediaIcon.setImageBitmap(bitmap);
    }
}