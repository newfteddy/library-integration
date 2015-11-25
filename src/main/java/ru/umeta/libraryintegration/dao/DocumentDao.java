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
public class DocumentDao extends AbstractDao<Document> {

    public DocumentDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

}
