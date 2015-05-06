package ru.umeta.libraryintegration.dao;

import org.hibernate.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by k.kosolapov on 30.04.2015.
 */
@Transactional
public abstract class AbstractDao<T> implements IDao<T> {

    private Class genericType;

    private SessionFactory sessionFactory;

    protected AbstractDao(Class genericType) {
        this.genericType = genericType;
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = checkNotNull(sessionFactory);
    }

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public T save(T object) {
        getSession().persist(object);
        return object;
    }

    @Override
    public void delete(T object) {
        getSession().delete(object);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(final Long id) {
        return (T) getSession().get(genericType, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(genericType);
        return (List<T>) criteria.list();
    }

}
