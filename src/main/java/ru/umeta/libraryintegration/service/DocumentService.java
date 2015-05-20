package ru.umeta.libraryintegration.service;

import org.springframework.beans.factory.annotation.Autowired;
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
    private ProtocolService enrichedDocumentService;


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

                findEnrichedDocument(document);
            }
        }
    }

    private EnrichedDocument findEnrichedDocument(Document document) {
        filterIsbn();

        List<EnrichedDocument> nearDuplicates = enrichedDocumentService.findNearDuplicates(document);

    }
}
