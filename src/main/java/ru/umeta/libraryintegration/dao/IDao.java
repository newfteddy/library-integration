package ru.umeta.libraryintegration.dao;

import java.util.List;

/**
 * Created by k.kosolapov on 05/05/2015.
 */
public interface IDao<T> {
    /**
     * Persists the object in the database.
     *
     * @param object
     * @return the same object
     */
    T save(T object);

    /**
     * Deletes the object in the database
     *
     * @param object
     */
    void delete(T object);

    /**
     * Gets the object with selected id from the database
     *
     * @param id
     * @return the object
     */
    @SuppressWarnings("unchecked")
    T get(Long id);

    /**
     * Gets all the objects of the current class from the databse
     *
     * @return a list of objects
     */
    @SuppressWarnings("unchecked")
    List<T> getAll();
}
