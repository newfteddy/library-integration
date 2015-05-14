package ru.umeta.libraryintegration.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.umeta.libraryintegration.model.StringHash;

import javax.transaction.Transactional;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by k.kosolapov on 05/05/2015.
 */
@Transactional
@Repository
public class StringHashDao{

    private final SessionFactory sessionFactory;

    public StringHashDao(SessionFactory sessionFactory) {
        this.sessionFactory = checkNotNull(sessionFactory);
    }

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public StringHash save(StringHash object) {
        getSession().persist(object);
        return object;
    }

    public void delete(StringHash object) {
        getSession().delete(object);
    }

    @SuppressWarnings("unchecked")
    public StringHash get(final Long id) {
        return (StringHash) getSession().get(StringHash.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<StringHash> getAll() {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(StringHash.class);
        return (List<StringHash>) criteria.list();
    }
}
