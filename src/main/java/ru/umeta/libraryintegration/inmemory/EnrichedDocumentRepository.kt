package ru.umeta.libraryintegration.inmemory

import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import ru.umeta.libraryintegration.fs.EnrichedDocumentFsPersister
import ru.umeta.libraryintegration.model.Document
import ru.umeta.libraryintegration.model.EnrichedDocument
import ru.umeta.libraryintegration.model.EnrichedDocumentLite
import ru.umeta.libraryintegration.model.StringHash
import ru.umeta.libraryintegration.service.StringHashService
import java.util.*

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
object EnrichedDocumentRepository : IEnrichedDocumentRepository, AutoCloseable {

    private val BATCH_SIZE = 10000

    internal val fsPersister = EnrichedDocumentFsPersister;
    internal val stringHashService = StringHashService;
    internal val list = ArrayList<EnrichedDocumentLite>()
    //internal var isbnMap: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap.create<Int, EnrichedDocumentLite>()

    //no year maps
    internal var t1t2a1a2Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()
    internal var t1t2a1a3Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()
    internal var t1t2a1a4Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()
    internal var t1t2a2a3Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()
    internal var t1t2a2a4Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()
    internal var t1t2a3a4Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()

    internal var t1t3a1a2Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()
    internal var t1t3a1a3Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()
    internal var t1t3a1a4Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()
    internal var t1t3a2a3Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()
    internal var t1t3a2a4Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()
    internal var t1t3a3a4Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()

    internal var t1t4a1a2Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()
    internal var t1t4a1a3Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()
    internal var t1t4a1a4Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()
    internal var t1t4a2a3Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()
    internal var t1t4a2a4Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()
    internal var t1t4a3a4Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()


    internal var t2t3a1a2Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()
    internal var t2t3a1a3Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()
    internal var t2t3a1a4Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()
    internal var t2t3a2a3Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()
    internal var t2t3a2a4Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()
    internal var t2t3a3a4Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()

    internal var t2t4a1a2Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()
    internal var t2t4a1a3Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()
    internal var t2t4a1a4Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()
    internal var t2t4a2a3Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()
    internal var t2t4a2a4Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()
    internal var t2t4a3a4Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()

    internal var t3t4a1a2Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()
    internal var t3t4a1a3Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()
    internal var t3t4a1a4Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()
    internal var t3t4a2a3Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()
    internal var t3t4a2a4Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()
    internal var t3t4a3a4Map: Multimap<Int, EnrichedDocumentLite> = ArrayListMultimap
            .create<Int, EnrichedDocumentLite>()


    private var identity = 0L

    init {
        while (StringHashRepository.isInit == false) {
            (this as java.lang.Object).wait(10)
        }
        val lastId = fsPersister.applyToPersisted { enrichedDocument: EnrichedDocument -> this.putIntoMaps(enrichedDocument) }
        identity = lastId + 1
    }

    override fun getNearDuplicates(document: Document): List<EnrichedDocumentLite> {
//        val author = document.author
//        val title = document.title
//
//        val a1 = author.hashPart1()
//        val a2 = author.hashPart2()
//
//        val t1 = title.hashPart1()
//        val t2 = title.hashPart2()
//        val t3 = title.hashPart3()
//        val t4 = title.hashPart4()
//
//        val yt1t2a1Hash = getHashWithoutYear(t1, t2, a1, a2)
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
    }

