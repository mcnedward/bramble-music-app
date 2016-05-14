package com.mcnedward.bramble.view.grid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.utils.Extension;
import com.mcnedward.bramble.utils.PicassoUtil;
import com.mcnedward.bramble.utils.RippleUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URI;

/**
 * Created by Edward on 5/14/2016.
 */
public class MediaGridItem extends LinearLayout {
    final private static String TAG = "MediaGridItem";

    private Context context;
    private TextView txtMediaTitle;
    private ImageView imgMediaIcon;
    private ProgressBar progressBar;
    private Picasso picasso;

    public MediaGridItem(Context context) {
        super(context);
        this.context = context;
        inflate(context, R.layout.item_media_grid, this);

        RippleUtil.setRippleBackground(this, R.color.FireBrick, R.color.White, context);
        txtMediaTitle = (TextView) findViewById(R.id.txt_media_title);
        imgMediaIcon = (ImageView) findViewById(R.id.media_icon);
        progressBar = (ProgressBar) findViewById(R.id.media_icon_progress);

        picasso = PicassoUtil.getPiccasoInstance(context);
    }

    public void setMediaTitleText(String text) {
        txtMediaTitle.setText(text);
    }

    public void setMediaIconPath(final String path) {
        if (path != null) {
            File imageFile = new File(path);
            picasso.with(context).load(imageFile).placeholder(R.drawable.no_album_art).into(imgMediaIcon);
        } else {
            imgMediaIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.no_album_art));
        }
    }
}
