package com.mcnedward.bramble.exception;

/**
 * Created by Edward on 5/28/2016.
 */
public class EntityAlreadyExistsException extends Exception {
    public EntityAlreadyExistsException(long id) {
        super("An entity with the id " + id + " already exists.");
    }
}
