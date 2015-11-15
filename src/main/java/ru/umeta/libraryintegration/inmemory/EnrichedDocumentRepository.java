package ru.umeta.libraryintegration.inmemory;

import org.springframework.stereotype.Repository;
import ru.umeta.libraryintegration.model.EnrichedDocument;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Kirill Kosolapov (https://github.com/c-tash) on 12.11.2015.
 */
@Repository
public class EnrichedDocumentRepository {

    private AtomicLong identity = new AtomicLong(0);

    ConcurrentMap<Long, EnrichedDocument> mapId = new ConcurrentHashMap<>();


}
