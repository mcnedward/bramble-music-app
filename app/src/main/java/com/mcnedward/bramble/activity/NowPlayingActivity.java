package com.mcnedward.bramble.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.exception.MediaNotFoundException;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Song;
import com.mcnedward.bramble.service.MediaService;
import com.mcnedward.bramble.utils.Extension;
import com.mcnedward.bramble.utils.listener.AlbumLoadListener;
import com.mcnedward.bramble.utils.task.PlayMediaTask;

import java.io.File;

/**
 * Created by edward on 26/12/15.
 */
public class NowPlayingActivity extends Activity implements AlbumLoadListener {
    private final static String TAG = "NowPlayingActivity";

    private Song song;
    private Album album;

    private TextView txtPassed;
    private SeekBar seekBar;
    private TextView txtDuration;
    private ImageView btnPrevious;
    private ImageView btnPlay;
    private ImageView btnForward;

    private boolean loaded = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.now_playing_view);

        song = (Song) getIntent().getSerializableExtra("song");
        ((TextView) findViewById(R.id.now_playing_title)).setText(song.getTitle());

        MainActivity.mediaCache.registerAlbumLoadListener(this);
    }

    private void load() {
        try {
            album = MainActivity.mediaCache.getAlbumForSong(song);
        } catch (MediaNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        // Load title bar
        TextView txtSongTitle = (TextView) findViewById(R.id.now_playing_title);
        TextView txtAlbum = (TextView) findViewById(R.id.now_playing_album);
        txtSongTitle.setText(song.getTitle());
        txtAlbum.setText(album.getAlbumName());

        // Load album art
        String albumArt = album.getAlbumArt();
        if (albumArt != null) {
            // Create the album art bitmap and scale it to fit properly and avoid over using memory
            File imageFile = new File(album.getAlbumArt());
            Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            ((ImageView) findViewById(R.id.now_playing_album_art)).setImageBitmap(imageBitmap);
        }

        // Load controls
        txtPassed = (TextView) findViewById(R.id.now_playing_passed);
        txtDuration = (TextView) findViewById(R.id.now_playing_duration);
        seekBar = (SeekBar) findViewById(R.id.now_playing_seek);
        seekBar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(this, R.color.Red), PorterDuff.Mode.MULTIPLY));
        seekBar.getThumb().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(this, R.color.Red), PorterDuff.Mode.SRC_IN));
        txtPassed.setText("0:00");
        txtDuration.setText(song.getDuration());

        btnPrevious = (ImageView) findViewById(R.id.btn_previous);
        btnPlay = (ImageView) findViewById(R.id.btn_play);
        btnForward = (ImageView) findViewById(R.id.btn_forward);
        int rippleColor = R.color.FireBrick;
        int backgroundColor = 0;
        btnPrevious.setBackground(Extension.rippleDrawable(rippleColor, backgroundColor, this));
        btnPlay.setBackground(Extension.rippleDrawable(rippleColor, backgroundColor, this));
        btnForward.setBackground(Extension.rippleDrawable(rippleColor, backgroundColor, this));

        // Start playing music!
        Intent intent = new Intent(this, MediaService.class);
        intent.putExtra("song", song);
        startService(intent);

        loaded = true;
    }

    @Override
    public void notifyAlbumLoadReady() {
        if (!loaded)
            load();
    }

    public TextView getTxtPassed() {
        return txtPassed;
    }

    public SeekBar getSeekBar() {
        return seekBar;
    }

    public TextView getTxtDuration() {
        return txtDuration;
    }

    public ImageView getBtnPrevious() {
        return btnPrevious;
    }

    public ImageView getBtnPlay() {
        return btnPlay;
    }

    public ImageView getBtnForward() {
        return btnForward;
    }
}
