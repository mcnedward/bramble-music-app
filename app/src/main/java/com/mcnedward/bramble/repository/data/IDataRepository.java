package com.mcnedward.bramble.repository.data;

import com.mcnedward.bramble.exception.EntityAlreadyExistsException;
import com.mcnedward.bramble.exception.EntityDoesNotExistException;
import com.mcnedward.bramble.media.Data;
import com.mcnedward.bramble.repository.IRepository;

import java.util.List;

/**
 * Created by Edward on 5/28/2016.
 */
public interface IDataRepository<T extends Data> extends IRepository<T> {

    /**
     * Save an entity in the database.
     *
     * @param entity The entity to save to the database.
     * @return True if the entity was saved, false otherwise.
     * @throws EntityAlreadyExistsException If the entity already exists in the database.
     */
    T save(T entity) throws EntityAlreadyExistsException;

    /**
     * Update an existing entity.
     *
     * @param entity The entity to update.
     * @return True if the entity was updated, false otherwise
     * @throws EntityDoesNotExistException If the entity does not exist.
     */
    boolean update(T entity) throws EntityDoesNotExistException;

    /**
     * Delete an existing entity.
     *
     * @param entity The entity to delete.
     * @return True if the entity was deleted, false otherwise.
     * @throws EntityDoesNotExistException If the entity does not exist.
     */
    boolean delete(T entity) throws EntityDoesNotExistException;

    /**
     * Delete an existing entity.
     *
     * @param id The id of the entity to delete.
     * @return True if the entity was deleted, false otherwise.
     * @throws EntityDoesNotExistException If the entity does not exist.
     */
    boolean delete(long id) throws EntityDoesNotExistException;

}
