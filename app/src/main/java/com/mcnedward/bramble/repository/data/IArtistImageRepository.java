package com.mcnedward.bramble.repository.data;

import com.mcnedward.bramble.entity.data.ArtistImage;
import com.mcnedward.bramble.exception.EntityDoesNotExistException;

import java.util.List;

/**
 * Created by Edward on 5/28/2016.
 */
public interface IArtistImageRepository extends IDataRepository<ArtistImage> {

    List<ArtistImage> getForArtistId(long artistId);

    ArtistImage getSelectedImageForArtist(long artistId) throws  EntityDoesNotExistException;

    ArtistImage setSelectedImage(ArtistImage newSelectedArtistImage);
}
