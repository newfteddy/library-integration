package ru.umeta.libraryintegration.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.stereotype.Repository;
import ru.umeta.libraryintegration.model.Document;
import ru.umeta.libraryintegration.model.EnrichedDocument;
import ru.umeta.libraryintegration.model.StringHash;

import javax.transaction.Transactional;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by k.kosolapov on 05/05/2015.
 */
@Transactional
@Repository
public class EnrichedDocumentDao  extends AbstractDao<EnrichedDocument> {

    public EnrichedDocumentDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @SuppressWarnings("unchecked")
    public List<EnrichedDocument> getNearDuplicates(Document document) {
        Criteria c = getEmptyCriteria();
        c = getNearDuplicateCriteria(document, c);

        return c.list();
    }

    private Criteria getNearDuplicateCriteria(Document document, Criteria c) {
        StringHash author = document.getAuthor();
        StringHash title = document.getTitle();

        Criterion authorEq1 = Restrictions.eq("author.hash_part_1", author.getHashPart1());
        Criterion authorEq2 = Restrictions.eq("author.hash_part_2", author.getHashPart2());
        Criterion authorEq3 = Restrictions.eq("author.hash_part_3", author.getHashPart3());
        Criterion authorEq4 = Restrictions.eq("author.hash_part_4", author.getHashPart4());

        Criterion titleEq1 = Restrictions.eq("title.hash_part_1", title.getHashPart1());
        Criterion titleEq2 = Restrictions.eq("title.hash_part_2", title.getHashPart2());
        Criterion titleEq3 = Restrictions.eq("title.hash_part_3", title.getHashPart3());
        Criterion titleEq4 = Restrictions.eq("title.hash_part_4", title.getHashPart4());

        c.add(Restrictions.and(
                Restrictions.or(
                        Restrictions.and(authorEq1, authorEq2),
                        Restrictions.and(authorEq1, authorEq3),
                        Restrictions.and(authorEq1, authorEq4),
                        Restrictions.and(authorEq2, authorEq3),
                        Restrictions.and(authorEq2, authorEq4),
                        Restrictions.and(authorEq3, authorEq4)
                ),
                Restrictions.or(
                        Restrictions.and(titleEq1, titleEq2),
                        Restrictions.and(titleEq1, titleEq3),
                        Restrictions.and(titleEq1, titleEq4),
                        Restrictions.and(titleEq2, titleEq3),
                        Restrictions.and(titleEq2, titleEq4),
                        Restrictions.and(titleEq3, titleEq4)
                )
        ));
        return c;
    }

    private Criteria getEmptyCriteria() {
        Criteria c = currentSession().createCriteria(EnrichedDocument.class, "enriched");
        c.createAlias("enriched.author", "author");
        c.createAlias("enriched.title", "title");
        return c;
    }

    public List<EnrichedDocument> getNearDuplicatesWithIsbn(Document document) {
        Criteria c = getEmptyCriteria();
        c.add(Restrictions.eq("enriched.isbn", document.getIsbn()));
        c = getNearDuplicateCriteria(document, c);
        return c.list();

    }

    public List<EnrichedDocument> getNearDuplicatesWithNullIsbn(Document document) {
        Criteria c = getEmptyCriteria();
        c.add(Restrictions.isNull("enriched.isbn"));
        c = getNearDuplicateCriteria(document, c);
        return c.list();

    }
}
