package com.mcnedward.bramble.repository.data;

import com.mcnedward.bramble.entity.data.ArtistImage;
import com.mcnedward.bramble.exception.EntityDoesNotExistException;

/**
 * Created by Edward on 5/28/2016.
 */
public interface IArtistImageRepository extends IDataRepository<ArtistImage> {

    ArtistImage getForArtistId(int artistId) throws EntityDoesNotExistException;

}
