package ru.umeta.libraryintegration.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.umeta.libraryintegration.model.Document;

import javax.transaction.Transactional;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by k.kosolapov on 05/05/2015.
 */
@Transactional
@Repository
public class DocumentDao {

    private final SessionFactory sessionFactory;

    public DocumentDao(SessionFactory sessionFactory) {
        this.sessionFactory = checkNotNull(sessionFactory);
    }

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public Document save(Document object) {
        getSession().persist(object);
        return object;
    }

    public void delete(Document object) {
        getSession().delete(object);
    }

    @SuppressWarnings("unchecked")
    public Document get(final Long id) {
        return (Document) getSession().get(Document.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Document> getAll() {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Document.class);
        return (List<Document>) criteria.list();
    }
}
