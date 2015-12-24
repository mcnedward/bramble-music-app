package com.mcnedward.bramble.utils.task;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.media.Album;
import com.mcnedward.bramble.media.Artist;
import com.mcnedward.bramble.utils.MediaType;
import com.mcnedward.bramble.utils.loader.MediaLoader;
import com.mcnedward.bramble.utils.refresh.Refresher;

import java.util.List;

/**
 * AsyncTask used to retrieve media information from the Media Store. This should be called as soon as the application
 * is created, because this will look through all media files on the user's phone, and then retrieve the information for
 * those files. This Task is very time consuming.
 *
 * Created by edward on 23/12/15.
 */
public class RetrieveMediaTask extends AsyncTask<Void, Integer, Void> {
    final private static String TAG = "RetrieveMediaTask";

    private Context context;
    private MediaLoader mediaLoader;
    private Refresher refresher;

    private List<Artist> artists;
    private List<Album> albums;

    /**
     * Constructor for this AsyncTask. Creates a MediaAdapter to retrieve information from the Media Store, a
     * MusicDatabase to get data after the media information has been retrieved, and a Refresh adapter to refresh the
     * lists views with the new media data.
     *
     * @param context
     *            The context of the activity to run this AsyncTask.
     */
    public RetrieveMediaTask(Context context) {
        this.context = context;
        mediaLoader = new MediaLoader(context);
        refresher = new Refresher(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ((Activity) context).setProgressBarIndeterminateVisibility(true);	// Show the progress spinner
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.i(TAG, "Starting Retrieve Media Task!");
        try {
            artists = mediaLoader.getArtists();
            albums = mediaLoader.getAlbums();
        } catch (Exception e) {
            // TODO Definitely need better error handling
            Log.d(TAG, e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        Log.i(TAG, "Task successfully executed");
        ((Activity) context).setProgressBarIndeterminateVisibility(false);	// Remove the progress spinner

        refresher.refresh(artists, MediaType.ARTIST);
        refresher.refresh(albums, MediaType.ALBUM);
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        Log.i(TAG, "Progress: " + progress[0] + "%");
    }
}
