package com.mcnedward.bramble.repository.media;

import android.content.Context;

import com.mcnedward.bramble.exception.EntityDoesNotExistException;
import com.mcnedward.bramble.entity.media.Media;
import com.mcnedward.bramble.entity.media.MediaType;
import com.mcnedward.bramble.repository.Repository;

/**
 * Created by Edward on 5/28/2016.
 */
public abstract class MediaRepository<T extends Media> extends Repository<T> implements IMediaRepository<T> {
    private static final String TAG = "MediaRepository";

    protected MediaType mMediaType;

    public MediaRepository(Context context, MediaType mediaType) {
        super(context);
        mMediaType = mediaType;
    }

    @Override
    public T get(String key) throws EntityDoesNotExistException {
        String whereClause = String.format("%s = ?", getColumns()[1]);
        return readFirstOrDefault(whereClause, new String[]{key});
    }

    @Override
    public MediaType getMediaType() {
        return mMediaType;
    }
}
