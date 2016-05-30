package com.mcnedward.bramble.view.card;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.entity.ITitleAndImage;
import com.mcnedward.bramble.enums.CardType;
import com.mcnedward.bramble.utils.BitmapUtil;
import com.mcnedward.bramble.utils.PicassoUtil;
import com.mcnedward.bramble.utils.RippleUtil;
import com.squareup.picasso.Callback;

/**
 * Created by Edward on 5/14/2016.
 * A card-shaped view for displaying an image and information. This can be either a square shape, or a rectangle shape.
 */
public class MediaCard<T extends ITitleAndImage> extends LinearLayout {
    final private static String TAG = "MediaCard";

    private T mItem;
    private LruCache<String, Bitmap> mCache;
    private Context mContext;
    private TextView mTxtMediaTitle;
    private ImageView mImgMediaImage;
    private ImageView mImgSelected;
    private CardType mCardType;

    public MediaCard(Context context, T item, LruCache<String, Bitmap> cache, CardType cardType) {
        super(context);
        initialize(context, item, cache, cardType);
        update(item);
    }

    private void initialize(Context context, T item, LruCache<String, Bitmap> cache, CardType cardType) {
        mItem = item;
        mCache = cache;
        mContext = context;
        mCardType = cardType;
        inflate(context, R.layout.item_media_card, this);

        RippleUtil.setRippleBackground(this, R.color.FireBrick, R.color.White, context);
        mTxtMediaTitle = (TextView) findViewById(R.id.txt_media_title);
        switch (mCardType) {
            case SQUARE:
                mImgMediaImage = new SquareImageCard(context);
                break;
            case RECT:
                mImgMediaImage = new RectImageCard(context);
                break;
            case BIG_RECT:
                mImgMediaImage = new BigRectImageCard(context);
                break;
            default:
                mImgMediaImage = new RectImageCard(context);
                break;
        }
        mImgMediaImage.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mImgMediaImage.setAdjustViewBounds(true);
        mImgMediaImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mImgMediaImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.no_album_art));
        ((RelativeLayout) findViewById(R.id.container_media_image)).addView(mImgMediaImage);

        mImgSelected = (ImageView) findViewById(R.id.image_selected);
    }

    public void update(T item) {
        mItem = item;
        mTxtMediaTitle.setText(item.getTitle());
        mImgSelected.setVisibility(item.isSelected() ? VISIBLE : GONE);
        Bitmap bitmap = item.getBitmap();
        String path = item.getImagePath();
        if (bitmap != null || (path != null && !path.equals(""))) {
            // Load the bitmap
            BitmapUtil.startBitmapLoadTask(mContext, item, this, mCache);
        } else if (item.getImageUrl() != null && !item.getImageUrl().equals("")) {
            PicassoUtil.getPicasso(mContext).load(item.getImageUrl()).placeholder(R.drawable.no_album_art).fit().centerCrop().into(mImgMediaImage, getCallback());
        } else {
            // No usuable image path, so set the default
            mImgMediaImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.no_album_art));
        }
    }

    private Callback getCallback() {
        return new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError() {
                Log.d(TAG, "Error");
            }
        };
    }

    public void updateAfterBitmapLoadTaskFinished(T item, Bitmap bitmap) {
        if (item.getCacheKey().equals(mItem.getCacheKey())) {
            mTxtMediaTitle.setText(item.getTitle());
            setImage(bitmap);
        }
        mItem = item;
    }

    public ImageView getImageView() {
        return mImgMediaImage;
    }

    public void setImage(Bitmap bitmap) {
        if (bitmap == null) {
            mImgMediaImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.no_album_art));
        } else {
            mImgMediaImage.setImageBitmap(bitmap);
        }
    }
}