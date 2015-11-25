package ru.umeta.libraryintegration.inmemory;

import org.springframework.stereotype.Repository;
import ru.umeta.libraryintegration.model.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by k.kosolapov on 11/18/2015.
 */
@Repository
public class DocumentRepository {

    private AtomicLong identity = new AtomicLong(0);

    private Map<Long, Document> map = new HashMap<>();

    public void save(Document document) {
        Long id = identity.getAndIncrement();
        map.put(id, document);
    }
}