    override fun getNearDuplicates(document: EnrichedDocumentLite): List<EnrichedDocumentLite> {
        val authorId = document.authorId
        val titleId = document.titleId

        val author = stringHashService.getById(authorId)
        val title = stringHashService.getById(titleId)

        val a1 = author.hashPart1()
        val a2 = author.hashPart2()
        val a3 = author.hashPart3()
        val a4 = author.hashPart4()

        val t1 = title.hashPart1()
        val t2 = title.hashPart2()
        val t3 = title.hashPart3()
        val t4 = title.hashPart4()

        val t1t2a1a2Hash = getHashWithoutYear(t1, t2, a1, a2)
        val t1t2a1a3Hash = getHashWithoutYear(t1, t2, a1, a3)
        val t1t2a1a4Hash = getHashWithoutYear(t1, t2, a1, a4)
        val t1t2a2a3Hash = getHashWithoutYear(t1, t2, a2, a3)
        val t1t2a2a4Hash = getHashWithoutYear(t1, t2, a2, a4)
        val t1t2a3a4Hash = getHashWithoutYear(t1, t2, a3, a4)

        val t1t3a1a2Hash = getHashWithoutYear(t1, t3, a1, a2)
        val t1t3a1a3Hash = getHashWithoutYear(t1, t3, a1, a3)
        val t1t3a1a4Hash = getHashWithoutYear(t1, t3, a1, a4)
        val t1t3a2a3Hash = getHashWithoutYear(t1, t3, a2, a3)
        val t1t3a2a4Hash = getHashWithoutYear(t1, t3, a2, a4)
        val t1t3a3a4Hash = getHashWithoutYear(t1, t3, a3, a4)

        val t1t4a1a2Hash = getHashWithoutYear(t1, t4, a1, a2)
        val t1t4a1a3Hash = getHashWithoutYear(t1, t4, a1, a3)
        val t1t4a1a4Hash = getHashWithoutYear(t1, t4, a1, a4)
        val t1t4a2a3Hash = getHashWithoutYear(t1, t4, a2, a3)
        val t1t4a2a4Hash = getHashWithoutYear(t1, t4, a2, a4)
        val t1t4a3a4Hash = getHashWithoutYear(t1, t4, a3, a4)

        val t2t3a1a2Hash = getHashWithoutYear(t2, t3, a1, a2)
        val t2t3a1a3Hash = getHashWithoutYear(t2, t3, a1, a3)
        val t2t3a1a4Hash = getHashWithoutYear(t2, t3, a1, a4)
        val t2t3a2a3Hash = getHashWithoutYear(t2, t3, a2, a3)
        val t2t3a2a4Hash = getHashWithoutYear(t2, t3, a2, a4)
        val t2t3a3a4Hash = getHashWithoutYear(t2, t3, a3, a4)

        val t2t4a1a2Hash = getHashWithoutYear(t2, t4, a1, a2)
        val t2t4a1a3Hash = getHashWithoutYear(t2, t4, a1, a3)
        val t2t4a1a4Hash = getHashWithoutYear(t2, t4, a1, a4)
        val t2t4a2a3Hash = getHashWithoutYear(t2, t4, a2, a3)
        val t2t4a2a4Hash = getHashWithoutYear(t2, t4, a2, a4)
        val t2t4a3a4Hash = getHashWithoutYear(t2, t4, a3, a4)

        val t3t4a1a2Hash = getHashWithoutYear(t3, t4, a1, a2)
        val t3t4a1a3Hash = getHashWithoutYear(t3, t4, a1, a3)
        val t3t4a1a4Hash = getHashWithoutYear(t3, t4, a1, a4)
        val t3t4a2a3Hash = getHashWithoutYear(t3, t4, a2, a3)
        val t3t4a2a4Hash = getHashWithoutYear(t3, t4, a2, a4)
        val t3t4a3a4Hash = getHashWithoutYear(t3, t4, a3, a4)

        val result = ArrayList<EnrichedDocumentLite>()

        result.addAll(t1t2a1a2Map.get(t1t2a1a2Hash))
        result.addAll(t1t2a1a3Map.get(t1t2a1a3Hash))
        result.addAll(t1t2a1a4Map.get(t1t2a1a4Hash))
        result.addAll(t1t2a2a3Map.get(t1t2a2a3Hash))
        result.addAll(t1t2a2a4Map.get(t1t2a2a4Hash))
        result.addAll(t1t2a3a4Map.get(t1t2a3a4Hash))

        result.addAll(t1t3a1a2Map.get(t1t3a1a2Hash))
        result.addAll(t1t3a1a3Map.get(t1t3a1a3Hash))
        result.addAll(t1t3a1a4Map.get(t1t3a1a4Hash))
        result.addAll(t1t3a2a3Map.get(t1t3a2a3Hash))
        result.addAll(t1t3a2a4Map.get(t1t3a2a4Hash))
        result.addAll(t1t3a3a4Map.get(t1t3a3a4Hash))

        result.addAll(t1t4a1a2Map.get(t1t4a1a2Hash))
        result.addAll(t1t4a1a3Map.get(t1t4a1a3Hash))
        result.addAll(t1t4a1a4Map.get(t1t4a1a4Hash))
        result.addAll(t1t4a2a3Map.get(t1t4a2a3Hash))
        result.addAll(t1t4a2a4Map.get(t1t4a2a4Hash))
        result.addAll(t1t4a3a4Map.get(t1t4a3a4Hash))

        result.addAll(t2t3a1a2Map.get(t2t3a1a2Hash))
        result.addAll(t2t3a1a3Map.get(t2t3a1a3Hash))
        result.addAll(t2t3a1a4Map.get(t2t3a1a4Hash))
        result.addAll(t2t3a2a3Map.get(t2t3a2a3Hash))
        result.addAll(t2t3a2a4Map.get(t2t3a2a4Hash))
        result.addAll(t2t3a3a4Map.get(t2t3a3a4Hash))

        result.addAll(t1t3a1a2Map.get(t1t3a1a2Hash))
        result.addAll(t1t3a1a3Map.get(t1t3a1a3Hash))
        result.addAll(t1t3a1a4Map.get(t1t3a1a4Hash))
        result.addAll(t1t3a2a3Map.get(t1t3a2a3Hash))
        result.addAll(t1t3a2a4Map.get(t1t3a2a4Hash))
        result.addAll(t1t3a3a4Map.get(t1t3a3a4Hash))

        return result.distinct().toList();
    }

