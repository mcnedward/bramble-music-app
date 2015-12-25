package com.mcnedward.bramble.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.activity.AlbumActivity;
import com.mcnedward.bramble.media.Album;

import java.io.File;

/**
 * Created by edward on 23/12/15.
 */
public class AlbumPopupItem extends LinearLayout {
    final private static String TAG = "AlbumPopupItem";

    private Context context;
    private TextView txtAlbumName;
    private ImageView imgAlbumArt;

    public AlbumPopupItem(final Album album, Context context) {
        super(context);
        this.context = context;
        inflate(context, R.layout.album_popup_item, this);

        txtAlbumName = (TextView) findViewById(R.id.album_name);
        setAlbumName(album.getAlbumName());

        imgAlbumArt = (ImageView) findViewById(R.id.album_art);
        String albumArt = album.getAlbumArt();
        if (albumArt != null) {
            // Create the album art bitmap and scale it to fit properly and avoid over using memory
            File imageFile = new File(album.getAlbumArt());
            Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            imgAlbumArt.setImageBitmap(imageBitmap);
        }

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startAlbumActivity(album);
            }
        });
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = (int) (dm.widthPixels * 0.75);
        int height = (int) (dm.heightPixels * 0.6);

        ((Activity) context).getWindow().setLayout(width, height);
    }

    private void startAlbumActivity(Album album) {
        Log.d(TAG, "Starting AlbumActivity for " + album + "!");
        Intent intent = new Intent(context, AlbumActivity.class);
        intent.putExtra("album", album);
        context.startActivity(intent);
    }

    public void setAlbumName(String name) {
        txtAlbumName.setText(name);
    }

}
