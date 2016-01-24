package ru.umeta.libraryintegration.service

import org.springframework.util.StringUtils
import ru.umeta.libraryintegration.inmemory.EnrichedDocumentRepository
import ru.umeta.libraryintegration.json.ModsParseResult
import ru.umeta.libraryintegration.json.ParseResult
import ru.umeta.libraryintegration.json.UploadResult
import ru.umeta.libraryintegration.model.Document
import ru.umeta.libraryintegration.model.EnrichedDocument
import ru.umeta.libraryintegration.model.EnrichedDocumentLite
import java.util.*


/**
 * Service for operating with [Document] and [EnrichedDocument]
 * Created by ctash on 28.04.2015.
 */
object DocumentService : AutoCloseable {

    private val DEFAULT_PROTOCOL = "Z39.50"
    private val DUPLICATE_SIZE = 1000

    val enrichedDocumentRepository = EnrichedDocumentRepository
    val stringHashService = StringHashService

    fun processDocumentList(resultList: List<ParseResult>, protocolName: String?): UploadResult {
        var newEnriched = 0
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

                    val authorId = stringHashService.getFromRepository(author)
                    val titleId = stringHashService.getFromRepository(title)
                    var isbn: String? = parseResult.isbn
                    if (isbn.isNullOrEmpty()) {
                    }
                    isbn = null
                    val enrichedDocument = EnrichedDocument(-1, authorId, titleId, isbn, null, Date(),
                            parseResult.publishYear)
                    enrichedDocumentRepository.save(enrichedDocument)
                    newEnriched++;
                    parsedDocs++
                } catch (e: Exception) {
                    throw e
                }


            }
        }
        return UploadResult(parsedDocs, newEnriched)
    }

    private fun findEnrichedDocument(document: Document): EnrichedDocument? {

        //first check whether the document has isbn or not
        val isbn = document.isbn
        val publishYear = document.publishYear
        var nearDuplicates: List<EnrichedDocumentLite>
        if (isbn == null) {
            // if it's null, we search through every record in the storage
            nearDuplicates = enrichedDocumentRepository.getNearDuplicates(document)
        } else {
            nearDuplicates = enrichedDocumentRepository.getNearDuplicatesWithIsbn(document)

            if (nearDuplicates == null || nearDuplicates.size == 0) {
                // if it didn't find anything, search through record with null isbn.
                nearDuplicates = enrichedDocumentRepository.getNearDuplicatesWithNullIsbn(document)
            }

        }

        if (nearDuplicates != null && nearDuplicates.size > 0) {

            var maxDistance = 0.0
            val minDistance = 0.7
            var closestDocument: EnrichedDocumentLite? = null

            val title = document.title
            val author = document.author

            for (nearDuplicate in nearDuplicates) {

                val titleDistance = stringHashService.distance(title, nearDuplicate.titleId)

                val authorDistance = stringHashService.distance(author, nearDuplicate.authorId)

                val resultDistance = (titleDistance + authorDistance) / 2

                if (resultDistance > maxDistance && resultDistance > minDistance) {
                    maxDistance = resultDistance
                    closestDocument = nearDuplicate
                }

            }
            //            document.distance = maxDistance
            if (closestDocument != null) {
                return enrichedDocumentRepository.getById(closestDocument.id)
            }
        }

        return null
    }

    fun findEnrichedDocument(document: EnrichedDocumentLite): EnrichedDocument? {

        //first check whether the document has
        val nearDuplicates = enrichedDocumentRepository.getNearDuplicates(document)

        if (nearDuplicates.size > 0) {

            var maxDistance = 0.0
            val minDistance = 0.7
            var closestDocument: EnrichedDocumentLite? = null

            val title = document.title
            val author = document.author

            for (nearDuplicate in nearDuplicates) {

                val titleDistance = stringHashService.distance(title, nearDuplicate.titleId)

                val authorDistance = stringHashService.distance(author, nearDuplicate.authorId)

                val resultDistance = (titleDistance + authorDistance) / 2

                if (resultDistance > maxDistance && resultDistance > minDistance) {
                    maxDistance = resultDistance
                    closestDocument = nearDuplicate
                }

            }
            //            document.distance = maxDistance
            if (closestDocument != null) {
                return enrichedDocumentRepository.getById(closestDocument.id)
            }
        }

        return null
    }



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

    override fun close() {
        enrichedDocumentRepository.close()
        stringHashService.close()
    }

    fun getDocuments(): List<EnrichedDocumentLite> {
        return enrichedDocumentRepository.list
    }
}