    private fun getHashWithoutYear(hash1: Byte, hash2: Byte, hash3: Byte, hash4: Byte): Int {
        //shift is of the size of a byte
        val shift = 8
        var result = hash1.toInt()
        result = (result shl shift) + hash2.toInt()
        result = (result shl shift) + hash3.toInt()
        result = (result shl shift) + hash4.toInt()
        return result
    }

    override fun getNearDuplicatesWithIsbn(document: Document): List<EnrichedDocumentLite> {
        return emptyList()
        //return isbnMap.get(document.isbn?.hashCode()).toList();
    }

    override fun getNearDuplicatesWithNullIsbn(document: Document): List<EnrichedDocumentLite> {
        val nearDuplicates = getNearDuplicates(document)
        return nearDuplicates.filter(EnrichedDocumentLite::isbnIsNull);
    }

    override fun getNearDuplicatesWithPublishYear(document: Document): List<EnrichedDocumentLite> {
        return emptyList()
//        val author = document.author
//        val title = document.title
//        val year = document.publishYear
//
//        val a1 = author.hashPart1()
//        val a2 = author.hashPart2()
//
//        val t1 = title.hashPart1()
//        val t2 = title.hashPart2()
//        val t3 = title.hashPart3()
//        val t4 = title.hashPart4()
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
    }

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
        val authorHash: StringHash
        val titleHash: StringHash
        val year: Int?
        val lite: EnrichedDocumentLite
        try {
            val id = enrichedDocument.id
            val isbn = enrichedDocument.isbn
            val author = enrichedDocument.author
            val title = enrichedDocument.title
            lite = EnrichedDocumentLite(id, author, title)
            if (isbn != null) {
                lite.nullIsbn = false
                //isbnMap.put(isbn.hashCode(), lite)
            }

            authorHash = stringHashService.getById(author)
            titleHash = stringHashService.getById(title)
            year = enrichedDocument.publishYear
        } catch (e: RuntimeException) {
            e.printStackTrace()
            return
        }

        val a1 = authorHash.hashPart1()
        val a2 = authorHash.hashPart2()
        val a3 = authorHash.hashPart3()
        val a4 = authorHash.hashPart4()

        val t1 = titleHash.hashPart1()
        val t2 = titleHash.hashPart2()
        val t3 = titleHash.hashPart3()
        val t4 = titleHash.hashPart4()

        val t1t2a1a2Hash = getHashWithoutYear(t1, t2, a1, a2)
        val t1t2a1a3Hash = getHashWithoutYear(t1, t2, a1, a3)
        val t1t2a1a4Hash = getHashWithoutYear(t1, t2, a1, a4)
        val t1t2a2a3Hash = getHashWithoutYear(t1, t2, a2, a3)
        val t1t2a2a4Hash = getHashWithoutYear(t1, t2, a2, a4)
        val t1t2a3a4Hash = getHashWithoutYear(t1, t2, a3, a4)

        val t1t3a1a2Hash = getHashWithoutYear(t1, t3, a1, a2)
        val t1t3a1a3Hash = getHashWithoutYear(t1, t3, a1, a3)
        val t1t3a1a4Hash = getHashWithoutYear(t1, t3, a1, a4)
        val t1t3a2a3Hash = getHashWithoutYear(t1, t3, a2, a3)
        val t1t3a2a4Hash = getHashWithoutYear(t1, t3, a2, a4)
        val t1t3a3a4Hash = getHashWithoutYear(t1, t3, a3, a4)

        val t1t4a1a2Hash = getHashWithoutYear(t1, t4, a1, a2)
        val t1t4a1a3Hash = getHashWithoutYear(t1, t4, a1, a3)
        val t1t4a1a4Hash = getHashWithoutYear(t1, t4, a1, a4)
        val t1t4a2a3Hash = getHashWithoutYear(t1, t4, a2, a3)
        val t1t4a2a4Hash = getHashWithoutYear(t1, t4, a2, a4)
        val t1t4a3a4Hash = getHashWithoutYear(t1, t4, a3, a4)

