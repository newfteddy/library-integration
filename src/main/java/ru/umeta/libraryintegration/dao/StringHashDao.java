package ru.umeta.libraryintegration.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.umeta.libraryintegration.model.StringHash;

import javax.transaction.Transactional;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by k.kosolapov on 05/05/2015.
 */
@Repository
public class StringHashDao extends AbstractDao<StringHash> {

    public StringHashDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

}
