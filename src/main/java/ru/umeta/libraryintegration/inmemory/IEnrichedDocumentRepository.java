package ru.umeta.libraryintegration.inmemory;

import ru.umeta.libraryintegration.model.Document;
import ru.umeta.libraryintegration.model.EnrichedDocument;
import ru.umeta.libraryintegration.model.EnrichedDocumentLite;

import java.util.List;

/**
 * Created by ctash on 24.11.15.
 */
public interface IEnrichedDocumentRepository {

    List<EnrichedDocumentLite> getNearDuplicates(Document document);

    List<EnrichedDocumentLite> getNearDuplicates(EnrichedDocumentLite document);

    List<EnrichedDocumentLite> getNearDuplicatesWithIsbn(Document document);

    List<EnrichedDocumentLite> getNearDuplicatesWithNullIsbn(Document document);

    List<EnrichedDocumentLite> getNearDuplicatesWithPublishYear(Document document);

    void save(EnrichedDocument enrichedDocument);

    void update(EnrichedDocument enrichedDocument);

    EnrichedDocument getById(long id);
}
