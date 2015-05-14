package ru.umeta.libraryintegration.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.umeta.libraryintegration.model.Protocol;

import javax.transaction.Transactional;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by k.kosolapov on 05/05/2015.
 */
@Transactional
@Repository
public class ProtocolDao{

    private final SessionFactory sessionFactory;

    public ProtocolDao(SessionFactory sessionFactory) {
        this.sessionFactory = checkNotNull(sessionFactory);
    }

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public Protocol save(Protocol object) {
        getSession().persist(object);
        return object;
    }

    public void delete(Protocol object) {
        getSession().delete(object);
    }

    @SuppressWarnings("unchecked")
    public Protocol get(final Long id) {
        return (Protocol) getSession().get(Protocol.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Protocol> getAll() {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Protocol.class);
        return (List<Protocol>) criteria.list();
    }
}
