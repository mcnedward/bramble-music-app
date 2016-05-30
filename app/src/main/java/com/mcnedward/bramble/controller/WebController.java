package com.mcnedward.bramble.controller;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.mcnedward.bramble.entity.data.ArtistImage;
import com.mcnedward.bramble.entity.media.Artist;
import com.mcnedward.bramble.enums.CompressionFormat;
import com.mcnedward.bramble.exception.EntityAlreadyExistsException;
import com.mcnedward.bramble.exception.EntityDoesNotExistException;
import com.mcnedward.bramble.listener.BitmapDownloadListener;
import com.mcnedward.bramble.repository.data.ArtistImageRepository;
import com.mcnedward.bramble.utils.BitmapUtil;

/**
 * Created by Edward on 5/28/2016.
 * <p/>
 * A controller for making web requests.
 */
public class WebController {
    private static final String TAG = "WebController";

    private Context mContext;
    private RequestQueue mQueue = null;
    private ArtistImageRepository mArtistImageRepository;

    public WebController(Context context) {
        mContext = context;
        mArtistImageRepository = new ArtistImageRepository(context);
    }

    public void requestArtistImages(Artist artist, Response.Listener<ArtistImageResponse> listener) {
        setupQueue();

        ArtistImageRequest request;
        try {
            request = new ArtistImageRequest(artist, listener, getErrorListener());
            // Add the request to the RequestQueue.
            mQueue.add(request);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public void requestArtistImages(String query, Artist artist, Response.Listener<ArtistImageResponse> listener) {
        setupQueue();

        ArtistImageRequest request;
        try {
            request = new ArtistImageRequest(query, artist, listener, getErrorListener());
            // Add the request to the RequestQueue.
            mQueue.add(request);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public void downloadArtistImage(ArtistImage artistImage, BitmapDownloadListener<ArtistImage> listener) {
        setupQueue();

        BitmapRequest request;
        try {
            request = new BitmapRequest(artistImage, listener, new Response.Listener<BitmapResponse>() {
                @Override
                public void onResponse(BitmapResponse response) {
                    try {
                        response.artistImage.setBitmapBytes(BitmapUtil.toBtyes(response.bitmap, CompressionFormat.getCompressionFormat(response.artistImage.getContentType()).format));
                        mArtistImageRepository.update(response.artistImage);
                    } catch (EntityDoesNotExistException e) {
                        try {
                            mArtistImageRepository.save(response.artistImage);
                        } catch (EntityAlreadyExistsException e1) {
                            // This should never happen...
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Something went wrong when resizing the bitmap for: " + response.artistImage.getTitle(), e);
                    }
                    response.listener.onDownloadComplete(response.artistImage);
                }
            }, getErrorListener());
            // Add the request to the RequestQueue.
            mQueue.add(request);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Volley request failed: " + error.getMessage());
                stopQueue();
            }
        };
    }

    private void setupQueue() {
        // Instantiate the RequestQueue.
        if (mQueue == null) {
            try {
                mQueue = Volley.newRequestQueue(mContext);
                mQueue.start();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    private void stopQueue() {
        if (mQueue == null) return;
        mQueue.stop();
    }

}
