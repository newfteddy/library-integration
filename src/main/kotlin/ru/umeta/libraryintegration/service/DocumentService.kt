package ru.umeta.libraryintegration.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import ru.umeta.libraryintegration.controller.ConsoleController
import ru.umeta.libraryintegration.inmemory.InMemoryRepository
import ru.umeta.libraryintegration.inmemory.RedisRepository
import ru.umeta.libraryintegration.json.ModsParseResult
import ru.umeta.libraryintegration.json.ParseResult
import ru.umeta.libraryintegration.json.UploadResult
import ru.umeta.libraryintegration.model.EnrichedDocumentLite
import java.util.*


/**
 * Service for operating with [Document] and [EnrichedDocument]
 * Created by ctash on 28.04.2015.
 */
@Component
open class DocumentService
@Autowired constructor(
        val redisRepository: RedisRepository,
        val stringHashService: StringHashService,
        val enrichedDocumentRepository: InMemoryRepository) {

    companion object {
        private val logger = LoggerFactory.getLogger(ConsoleController::class.java)
    }

    fun processDocumentList(resultList: List<ParseResult>, protocolName: String?, saveXml: Boolean): UploadResult {
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

                    val authorId = stringHashService.getFromRepositoryInit(author)
                    val titleId = stringHashService.getFromRepositoryInit(title)
                    val authorHash = stringHashService.getStringHash(author)
                    val titleHash = stringHashService.getStringHash(title)
                    val isbn: String = parseResult.isbn ?: ""
                    val year = parseResult.publishYear ?: -1

                    val document = EnrichedDocumentLite(
                            -1,
                            author.hashCode(), authorHash.simHash,
                            title.hashCode(), titleHash.simHash,
                            isbn.hashCode(),
                            year,
                            0.0)
                    val docId = redisRepository.addDoc(document)
                    if (saveXml) {
                        redisRepository.addXml(docId, parseResult.modsDefinition.toString())
                    }
                    newEnriched++;
                    parsedDocs++
                } catch (e: Exception) {
                    logger.error(e.message)
                }
            }
        }
        return UploadResult(parsedDocs, newEnriched)
    }

    fun findEnrichedDocuments(doc: EnrichedDocumentLite): DFS {
        var nearDuplicates = enrichedDocumentRepository.getNearDuplicates(doc)
        var iterationsIsbn = 0L
        var iterationsYear = 0L
        var remainingDocs = 0L
        val isbn = doc.isbn
        val year = doc.publishYear
        nearDuplicates = nearDuplicates.filter { it ->
            val otherIsbn = it.isbn
            val otherYear = it.publishYear

            if (otherIsbn != 0 && isbn != 0 && otherIsbn != isbn) {
                iterationsIsbn++
                return@filter false
            } else {
                if (otherYear != -1 && year != -1 && otherYear != year) {
                    iterationsYear++
                    return@filter false
                } else {
                    remainingDocs++
                    return@filter true
                }
            }
        }

        val dfs = DFS(nearDuplicates, iterationsIsbn, iterationsYear, remainingDocs)
        return dfs
    }
    fun findEnrichedDocumentsNA(doc: EnrichedDocumentLite): DFS {
        var nearDuplicatesNA = enrichedDocumentRepository.getNearDuplicatesNA(doc)
        var iterationsIsbn = 0L
        var iterationsYear = 0L
        var remainingDocs = 0L
        val isbn = doc.isbn
        val year = doc.publishYear
        /*correction*/
        nearDuplicatesNA = nearDuplicatesNA.filter { it ->
            val otherIsbn = it.isbn
            val otherYear = it.publishYear

            if (otherIsbn != 0 && isbn != 0 && otherIsbn != isbn) {
                iterationsIsbn++
                return@filter false
            } else {
                if (otherYear != -1 && year != -1 && otherYear != year) {
                    iterationsYear++
                    return@filter false
                } else {
                    remainingDocs++
                    return@filter true
                }
            }
        }

        /*--correction*/

        val dfs = DFS(nearDuplicatesNA, iterationsIsbn, iterationsYear, remainingDocs)
        return dfs
    }

    data class DFS(
            val component: List<EnrichedDocumentLite>,
            var iterationsIsbn: Long,
            var iterationsYear: Long,
            var remainingDocs: Long)

    fun processDocumentListInit(resultList: List<ParseResult>) = processDocumentList(resultList, null, false)

    fun processDocumentListLarge(resultList: List<ParseResult>) = processDocumentList(resultList, null, true)

    fun getDoc(id: Int): EnrichedDocumentLite? {
        return enrichedDocumentRepository.docStorage[id]
    }


}
