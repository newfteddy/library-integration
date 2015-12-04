package ru.umeta.libraryintegration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import ru.umeta.libraryintegration.inmemory.DocumentRepository;
import ru.umeta.libraryintegration.inmemory.EnrichedDocumentRepository;
import ru.umeta.libraryintegration.inmemory.IEnrichedDocumentRepository;
import ru.umeta.libraryintegration.json.ModsParseResult;
import ru.umeta.libraryintegration.json.ParseResult;
import ru.umeta.libraryintegration.json.UploadResult;
import ru.umeta.libraryintegration.model.Document;
import ru.umeta.libraryintegration.model.EnrichedDocument;
import ru.umeta.libraryintegration.model.EnrichedDocumentLite;
import ru.umeta.libraryintegration.parser.ModsXMLParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by ctash on 28.04.2015.
 */
public class DocumentService {

    private static final String DEFAULT_PROTOCOL = "Z39.50";
    private static final int DUPLICATE_SIZE = 1000;

    private final StringHashService stringHashService;

    private final ProtocolService protocolService;

    private final IEnrichedDocumentRepository enrichedDocumentRepository;

    private final DocumentRepository documentRepository;

    private final ModsXMLParser modsXMLParser;

    private final EnrichedDocumentRepository enrichedDocumentDao;

    @Autowired
    public DocumentService(StringHashService stringHashService, ProtocolService protocolService, IEnrichedDocumentRepository enrichedDocumentRepository, DocumentRepository documentRepository, ModsXMLParser modsXMLParser, EnrichedDocumentRepository enrichedDocumentDao) {
        this.stringHashService = stringHashService;
        this.protocolService = protocolService;
        this.enrichedDocumentRepository = enrichedDocumentRepository;
        this.documentRepository = documentRepository;
        this.modsXMLParser = modsXMLParser;
        this.enrichedDocumentDao = enrichedDocumentDao;
    }

    public UploadResult processDocumentList(List<ParseResult> resultList, String protocolName) {
        int newEnriched = 0;
        int parsedDocs = 0;

        for (ParseResult parseResult : checkNotNull(resultList)) {
            if (parseResult instanceof ModsParseResult) {
                try {
                    EnrichedDocument document = new EnrichedDocument();
                    ModsParseResult modsParseResult = (ModsParseResult) parseResult;

                    String author = modsParseResult.getAuthor();
                    if (author.length() > 255) {
                        author = author.substring(0, 255);
                    }
                    String title = modsParseResult.getTitle();
                    if (title.length() > 255) {
                        title = title.substring(0, 255);
                    }
                    document.setAuthor(stringHashService.getFromRepository(author));
                    document.setTitle(stringHashService.getFromRepository(title));
                    document.setCreationTime(new Date());
                    String isbn = modsParseResult.getIsbn();
                    if (StringUtils.isEmpty(isbn)) {
                        isbn = null;
                    }
                    document.setIsbn(isbn);
                    document.setPublishYear(modsParseResult.getPublishYear());
                    document.setXml(null);
                    enrichedDocumentRepository.save(document);
//
//
//                    ModsParseResult modsParseResult = (ModsParseResult) parseResult;
//
//                    document.setAuthor(stringHashService.getFromRepository(modsParseResult.getAuthor()));
//                    document.setTitle(stringHashService.getFromRepository(modsParseResult.getTitle()));
//                    document.setCreationTime(new Date());
//                    String isbn = modsParseResult.getIsbn();
//                    if (StringUtils.isEmpty(isbn)) {
//                        isbn = null;
//                    }
//                    document.setIsbn(isbn);
//                    document.setProtocol(protocolService.getFromRepository(protocolName == null ? DEFAULT_PROTOCOL : protocolName));
//                    document.setPublishYear(modsParseResult.getPublishYear());
//                    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//                        modsParseResult.getModsDefinition().save(outputStream);
//                        document.setXml(new String(outputStream.toByteArray(), "UTF-8"));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        continue;
//                    }
//                    EnrichedDocument enrichedDocument = findEnrichedDocument(document);
//                    if (enrichedDocument != null) {
//                        if (enrichedDocument.getPublishYear() == null) {
//                            enrichedDocument.setPublishYear(document.getPublishYear());
//                        }
//                        if (enrichedDocument.getIsbn() == null) {
//                            enrichedDocument.setIsbn(document.getIsbn());
//                        }
//                        mergeDocuments(modsParseResult, enrichedDocument);
//                        enrichedDocumentRepository.update(enrichedDocument);
//                    } else {
//                        enrichedDocument = new EnrichedDocument();
//                        enrichedDocument.setAuthor(document.getAuthor());
//                        enrichedDocument.setTitle(document.getTitle());
//                        enrichedDocument.setIsbn(document.getIsbn());
//                        enrichedDocument.setXml(document.getXml());
//                        enrichedDocument.setCreationTime(document.getCreationTime());
//                        enrichedDocument.setPublishYear(document.getPublishYear());
//                        enrichedDocument.setId(enrichedDocumentRepository.save(enrichedDocument).longValue());
//                        newEnriched++;
//                        document.setDistance(1.);
//                    }
//                    document.setEnrichedDocument(enrichedDocument);
//                    documentRepository.save(document);
                    parsedDocs++;
                } catch (Exception e) {
//                    System.err.println("ERROR. Failed to add a document with title {" +
//                            parseResult.getTitle() + "}, author {" +
//                            parseResult.getAuthor() + "}");
                }


            }
        }
        return new UploadResult(parsedDocs, newEnriched);
    }

