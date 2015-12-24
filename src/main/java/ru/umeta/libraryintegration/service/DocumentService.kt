package ru.umeta.libraryintegration.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.StringUtils
import ru.umeta.libraryintegration.inmemory.DocumentRepository
import ru.umeta.libraryintegration.inmemory.EnrichedDocumentRepository
import ru.umeta.libraryintegration.inmemory.IEnrichedDocumentRepository
import ru.umeta.libraryintegration.json.ModsParseResult
import ru.umeta.libraryintegration.json.ParseResult
import ru.umeta.libraryintegration.json.UploadResult
import ru.umeta.libraryintegration.model.Document
import ru.umeta.libraryintegration.model.EnrichedDocument
import ru.umeta.libraryintegration.model.EnrichedDocumentLite
import ru.umeta.libraryintegration.parser.ModsXMLParser

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*

import com.google.common.base.Preconditions.checkNotNull

/**
 * Created by ctash on 28.04.2015.
 */
class DocumentService
@Autowired
constructor(val stringHashService: StringHashService,
            val enrichedDocumentRepository: IEnrichedDocumentRepository,
            val modsXMLParser: ModsXMLParser) {

    fun processDocumentList(resultList: List<ParseResult>, protocolName: String?): UploadResult {
        val newEnriched = 0
        var parsedDocs = 0

        for (parseResult in resultList) {
            if (parseResult is ModsParseResult) {
                try {
                    var author = parseResult.author
                    if (author.length > 255) {
                        author = author.substring(0, 255)
                    }
                    var title = parseResult.title
                    if (title.length > 255) {
                        title = title.substring(0, 255)
                    }

                    val docAuthor = stringHashService.getFromRepository(author)
                    val docTitle = stringHashService.getFromRepository(title)
                    var isbn: String? = parseResult.isbn
                    if (isbn.isNullOrEmpty()) {
                        isbn = null
                    }

//                    val document = EnrichedDocument(-1, docAuthor, docTitle, isbn, null, Date(), parseResult.publishYear)
//                    enrichedDocumentRepository.save(document)
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
                    parsedDocs++
                } catch (e: Exception) {
                    //                    System.err.println("ERROR. Failed to add a document with title {" +
                    //                            parseResult.getTitle() + "}, author {" +
                    //                            parseResult.getAuthor() + "}");
                }


            }
        }
        return UploadResult(parsedDocs, newEnriched)
    }

//    private fun mergeDocuments(modsParseResult: ModsParseResult, enrichedDocument: EnrichedDocument) {
//        modsXMLParser.enrich(modsParseResult.modsDefinition, enrichedDocument)
//    }

//    private fun findEnrichedDocument(document: Document): EnrichedDocument? {
//
//        //first check whether the document has isbn or not
//        val isbn = document.isbn
//        val publishYear = document.publishYear
//        var nearDuplicates: List<EnrichedDocumentLite>?
//        if (isbn == null && publishYear == null) {
//            // if it's null, we search through every record in the storage
//            nearDuplicates = enrichedDocumentRepository.getNearDuplicates(document)
//        } else {
//
//            // if it's not null, we search through record where isbn is the same
//            if (publishYear == null) {
//                nearDuplicates = enrichedDocumentRepository.getNearDuplicatesWithIsbn(document)
//
//                if (nearDuplicates == null || nearDuplicates.size == 0) {
//                    // if it didn't find anything, search through record with null isbn.
//                    nearDuplicates = enrichedDocumentRepository.getNearDuplicatesWithNullIsbn(document)
//                }
//            } else if (isbn == null) {
//                nearDuplicates = enrichedDocumentRepository.getNearDuplicatesWithPublishYear(document)
//
//            } else {
//                nearDuplicates = enrichedDocumentRepository.getNearDuplicatesWithIsbn(document)
//
//                if (nearDuplicates == null || nearDuplicates.size == 0) {
//                    nearDuplicates = enrichedDocumentRepository.getNearDuplicatesWithPublishYear(document)
//                }
//            }
//
//        }
//
//        if (nearDuplicates != null && nearDuplicates.size > 0) {
//
//            var maxDistance = 0.0
//            val minDistance = 0.7
//            var closestDocument: EnrichedDocumentLite? = null
//
//            val titleTokens = stringHashService.getTokens(document.title!!.value)
//            val authorTokens = stringHashService.getTokens(document.author!!.value)
//
//            for (nearDuplicate in nearDuplicates) {
//
//                val titleDistance = stringHashService.distance(titleTokens, nearDuplicate.titleTokens)
//
//                val authorDistance = stringHashService.distance(authorTokens, nearDuplicate.authorTokens)
//
//                val resultDistance = (titleDistance + authorDistance) / 2
//
//                if (resultDistance > maxDistance && resultDistance > minDistance) {
//                    maxDistance = resultDistance
//                    closestDocument = nearDuplicate
//                }
//
//            }
//            document.distance = maxDistance
//            if (closestDocument != null) {
//                return enrichedDocumentRepository.getById(closestDocument.id)
//            }
//        }
//
//        return null
//    }

    fun addNoise(parseResult: ParseResult, saltLevel: Int): List<ParseResult>? {
        val author = parseResult.author
        val title = parseResult.title

        val authorLength = author.length
        val titleLength = title.length

        parseResult.isbn = null
        if (StringUtils.isEmpty(author) || StringUtils.isEmpty(title)) {
            return null
        } else {
            val resultList = ArrayList<ParseResult>()
            for (i in 0..DUPLICATE_SIZE - 1) {
                val newParseResult = parseResult.clone()
                val newAuthor = StringBuilder(author)
                val newTitle = StringBuilder(title)
                for (j in 0..saltLevel - 1) {
                    val rnd = Random()
                    var noiseIndex = rnd.nextInt(authorLength)
                    newAuthor.setCharAt(noiseIndex, '#')

                    noiseIndex = Random().nextInt(titleLength)
                    newTitle.setCharAt(noiseIndex, '#')
                }
                newParseResult.author = newAuthor.toString()
                newParseResult.title = newTitle.toString()
                resultList.add(newParseResult)
            }
            return resultList
        }
    }

    companion object {
        private val DEFAULT_PROTOCOL = "Z39.50"
        private val DUPLICATE_SIZE = 1000
    }
}
