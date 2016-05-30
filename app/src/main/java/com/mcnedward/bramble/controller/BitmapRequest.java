package com.mcnedward.bramble.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.mcnedward.bramble.entity.data.ArtistImage;
import com.mcnedward.bramble.listener.BitmapDownloadListener;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Edward on 5/28/2016.
 * The request for downloading artist images.
 */
public class BitmapRequest extends Request<byte[]> {
    private static final String TAG = BitmapRequest.class.getName();

    private ArtistImage mArtistImage;
    private BitmapDownloadListener<ArtistImage> mDownloadListener;
    private Response.Listener<BitmapResponse> mListener;

    public BitmapRequest(ArtistImage artistImage, BitmapDownloadListener<ArtistImage> downloadListener, Response.Listener<BitmapResponse> listener, Response.ErrorListener errorListener) throws
            UnsupportedEncodingException {
        super(Method.GET, artistImage.getMediaUrl(), errorListener);
        Log.d(TAG, String.format("Making request to: %s", artistImage.getMediaUrl()));
        mArtistImage = artistImage;
        mDownloadListener = downloadListener;
        mListener = listener;
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response.data, getCacheEntry());
    }

    @Override
    protected void deliverResponse(byte[] response) {
        if (response == null) return;
        Bitmap bitmap = null;
        try {
            InputStream stream = new ByteArrayInputStream(response);
            bitmap = BitmapFactory.decodeStream(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mListener.onResponse(new BitmapResponse(bitmap, mArtistImage, mDownloadListener));
    }
}
