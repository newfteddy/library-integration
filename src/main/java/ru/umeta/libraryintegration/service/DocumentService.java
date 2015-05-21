package ru.umeta.libraryintegration.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.umeta.libraryintegration.dao.DocumentDao;
import ru.umeta.libraryintegration.dao.EnrichedDocumentDao;
import ru.umeta.libraryintegration.json.ModsParseResult;
import ru.umeta.libraryintegration.json.ParseResult;
import ru.umeta.libraryintegration.model.Document;
import ru.umeta.libraryintegration.model.EnrichedDocument;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by ctash on 28.04.2015.
 */
public class DocumentService {

    private static final String DEFAULT_PROTOCOL = "Z39.50";

    @Autowired
    private StringHashService stringHashService;

    @Autowired
    private ProtocolService protocolService;

    @Autowired
    private EnrichedDocumentDao enrichedDocumentDao;

    @Autowired
    private DocumentDao documentDao;

    public void processDocumentList(List<ParseResult> resultList, String protocolName) {
        for (ParseResult parseResult : checkNotNull(resultList)) {
            if (parseResult instanceof ModsParseResult) {
                Document document = new Document();
                ModsParseResult modsParseResult = (ModsParseResult) parseResult;

                document.setAuthor(stringHashService.getFromRepository(modsParseResult.getAuthor()));
                document.setTitle(stringHashService.getFromRepository(modsParseResult.getTitle()));
                document.setCreationTime(new Date());
                document.setIsbn(modsParseResult.getIsbn());
                document.setProtocol(protocolService.getFromRepository(protocolName == null ? DEFAULT_PROTOCOL : protocolName));
                document.setXml(modsParseResult.getModsDefinition().xmlText());

                EnrichedDocument enrichedDocument = findEnrichedDocument(document);
                if (enrichedDocument != null) {
                    mergeDocuments(modsParseResult, enrichedDocument);
                } else {
                    enrichedDocument = new EnrichedDocument();
                    enrichedDocument.setAuthor(document.getAuthor());
                    enrichedDocument.setTitle(document.getTitle());
                    enrichedDocument.setIsbn(document.getIsbn());
                    enrichedDocument.setXml(document.getXml());
                    enrichedDocument.setCreationTime(document.getCreationTime());
                    enrichedDocument.setId(enrichedDocumentDao.save(enrichedDocument).longValue());
                    document.setDistance(1.);
                }
                document.setEnrichedDocument(enrichedDocument);
                documentDao.save(document);
            }
        }
    }

    private void mergeDocuments(ModsParseResult modsParseResult, EnrichedDocument enrichedDocument) {

    }

    private EnrichedDocument findEnrichedDocument(Document document) {

        //first check whether the document has isbn or not
        String isbn = document.getIsbn();
        List<EnrichedDocument> nearDuplicates;
        if (isbn == null) {
            // if it's null, we search through every record in the storage
            nearDuplicates = enrichedDocumentDao.getNearDuplicates(document);
        } else {
            // if it's not null, we search through record where isbn is the same
            nearDuplicates = enrichedDocumentDao.getNearDuplicatesWithIsbn(document);

            if (nearDuplicates == null || nearDuplicates.size() == 0) {
                // if it didn't find anything, search through record with null isbn.
                nearDuplicates = enrichedDocumentDao.getNearDuplicatesWithNullIsbn(document);
            }
        }

        if (nearDuplicates != null && nearDuplicates.size() > 0) {

            double maxDistance = 0;
            EnrichedDocument closestDocument = null;
            String titleValue = document.getTitle().getValue();
            String authorValue = document.getAuthor().getValue();
            for (EnrichedDocument nearDuplicate : nearDuplicates) {

                double titleDistance = stringHashService.distance(titleValue, nearDuplicate.getTitle().getValue());

                double authorDistance = stringHashService.distance(authorValue, nearDuplicate.getAuthor().getValue());

                double resultDistance = (titleDistance + authorDistance) / 2;

                if (resultDistance > maxDistance) {
                    maxDistance = resultDistance;
                    closestDocument = nearDuplicate;
                }

            }
            document.setDistance(maxDistance);
            return closestDocument;
        }

        return null;
    }
}