    private void mergeDocuments(ModsParseResult modsParseResult, EnrichedDocument enrichedDocument) {
        modsXMLParser.enrich(modsParseResult.getModsDefinition(),enrichedDocument);
    }

    private EnrichedDocument findEnrichedDocument(Document document) {

        //first check whether the document has isbn or not
        String isbn = document.getIsbn();
        Integer publishYear = document.getPublishYear();
        List<EnrichedDocumentLite> nearDuplicates;
        if (isbn == null && publishYear == null) {
            // if it's null, we search through every record in the storage
            nearDuplicates = enrichedDocumentRepository.getNearDuplicates(document);
        } else {

            // if it's not null, we search through record where isbn is the same
            if (publishYear == null) {
                nearDuplicates = enrichedDocumentRepository.getNearDuplicatesWithIsbn(document);

                if (nearDuplicates == null || nearDuplicates.size() == 0) {
                    // if it didn't find anything, search through record with null isbn.
                    nearDuplicates = enrichedDocumentRepository.getNearDuplicatesWithNullIsbn(document);
                }
            } else if (isbn == null) {
                nearDuplicates = enrichedDocumentRepository.getNearDuplicatesWithPublishYear(document);

            } else {
                nearDuplicates = enrichedDocumentRepository.getNearDuplicatesWithIsbn(document);

                if (nearDuplicates == null || nearDuplicates.size() == 0) {
                    nearDuplicates = enrichedDocumentRepository.getNearDuplicatesWithPublishYear(document);
                }
            }

        }

        if (nearDuplicates != null && nearDuplicates.size() > 0) {

            double maxDistance = 0;
            double minDistance = 0.7;
            EnrichedDocumentLite closestDocument = null;

            Set<String> titleTokens = stringHashService.getTokens(document.getTitle().getValue());
            Set<String> authorTokens = stringHashService.getTokens(document.getAuthor().getValue());

            for (EnrichedDocumentLite nearDuplicate : nearDuplicates) {

                double titleDistance = stringHashService.distance(titleTokens, nearDuplicate.titleTokens);

                double authorDistance = stringHashService.distance(authorTokens, nearDuplicate.authorTokens);

                double resultDistance = (titleDistance + authorDistance) / 2;

                if (resultDistance > maxDistance && resultDistance > minDistance) {
                    maxDistance = resultDistance;
                    closestDocument = nearDuplicate;
                }

            }
            document.setDistance(maxDistance);
            if (closestDocument != null) {
                return enrichedDocumentRepository.getById(closestDocument.id);
            }
        }

        return null;
    }

    public List<ParseResult> addNoise(ParseResult parseResult, int saltLevel) {
        String author = parseResult.getAuthor();
        String title = parseResult.getTitle();

        int authorLength = author.length();
        int titleLength = title.length();

        parseResult.setIsbn(null);
        if (StringUtils.isEmpty(author) || StringUtils.isEmpty(title)) {
            return null;
        } else {
            ArrayList<ParseResult> resultList = new ArrayList<>();
            for (int i = 0; i < DUPLICATE_SIZE; i++) {
                ParseResult newParseResult = parseResult.clone();
                StringBuilder newAuthor = new StringBuilder(author);
                StringBuilder newTitle = new StringBuilder(title);
                for (int j = 0; j < saltLevel; j++) {
                    Random rnd = new Random();
                    int noiseIndex = rnd.nextInt(authorLength);
                    newAuthor.setCharAt(noiseIndex, '#');

                    noiseIndex = new Random().nextInt(titleLength);
                    newTitle.setCharAt(noiseIndex, '#');
                }
                newParseResult.setAuthor(newAuthor.toString());
                newParseResult.setTitle(newTitle.toString());
                resultList.add(newParseResult);
            }
            return resultList;
        }
    }

    public void getPersistedData() {
        enrichedDocumentRepository.getPersistedData();
    }
}
