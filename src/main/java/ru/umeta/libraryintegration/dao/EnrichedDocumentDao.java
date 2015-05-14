package ru.umeta.libraryintegration.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.umeta.libraryintegration.model.EnrichedDocument;

import javax.transaction.Transactional;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by k.kosolapov on 05/05/2015.
 */
@Transactional
@Repository
public class EnrichedDocumentDao {

    private final SessionFactory sessionFactory;

    public EnrichedDocumentDao(SessionFactory sessionFactory) {
        this.sessionFactory = checkNotNull(sessionFactory);
    }

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public EnrichedDocument save(EnrichedDocument object) {
        getSession().persist(object);
        return object;
    }

    public void delete(EnrichedDocument object) {
        getSession().delete(object);
    }

    @SuppressWarnings("unchecked")
    public EnrichedDocument get(final Long id) {
        return (EnrichedDocument) getSession().get(EnrichedDocument.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<EnrichedDocument> getAll() {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EnrichedDocument.class);
        return (List<EnrichedDocument>) criteria.list();
    }
}
