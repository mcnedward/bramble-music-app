package com.mcnedward.bramble.adapter.grid;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import com.mcnedward.bramble.R;
import com.mcnedward.bramble.controller.WebController;
import com.mcnedward.bramble.entity.data.ArtistImage;
import com.mcnedward.bramble.enums.CardType;
import com.mcnedward.bramble.exception.EntityDoesNotExistException;
import com.mcnedward.bramble.listener.BitmapDownloadListener;
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
    private WebController mController;

    public ArtistImageGridAdapter(Context context, List<ArtistImage> groups) {
        super(context, groups, CardType.BIG_RECT);
        mRepository = new ArtistImageRepository(context);
        mController = new WebController(context);
    }

    @Override
    protected void doOnClickAction(ArtistImage media, MediaCard view) {
        media = mRepository.setSelectedImage(media);
        mController.downloadArtistImage(media, new BitmapDownloadListener<ArtistImage>() {
            @Override
            public void onDownloadComplete(ArtistImage item) {
                List<ArtistImage> artistImages = mRepository.getForArtistId(item.getArtistId());
                setGroups(artistImages);
                notifyListeners(item);
            }
        });
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
