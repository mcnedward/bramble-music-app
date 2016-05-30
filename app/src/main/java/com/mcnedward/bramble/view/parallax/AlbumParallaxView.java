package com.mcnedward.bramble.view.parallax;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.entity.data.ArtistImage;
import com.mcnedward.bramble.entity.media.Album;
import com.mcnedward.bramble.entity.media.Song;
import com.mcnedward.bramble.repository.media.SongRepository;
import com.mcnedward.bramble.service.MediaService;
import com.mcnedward.bramble.view.AlbumSongItem;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by edward on 23/12/15.
 *
 * A ParallaxView for displaying Album contents.
 */
public class AlbumParallaxView extends ParallaxView<Album> {
    final private static String TAG = "AlbumParallaxView";

    private Album mAlbum;
    private SongRepository mSongRepository;

    public AlbumParallaxView(Context context, Album album) {
        super(context);
        mAlbum = album;
        mSongRepository = new SongRepository(mContext);
        initialize();
    }

    @Override
    protected void afterAddingForegroundSpace(LinearLayout layout) {
        // Set the album and artist name
        LinearLayout albumTitleView = (LinearLayout) inflate(mContext, R.layout.view_album_title, null);
        TextView txtAlbumName = (TextView) albumTitleView.findViewById(R.id.album_title_title);
        txtAlbumName.setText(mAlbum.getAlbumName());
        TextView txtArtistName = (TextView) albumTitleView.findViewById(R.id.album_title_artist);
        txtArtistName.setText(mAlbum.getArtist());
        layout.addView(albumTitleView);

        // Set the list of songs
        List<Song> songs = mSongRepository.getSongsForAlbum(mAlbum.getId());
        mAlbum.setAlbumSongIds(songs);
        for (Song song : songs) {
            AlbumSongItem songItem = new AlbumSongItem(mAlbum, song, mContext);
            layout.addView(songItem);
            MediaService.attachMediaChangeListener(songItem);
        }
    }

    @Override
    protected void setupBackgroundContent(LinearLayout layout) {
    }

    @Override
    protected void loadBackgroundImage(ImageView imageView) {
        String albumArt = mAlbum.getAlbumArt();
        if (albumArt != null) {
            // Create the album art bitmap and scale it to fit properly and avoid over using memory
            File imageFile = new File(mAlbum.getAlbumArt());
            Picasso.with(mContext).load(imageFile).into(imageView);
        }
    }

    @Override
    protected void onGlobalLayoutChange() {

    }

    @Override
    public void notifyMediaGridChange(ArtistImage item) {

    }
}