        val t2t3a1a2Hash = getHashWithoutYear(t2, t3, a1, a2)
        val t2t3a1a3Hash = getHashWithoutYear(t2, t3, a1, a3)
        val t2t3a1a4Hash = getHashWithoutYear(t2, t3, a1, a4)
        val t2t3a2a3Hash = getHashWithoutYear(t2, t3, a2, a3)
        val t2t3a2a4Hash = getHashWithoutYear(t2, t3, a2, a4)
        val t2t3a3a4Hash = getHashWithoutYear(t2, t3, a3, a4)

        val t2t4a1a2Hash = getHashWithoutYear(t2, t4, a1, a2)
        val t2t4a1a3Hash = getHashWithoutYear(t2, t4, a1, a3)
        val t2t4a1a4Hash = getHashWithoutYear(t2, t4, a1, a4)
        val t2t4a2a3Hash = getHashWithoutYear(t2, t4, a2, a3)
        val t2t4a2a4Hash = getHashWithoutYear(t2, t4, a2, a4)
        val t2t4a3a4Hash = getHashWithoutYear(t2, t4, a3, a4)

        val t3t4a1a2Hash = getHashWithoutYear(t3, t4, a1, a2)
        val t3t4a1a3Hash = getHashWithoutYear(t3, t4, a1, a3)
        val t3t4a1a4Hash = getHashWithoutYear(t3, t4, a1, a4)
        val t3t4a2a3Hash = getHashWithoutYear(t3, t4, a2, a3)
        val t3t4a2a4Hash = getHashWithoutYear(t3, t4, a2, a4)
        val t3t4a3a4Hash = getHashWithoutYear(t3, t4, a3, a4)

        t1t2a1a2Map.put(t1t2a1a2Hash, lite)
        t1t2a1a3Map.put(t1t2a1a3Hash, lite)
        t1t2a1a4Map.put(t1t2a1a4Hash, lite)
        t1t2a2a3Map.put(t1t2a2a3Hash, lite)
        t1t2a2a4Map.put(t1t2a2a4Hash, lite)
        t1t2a3a4Map.put(t1t2a3a4Hash, lite)

        t1t3a1a2Map.put(t1t3a1a2Hash, lite)
        t1t3a1a3Map.put(t1t3a1a3Hash, lite)
        t1t3a1a4Map.put(t1t3a1a4Hash, lite)
        t1t3a2a3Map.put(t1t3a2a3Hash, lite)
        t1t3a2a4Map.put(t1t3a2a4Hash, lite)
        t1t3a3a4Map.put(t1t3a3a4Hash, lite)

        t1t4a1a2Map.put(t1t4a1a2Hash, lite)
        t1t4a1a3Map.put(t1t4a1a3Hash, lite)
        t1t4a1a4Map.put(t1t4a1a4Hash, lite)
        t1t4a2a3Map.put(t1t4a2a3Hash, lite)
        t1t4a2a4Map.put(t1t4a2a4Hash, lite)
        t1t4a3a4Map.put(t1t4a3a4Hash, lite)

        t2t3a1a2Map.put(t2t3a1a2Hash, lite)
        t2t3a1a3Map.put(t2t3a1a3Hash, lite)
        t2t3a1a4Map.put(t2t3a1a4Hash, lite)
        t2t3a2a3Map.put(t2t3a2a3Hash, lite)
        t2t3a2a4Map.put(t2t3a2a4Hash, lite)
        t2t3a3a4Map.put(t2t3a3a4Hash, lite)

        t2t4a1a2Map.put(t2t4a1a2Hash, lite)
        t2t4a1a3Map.put(t2t4a1a3Hash, lite)
        t2t4a1a4Map.put(t2t4a1a4Hash, lite)
        t2t4a2a3Map.put(t2t4a2a3Hash, lite)
        t2t4a2a4Map.put(t2t4a2a4Hash, lite)
        t2t4a3a4Map.put(t2t4a3a4Hash, lite)

        t3t4a1a2Map.put(t3t4a1a2Hash, lite)
        t3t4a1a3Map.put(t3t4a1a3Hash, lite)
        t3t4a1a4Map.put(t3t4a1a4Hash, lite)
        t3t4a2a3Map.put(t3t4a2a3Hash, lite)
        t3t4a2a4Map.put(t3t4a2a4Hash, lite)
        t3t4a3a4Map.put(t3t4a3a4Hash, lite)

        list.add(lite)
    }

    override fun update(enrichedDocument: EnrichedDocument) {
        //TODO
    }

    override fun getById(id: Long): EnrichedDocument? {
        //TODO
        return null
    }

    override fun close() {
        fsPersister.close()
    }

}
