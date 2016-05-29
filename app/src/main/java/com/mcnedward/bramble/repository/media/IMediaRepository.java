package com.mcnedward.bramble.repository.media;

import com.mcnedward.bramble.exception.EntityDoesNotExistException;
import com.mcnedward.bramble.entity.media.Media;
import com.mcnedward.bramble.entity.media.MediaType;
import com.mcnedward.bramble.repository.IRepository;

/**
 * Created by Edward on 5/28/2016.
 */
public interface IMediaRepository<T extends Media> extends IRepository<T> {

    MediaType getMediaType();

    T get(String key) throws EntityDoesNotExistException;
}
