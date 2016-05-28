package com.mcnedward.bramble.exception;

/**
 * Created by Edward on 5/28/2016.
 */
public class EntityDoesNotExistException extends Exception {
    public EntityDoesNotExistException(String message) {
        super(message);
    }

    public EntityDoesNotExistException(long id) {
        super("Entity with the id " + id + " does not exist.");
    }
}
