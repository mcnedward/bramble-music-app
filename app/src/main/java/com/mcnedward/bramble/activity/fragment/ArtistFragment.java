package com.mcnedward.bramble.activity.fragment;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;

import com.android.volley.Response;
import com.mcnedward.bramble.adapter.grid.ArtistGridAdapter;
import com.mcnedward.bramble.adapter.grid.ArtistImageGridAdapter;
import com.mcnedward.bramble.adapter.grid.MediaGridAdapter;
import com.mcnedward.bramble.adapter.list.ArtistListAdapter;
import com.mcnedward.bramble.adapter.list.MediaListAdapter;
import com.mcnedward.bramble.controller.ArtistImageResponse;
import com.mcnedward.bramble.controller.WebController;
import com.mcnedward.bramble.entity.data.ArtistImage;
import com.mcnedward.bramble.entity.media.Artist;
import com.mcnedward.bramble.entity.media.MediaType;
import com.mcnedward.bramble.exception.EntityAlreadyExistsException;
import com.mcnedward.bramble.exception.EntityDoesNotExistException;
import com.mcnedward.bramble.listener.MediaGridChangeListener;
import com.mcnedward.bramble.repository.data.ArtistImageRepository;
import com.mcnedward.bramble.repository.data.IArtistImageRepository;
import com.mcnedward.bramble.repository.media.ArtistRepository;
import com.mcnedward.bramble.repository.media.IMediaRepository;

import java.util.List;
import java.util.Random;

/**
 * Created by edward on 24/12/15.
 */
public class ArtistFragment extends MediaFragment<Artist> implements MediaGridChangeListener<ArtistImage> {
    private final static String TAG = "ArtistFragment";
    private final static int LOADER_ID = new Random().nextInt();

    private WebController mController;
    private IArtistImageRepository mRepository;

    public ArtistFragment() {
        super(MediaType.ARTIST);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mController = new WebController(getActivity());
        mRepository = new ArtistImageRepository(getActivity());

        // Register this as a listener for ArtistImage choosing changes
        ArtistImageGridAdapter.registerListener(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Artist>> loader, List<Artist> data) {
        super.onLoadFinished(loader, data);

        for (Artist artist : data) {
            List<ArtistImage> artistImages;
            artistImages = mRepository.getForArtistId(artist.getId());
            if (!artistImages.isEmpty()) {
                artist.setArtistImages(artistImages);
                mGridAdapter.updateItem(artist);
                mGridAdapter.notifyDataSetChanged();
            } else {
                // If no image, then make a request for one
                mController.requestArtistImages(artist,
                        new Response.Listener<ArtistImageResponse>() {
                            @Override
                            public void onResponse(ArtistImageResponse response) {
                                Artist requestArtist = response.getArtist();
                                List<ArtistImage> artistImages = response.getArtistImages();
                                if (artistImages.isEmpty()) {
                                    Log.d(TAG, "No images found for artist: " + requestArtist.getArtistName());
                                } else {
                                    // Get the artist images from the response, save them in the database, and handle the bitmap for the first one
                                    for (int i = 0; i < artistImages.size(); i++) {
                                        ArtistImage ai = artistImages.get(i);
                                        if (i == 0) ai.setSelectedImage(true);  // Set the first as the default selected image
                                        ai.setArtistId(requestArtist.getId());
                                        try {
                                            mRepository.save(ai);
                                        } catch (EntityAlreadyExistsException e1) {
                                            Log.w(TAG, e1.getMessage());
                                        }
                                    }
                                    requestArtist.setArtistImages(artistImages);
                                    mGridAdapter.updateItem(requestArtist);
                                }
                            }
                        });
            }
        }
    }


    @Override
    public IMediaRepository<Artist> createRepository() {
        return new ArtistRepository(getActivity());
    }

    @Override
    public MediaListAdapter<Artist> createMediaListAdapter() {
        return new ArtistListAdapter(getActivity());
    }

    @Override
    protected MediaGridAdapter<Artist> createMediaGridAdapter() {
        return new ArtistGridAdapter(getActivity());
    }

    @Override
    protected int getLoaderId() {
        return LOADER_ID;
    }

    @Override
    public void notifyMediaGridChange(ArtistImage item) {
        Artist artist = mGridAdapter.getItemWithId(item.getArtistId());
        if (artist == null) return;
        artist.setSelectedImage(item);
        mGridAdapter.updateItem(artist);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ArtistImageGridAdapter.unregisterListener(this);
    }
}
