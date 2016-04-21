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
class DocumentService
@Autowired constructor(
        val redisRepository: RedisRepository,
        val stringHashService: StringHashService,
        val enrichedDocumentRepository: InMemoryRepository) {

    companion object {
        val logger = LoggerFactory.getLogger(ConsoleController::class.java)
    }

    private val DUPLICATE_SIZE = 1000

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
                    redisRepository.addDoc(document)
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

    data class DFS(
            val component: List<EnrichedDocumentLite>,
            var iterationsIsbn: Long,
            var iterationsYear: Long,
            var remainingDocs: Long)



    //    class DFS(val enrichedDocumentRepository: EnrichedDocumentRepository,
    //              val stringHashService: StringHashService) {
    //
    //        val used = TLongHashSet()
    //        val component = ArrayList<EnrichedDocumentLite>()
    //        var filtered= ArrayList<EnrichedDocumentLite>()
    //        var stack = Stack<EnrichedDocumentLite>()
    //
    //        fun apply(document: EnrichedDocumentLite) {
    //            stack.add(document)
    //            while (!stack.isEmpty()) {
    //                val cur = stack.pop()
    //                val id = cur.id
    //                if (!used.contains(id)) {
    //                    val authorId = cur.authorId
    //                    val titleId = cur.titleId
    //                    used.add(id)
    //                    component.add(cur)
    //                    //filter documents which have the nearest measure of 0.7 or more
    //                    val current = filtered;
    //                    val nearDuplicates = enrichedDocumentRepository.getNearDuplicates(cur, current)
    //                    filtered = nearDuplicates.filter {
    //                            (stringHashService.distance(authorId, it.authorId)
    //                            + stringHashService.distance(titleId, it.titleId) >= 0.7 * 2)}
    //                        .toArrayList();
    //                    current.forEach { filtered.add(it)}
    //                    for (duplicate in filtered) {
    //                        stack.add(duplicate)
    //                    }
    //                }
    //            }
    //        }
    //
    //    }

//    fun findEnrichedDocuments(document: EnrichedDocumentLite): List<EnrichedDocumentLite> {
//
//            val dfs = DFS(enrichedDocumentRepository, stringHashService)
//            dfs.apply(document);
//
//            return dfs.component;
//        return emptyList()
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

    fun getDocuments(): List<EnrichedDocumentLite> {
        //return enrichedDocumentRepository.list
        return emptyList()
    }

    fun processDocumentListInit(resultList: List<ParseResult>) = processDocumentList(resultList, null)

    fun getDoc(id: Int): EnrichedDocumentLite? {
        return enrichedDocumentRepository.docStorage[id]
    }


}
