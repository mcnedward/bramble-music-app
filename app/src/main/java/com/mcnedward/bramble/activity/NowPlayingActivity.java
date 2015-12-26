package com.mcnedward.bramble.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.exception.MediaNotFoundException;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Song;
import com.mcnedward.bramble.utils.listener.AlbumLoadListener;

import java.io.File;

/**
 * Created by edward on 26/12/15.
 */
public class NowPlayingActivity extends Activity implements AlbumLoadListener {
    private final static String TAG = "NowPlayingActivity";

    private Song song;
    private Album album;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.now_playing_view);

        song = (Song) getIntent().getSerializableExtra("song");
        ((TextView) findViewById(R.id.now_playing_title)).setText(song.getTitle());

        MainActivity.mediaService.registerAlbumLoadListener(this);
    }

    private void load() {
        try {
            album = MainActivity.mediaService.getAlbumForSong(song);
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
        TextView txtPassed = (TextView) findViewById(R.id.now_playing_passed);
        TextView txtDuration = (TextView) findViewById(R.id.now_playing_duration);
        SeekBar seekBar = (SeekBar) findViewById(R.id.now_playing_seek);
        txtPassed.setText("0:00");
        txtDuration.setText(song.getDuration());
    }

    @Override
    public void notifyAlbumLoadReady() {
        load();
    }
}
