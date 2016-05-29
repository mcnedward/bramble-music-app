package com.mcnedward.bramble.controller;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.mcnedward.bramble.entity.media.Artist;

/**
 * Created by Edward on 5/28/2016.
 *
 * A controller for making web requests.
 */
public class WebController {
    private static final String TAG = "WebController";

    private Context mContext;
    private RequestQueue mQueue = null;

    public WebController(Context context) {
        mContext = context;
    }

    public void requestArtistImage(Artist artist, Response.Listener<ArtistImageResponse> listener) {
        // Instantiate the RequestQueue.
        if (mQueue == null) {
            try {
                mQueue = Volley.newRequestQueue(mContext);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                return;
            }
        }

        ArtistImageRequest request;
        try {
            request = new ArtistImageRequest(artist, listener, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Volley request failed: " + error.getMessage());
                }
            });
            // Add the request to the RequestQueue.
            mQueue.add(request);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return;
        }
    }

}
