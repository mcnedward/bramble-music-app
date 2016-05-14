package com.mcnedward.bramble.view.grid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.RippleDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.utils.Extension;
import com.mcnedward.bramble.utils.RippleUtil;

import java.io.File;

/**
 * Created by Edward on 5/14/2016.
 */
public class MediaCard extends MediaGridItem {
    final private static String TAG = "MediaCard";

    private Context context;
    private TextView txtMediaTitle;
    private ImageView imgMediaIcon;

    public MediaCard(Context context) {
        super(context);
        this.context = context;
        inflate(context, R.layout.item_media_grid, this);

        RippleUtil.setRippleBackground(this, R.color.FireBrick, R.color.White, context);
        txtMediaTitle = (TextView) findViewById(R.id.txt_media_title);
        imgMediaIcon = (ImageView) findViewById(R.id.media_icon);
    }

    public void setMediaTitleText(String text) {
        txtMediaTitle.setText(text);
    }

    public void setMediaIconPath(String path) {
        if (path != null) {
            // Create the media art bitmap and scale it to fit properly and avoid over using memory
            File imageFile = new File(path);
            Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            imgMediaIcon.setImageBitmap(imageBitmap);
        }
    }

    public ImageView getImageView() {
        return imgMediaIcon;
    }

    public void setImage(Bitmap bitmap) {
        if (bitmap == null) return;
        BitmapDrawable bd = new BitmapDrawable(context.getResources(), bitmap);
        RippleDrawable drawable = RippleUtil.getRippleDrawable(context, bd);
        imgMediaIcon.setImageDrawable(drawable);
    }
}