package com.mcnedward.bramble.controller;

import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.mcnedward.bramble.entity.media.Artist;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Edward on 5/28/2016.
 * The request for getting artist images from Bing.
 */
public class ArtistImageRequest extends Request<ArtistImageResponse> {
    private static final String TAG = ArtistImageRequest.class.getName();

    private static final String COUNT = "6";
    private static final String APP_ID = "zNw3wW02HmL+aOcxsVkmjDuA/Pu8IdKaJ6C1tOL1eS8";
    private static final String URL = "https://api.datamarket.azure" +
            ".com/Bing/Search/v1/Image?Market=%27en-US%27&Adult=%27Strict%27&%24format=JSON&%24top=" + COUNT + "&Query=";

    private Artist mArtist;
    private Response.Listener<ArtistImageResponse> mListener;
    private Gson mGson = new Gson();

    public ArtistImageRequest(Artist artist, Response.Listener<ArtistImageResponse> listener, Response.ErrorListener errorListener) throws
            UnsupportedEncodingException {
        super(Method.GET, URL + "%27" + URLEncoder.encode(artist.getArtistName(), "UTF-8") + "%27", errorListener);
        Log.d(TAG, String.format("Making request to: %s", URL + "%27" + URLEncoder.encode(artist.getArtistName(), "UTF-8") + "%27"));
        mArtist = artist;
        mGson = new Gson();
        mListener = listener;
    }

    @Override
    protected Response<ArtistImageResponse> parseNetworkResponse(NetworkResponse response) {
        String jsonString = new String(response.data);
        try {
            ArtistImageResponse result = mGson.fromJson(jsonString, ArtistImageResponse.class);
            result.setArtist(mArtist);
            return Response.success(result, getCacheEntry());
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void deliverResponse(ArtistImageResponse response) {
        mListener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        String accountKeyEnc = Base64.encodeToString((APP_ID + ":" + APP_ID).getBytes(), Base64.NO_WRAP);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic " + accountKeyEnc);
        return headers;
    }
}
