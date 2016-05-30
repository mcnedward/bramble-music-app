package com.mcnedward.bramble.adapter.grid;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.entity.data.ArtistImage;
import com.mcnedward.bramble.enums.CardType;
import com.mcnedward.bramble.exception.EntityDoesNotExistException;
import com.mcnedward.bramble.listener.MediaGridChangeListener;
import com.mcnedward.bramble.repository.data.ArtistImageRepository;
import com.mcnedward.bramble.utils.PicassoUtil;
import com.mcnedward.bramble.view.card.MediaCard;
import com.squareup.picasso.Callback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 5/14/2016.
 */
public class ArtistImageGridAdapter extends MediaGridAdapter<ArtistImage> {
    private static final String TAG = "ArtistImageGridAdapter";

    private static List<MediaGridChangeListener<ArtistImage>> mListeners;
    private ArtistImageRepository mRepository;

    public ArtistImageGridAdapter(Context context, List<ArtistImage> groups) {
        super(context, groups, CardType.BIG_RECT);
        mRepository = new ArtistImageRepository(context);
    }

    @Override
    protected void doOnClickAction(ArtistImage media, View view) {
        media.setSelectedImage(true);
        boolean updated = mRepository.setSelectedImage(media, media.getArtistId());
        if (updated) {
            List<ArtistImage> artistImages = mRepository.getForArtistId(media.getArtistId());
            setGroups(artistImages);
            notifyListeners(media);
        }
    }

    @Override
    protected void updateMediaCard(ArtistImage item, MediaCard mediaCard) {
        String imageUrl = item.getMediaUrl();
        if (imageUrl == null || !imageUrl.equals("")) {
            mediaCard.update(item, false);

            int width = mediaCard.getImageView().getMeasuredWidth();
            int height = mediaCard.getImageView().getMeasuredHeight();
            PicassoUtil.getPicasso(mContext).with(mContext).load(imageUrl).placeholder(R.drawable.no_album_art).resize(width, height).centerCrop().into(mediaCard.getImageView(), getCallback());
        } else {
            mediaCard.update(item);
        }
    }

    private Callback getCallback() {
        return new Callback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Success");
            }

            @Override
            public void onError() {
                Log.d(TAG, "Error");
            }
        };
    }

    public static void registerListener(MediaGridChangeListener<ArtistImage> listener) {
        if (mListeners == null) {
            mListeners = new ArrayList<>();
        }
        mListeners.add(listener);
    }

    public static void unregisterListener(MediaGridChangeListener<ArtistImage> listener) {
        if (mListeners == null) {
            mListeners = new ArrayList<>();
            return;
        }
        if (mListeners.contains(listener)) {
            mListeners.remove(listener);
        }
    }

    private static void notifyListeners(ArtistImage item) {
        if (mListeners == null) return;
        for (MediaGridChangeListener<ArtistImage> listener : mListeners) {
            listener.notifyMediaGridChange(item);
        }
    }

}
