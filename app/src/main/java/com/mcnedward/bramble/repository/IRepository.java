package com.mcnedward.bramble.repository;

import com.mcnedward.bramble.exception.EntityDoesNotExistException;

import java.util.List;

/**
 * Created by Edward on 5/16/2016.
 */
public interface IRepository<T> {

    /**
     * Queries the based on the options passed in.
     *
     * @param whereClause The filter of which rows to return (WHERE clause), with arguments passed in as "?"
     * @param whereArgs   The arguments for the WHERE clause
     * @param sortOrder     The order of the results to return
     * @return A list of data
     */
    List<T> read(String whereClause, String[] whereArgs, String sortOrder);

    List<T> read(String query, String... params);

    T readFirstOrDefault(String whereClause, String[] whereArgs) throws EntityDoesNotExistException;

    T readFirstOrDefault(String whereClause, String[] whereArgs, String orderBy) throws EntityDoesNotExistException;

    T get(long id) throws EntityDoesNotExistException;

    List<T> getAll();

}
