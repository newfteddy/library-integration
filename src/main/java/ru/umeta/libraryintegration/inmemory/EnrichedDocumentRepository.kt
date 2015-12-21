package ru.umeta.libraryintegration.inmemory

import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Repository
import ru.umeta.libraryintegration.fs.EnrichedDocumentFsPersister
import ru.umeta.libraryintegration.model.Document
import ru.umeta.libraryintegration.model.EnrichedDocument
import ru.umeta.libraryintegration.model.EnrichedDocumentLite
import ru.umeta.libraryintegration.model.StringHash
import ru.umeta.libraryintegration.service.StringHashService

import java.util.ArrayList
import java.util.stream.Collectors

/**
 * The repository consists of large amount of hashmaps to get fast access to near duplicates.
 * The maps have the following structure:
 * year -> t1 -> t2 -> a1
 * ||    \\ -> a2
 * \\ -> t3 -> a1
 * ||    \\ -> a2
 * \\ -> t4 -> a1
 * \\ -> a2
 * ...
 * Created by Kirill Kosolapov (https://github.com/c-tash) on 12.11.2015.
 */
@Primary
@Repository
class EnrichedDocumentRepository
@Autowired
constructor(private val stringHashService: StringHashService, private val fsPersister: EnrichedDocumentFsPersister) : IEnrichedDocumentRepository {

    internal var isbnMap: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap.create<String, EnrichedDocumentLite>()

    //no year maps
    internal var t1t2a1Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap.create<Int, EnrichedDocumentLite>()
    internal var t1t2a2Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap.create<Int, EnrichedDocumentLite>()
    internal var t1t3a1Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap.create<Int, EnrichedDocumentLite>()
    internal var t1t3a2Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap.create<Int, EnrichedDocumentLite>()
    internal var t1t4a1Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap.create<Int, EnrichedDocumentLite>()
    internal var t1t4a2Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap.create<Int, EnrichedDocumentLite>()
    internal var t2t3a1Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap.create<Int, EnrichedDocumentLite>()
    internal var t2t3a2Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap.create<Int, EnrichedDocumentLite>()
    internal var t2t4a1Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap.create<Int, EnrichedDocumentLite>()
    internal var t2t4a2Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap.create<Int, EnrichedDocumentLite>()
    internal var t3t4a1Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap.create<Int, EnrichedDocumentLite>()
    internal var t3t4a2Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap.create<Int, EnrichedDocumentLite>()

    //year maps
    internal var yt1t2a1Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap.create<Int, EnrichedDocumentLite>()
    internal var yt1t2a2Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap.create<Int, EnrichedDocumentLite>()
    internal var yt1t3a1Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap.create<Int, EnrichedDocumentLite>()
    internal var yt1t3a2Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap.create<Int, EnrichedDocumentLite>()
    internal var yt1t4a1Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap.create<Int, EnrichedDocumentLite>()
    internal var yt1t4a2Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap.create<Int, EnrichedDocumentLite>()
    internal var yt2t3a1Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap.create<Int, EnrichedDocumentLite>()
    internal var yt2t3a2Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap.create<Int, EnrichedDocumentLite>()
    internal var yt2t4a1Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap.create<Int, EnrichedDocumentLite>()
    internal var yt2t4a2Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap.create<Int, EnrichedDocumentLite>()
    internal var yt3t4a1Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap.create<Int, EnrichedDocumentLite>()
    internal var yt3t4a2Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap.create<Int, EnrichedDocumentLite>()

    private var identity = 0L

    init {
        val lastId = fsPersister.applyToPersisted { enrichedDocument: EnrichedDocument -> this.putIntoMaps(enrichedDocument) }
        identity = lastId + 1
    }

//    override fun getNearDuplicates(document: Document): List<EnrichedDocumentLite> {
//        val author = document.author
//        val title = document.title
//
//        val a1 = author.hashPart1
//        val a2 = author.hashPart2
//
//        val t1 = title.hashPart1
//        val t2 = title.hashPart2
//        val t3 = title.hashPart3
//        val t4 = title.hashPart4
//
//        val yt1t2a1Hash = getHashWithoutYear(t1, t2, a1)
//        val yt1t2a2Hash = getHashWithoutYear(t1, t2, a2)
//        val yt1t3a1Hash = getHashWithoutYear(t1, t3, a1)
//        val yt1t3a2Hash = getHashWithoutYear(t1, t3, a2)
//        val yt1t4a1Hash = getHashWithoutYear(t1, t4, a1)
//        val yt1t4a2Hash = getHashWithoutYear(t1, t4, a2)
//
//        val yt2t3a1Hash = getHashWithoutYear(t2, t3, a1)
//        val yt2t3a2Hash = getHashWithoutYear(t2, t3, a2)
//        val yt2t4a1Hash = getHashWithoutYear(t2, t4, a1)
//        val yt2t4a2Hash = getHashWithoutYear(t2, t4, a2)
//
//        val yt3t4a1Hash = getHashWithoutYear(t3, t4, a1)
//        val yt3t4a2Hash = getHashWithoutYear(t3, t4, a2)
//
//        val result = ArrayList<EnrichedDocumentLite>()
//
//        result.addAll(t1t2a1Map.get(yt1t2a1Hash))
//        result.addAll(t1t2a2Map.get(yt1t2a2Hash))
//        result.addAll(t1t3a1Map.get(yt1t3a1Hash))
//        result.addAll(t1t3a2Map.get(yt1t3a2Hash))
//        result.addAll(t1t4a1Map.get(yt1t4a1Hash))
//        result.addAll(t1t4a2Map.get(yt1t4a2Hash))
//        result.addAll(t2t3a1Map.get(yt2t3a1Hash))
//        result.addAll(t2t3a2Map.get(yt2t3a2Hash))
//        result.addAll(t2t4a1Map.get(yt2t4a1Hash))
//        result.addAll(t2t4a2Map.get(yt2t4a2Hash))
//        result.addAll(t3t4a1Map.get(yt3t4a1Hash))
//        result.addAll(t3t4a2Map.get(yt3t4a2Hash))
//        return result.distinct().toList();
//    }

    private fun getHashWithoutYear(hash1: Byte, hash2: Byte, hash3: Byte): Int {
        //shift is of the size of a byte
        val shift = 8
        var result = hash1.toInt()
        result = (result shl shift) + hash2.toInt()
        result = (result shl shift) + hash3.toInt()
        return result
    }

    override fun getNearDuplicatesWithIsbn(document: Document): List<EnrichedDocumentLite> {
        return isbnMap.get(document.isbn).toList();
    }

//    override fun getNearDuplicatesWithNullIsbn(document: Document): List<EnrichedDocumentLite> {
//        val nearDuplicates = getNearDuplicates(document)
//        return nearDuplicates.filter(EnrichedDocumentLite::isbnIsNull);
//    }

//    override fun getNearDuplicatesWithPublishYear(document: Document): List<EnrichedDocumentLite> {
//        val author = document.author
//        val title = document.title
//        val year = document.publishYear
//
//        val a1 = author.hashPart1
//        val a2 = author.hashPart2
//
//        val t1 = title.hashPart1
//        val t2 = title.hashPart2
//        val t3 = title.hashPart3
//        val t4 = title.hashPart4
//
//        val yt1t2a1Hash = getHashWithYear(year!!, t1, t2, a1)
//        val yt1t2a2Hash = getHashWithYear(year, t1, t2, a2)
//        val yt1t3a1Hash = getHashWithYear(year, t1, t3, a1)
//        val yt1t3a2Hash = getHashWithYear(year, t1, t3, a2)
//        val yt1t4a1Hash = getHashWithYear(year, t1, t4, a1)
//        val yt1t4a2Hash = getHashWithYear(year, t1, t4, a2)
//
//        val yt2t3a1Hash = getHashWithYear(year, t2, t3, a1)
//        val yt2t3a2Hash = getHashWithYear(year, t2, t3, a2)
//        val yt2t4a1Hash = getHashWithYear(year, t2, t4, a1)
//        val yt2t4a2Hash = getHashWithYear(year, t2, t4, a2)
//
//        val yt3t4a1Hash = getHashWithYear(year, t3, t4, a1)
//        val yt3t4a2Hash = getHashWithYear(year, t3, t4, a2)
//
//        val result = ArrayList<EnrichedDocumentLite>()
//
//        result.addAll(yt1t2a1Map.get(yt1t2a1Hash))
//        result.addAll(yt1t2a2Map.get(yt1t2a2Hash))
//        result.addAll(yt1t3a1Map.get(yt1t3a1Hash))
//        result.addAll(yt1t3a2Map.get(yt1t3a2Hash))
//        result.addAll(yt1t4a1Map.get(yt1t4a1Hash))
//        result.addAll(yt1t4a2Map.get(yt1t4a2Hash))
//        result.addAll(yt2t3a1Map.get(yt2t3a1Hash))
//        result.addAll(yt2t3a2Map.get(yt2t3a2Hash))
//        result.addAll(yt2t4a1Map.get(yt2t4a1Hash))
//        result.addAll(yt2t4a2Map.get(yt2t4a2Hash))
//        result.addAll(yt3t4a1Map.get(yt3t4a1Hash))
//        result.addAll(yt3t4a2Map.get(yt3t4a2Hash))
//        return result.distinct();
//    }

    private fun getHashWithYear(year: Int, hash1: Byte, hash2: Byte, hash3: Byte): Int {
        val shift = 8
        var result = year
        val mask = 255
        result = ((result shl shift) + (hash1.toInt() and mask))
        result = (result shl shift) + (hash2.toInt() and mask)
        result = (result shl shift) + (hash3.toInt() and mask)
        return result
    }

    override fun save(enrichedDocument: EnrichedDocument) {
        enrichedDocument.id = identity++
        putIntoMaps(enrichedDocument)
        fsPersister.save(enrichedDocument)

    }

    private fun putIntoMaps(enrichedDocument: EnrichedDocument) {
        val id = enrichedDocument.id
        val isbn = enrichedDocument.isbn
        val author = enrichedDocument.author
        val title = enrichedDocument.title
        val authorTokens = author.tokens
        val titleTokens = title.tokens
        val lite = EnrichedDocumentLite(id, authorTokens, titleTokens)
        if (isbn != null) {
            lite.nullIsbn = false
            isbnMap.put(isbn, lite)
        }


        val year = enrichedDocument.publishYear

        val a1 = author.hashPart1
        val a2 = author.hashPart2

        val t1 = title.hashPart1
        val t2 = title.hashPart2
        val t3 = title.hashPart3
        val t4 = title.hashPart4

        val t1t2a1Hash = getHashWithoutYear(t1, t2, a1)
        val t1t2a2Hash = getHashWithoutYear(t1, t2, a2)
        val t1t3a1Hash = getHashWithoutYear(t1, t3, a1)
        val t1t3a2Hash = getHashWithoutYear(t1, t3, a2)
        val t1t4a1Hash = getHashWithoutYear(t1, t4, a1)
        val t1t4a2Hash = getHashWithoutYear(t1, t4, a2)

        val t2t3a1Hash = getHashWithoutYear(t2, t3, a1)
        val t2t3a2Hash = getHashWithoutYear(t2, t3, a2)
        val t2t4a1Hash = getHashWithoutYear(t2, t4, a1)
        val t2t4a2Hash = getHashWithoutYear(t2, t4, a2)

        val t3t4a1Hash = getHashWithoutYear(t3, t4, a1)
        val t3t4a2Hash = getHashWithoutYear(t3, t4, a2)

        if (year != null) {
            val yt1t2a1Hash = getHashWithYear(year, t1, t2, a1)
            val yt1t2a2Hash = getHashWithYear(year, t1, t2, a2)
            val yt1t3a1Hash = getHashWithYear(year, t1, t3, a1)
            val yt1t3a2Hash = getHashWithYear(year, t1, t3, a2)
            val yt1t4a1Hash = getHashWithYear(year, t1, t4, a1)
            val yt1t4a2Hash = getHashWithYear(year, t1, t4, a2)

            val yt2t3a1Hash = getHashWithYear(year, t2, t3, a1)
            val yt2t3a2Hash = getHashWithYear(year, t2, t3, a2)
            val yt2t4a1Hash = getHashWithYear(year, t2, t4, a1)
            val yt2t4a2Hash = getHashWithYear(year, t2, t4, a2)

            val yt3t4a1Hash = getHashWithYear(year, t3, t4, a1)
            val yt3t4a2Hash = getHashWithYear(year, t3, t4, a2)

            yt1t2a1Map.put(yt1t2a1Hash, lite)
            yt1t2a2Map.put(yt1t2a2Hash, lite)
            yt1t3a1Map.put(yt1t3a1Hash, lite)
            yt1t3a2Map.put(yt1t3a2Hash, lite)
            yt1t4a1Map.put(yt1t4a1Hash, lite)
            yt1t4a2Map.put(yt1t4a2Hash, lite)
            yt2t3a1Map.put(yt2t3a1Hash, lite)
            yt2t3a2Map.put(yt2t3a2Hash, lite)
            yt2t4a1Map.put(yt2t4a1Hash, lite)
            yt2t4a2Map.put(yt2t4a2Hash, lite)
            yt3t4a1Map.put(yt3t4a1Hash, lite)
            yt3t4a2Map.put(yt3t4a2Hash, lite)
        }

        t1t2a1Map.put(t1t2a1Hash, lite)
        t1t2a2Map.put(t1t2a2Hash, lite)
        t1t3a1Map.put(t1t3a1Hash, lite)
        t1t3a2Map.put(t1t3a2Hash, lite)
        t1t4a1Map.put(t1t4a1Hash, lite)
        t1t4a2Map.put(t1t4a2Hash, lite)
        t2t3a1Map.put(t2t3a1Hash, lite)
        t2t3a2Map.put(t2t3a2Hash, lite)
        t2t4a1Map.put(t2t4a1Hash, lite)
        t2t4a2Map.put(t2t4a2Hash, lite)
        t3t4a1Map.put(t3t4a1Hash, lite)
        t3t4a2Map.put(t3t4a2Hash, lite)

    }

    override fun update(enrichedDocument: EnrichedDocument) {
        //TODO
    }

    override fun getById(id: Long): EnrichedDocument? {
        //TODO
        return null
    }

    companion object {

        private val BATCH_SIZE = 10000
    }
}
