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
public class ProtocolDao extends AbstractDao<Protocol> {

    public ProtocolDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
