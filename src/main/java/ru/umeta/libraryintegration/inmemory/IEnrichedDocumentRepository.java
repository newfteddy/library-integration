package ru.umeta.libraryintegration.inmemory;

import ru.umeta.libraryintegration.model.Document;
import ru.umeta.libraryintegration.model.EnrichedDocument;

import java.util.List;

/**
 * Created by ctash on 24.11.15.
 */
public interface IEnrichedDocumentRepository {

    List<EnrichedDocument> getNearDuplicates(Document document);

    List<EnrichedDocument> getNearDuplicatesWithIsbn(Document document);

    List<EnrichedDocument> getNearDuplicatesWithNullIsbn(Document document);

    List<EnrichedDocument> getNearDuplicatesWithPublishYear(Document document);

    List<EnrichedDocument> getNearDuplicatesWithIsbnAndPublishYear(Document document);

    Number save(EnrichedDocument enrichedDocument);
}
