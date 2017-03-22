package ru.umeta.libraryintegration.inmemory

import org.eclipse.collections.impl.factory.Lists
import org.eclipse.collections.impl.factory.Maps
import org.eclipse.collections.impl.factory.primitive.IntSets
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.umeta.libraryintegration.model.EnrichedDocument
import ru.umeta.libraryintegration.model.EnrichedDocumentLite
import ru.umeta.libraryintegration.model.StringHash
import ru.umeta.libraryintegration.service.StringHashService
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.nio.ByteBuffer
import javax.annotation.PostConstruct

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
@Component
open class InMemoryRepository @Autowired constructor(
        val stringHashService: StringHashService,
        val redisRepository: RedisRepository) {

    val docStorage = arrayOfNulls<EnrichedDocumentLite>(25000000)
    val docMarked = BooleanArray(25000000, {false})

    val strStorage = Maps.mutable.empty<Int, String>()

    fun getDocCount() = redisRepository.getDocCount()

    internal var maps = SimHashMaps()


    fun fillMapsFromRedisWithTokens() {
        val docCount = redisRepository.getDocCount()
        for (i in 0..docCount) {
            val doc = redisRepository.getDoc(i) ?: continue
            docStorage[i] = doc
            val titleIntHash = doc.titleId
            addTokens(titleIntHash)
            val authorIntHash = doc.authorId
            addTokens(authorIntHash)
            if (i % 1000000 == 0) {
                println("Added $i docs.")
            }
        }
    }


    fun fillMapsFromRedis() {
        val docCount = redisRepository.getDocCount()
        for (i in 0..docCount) {
            val doc = redisRepository.getDoc(i) ?: continue
            docStorage[i] = doc
            val titleHash = StringHash(doc.titleHash)
            val authorHash = StringHash(doc.authorHash)
            val hashes = SimHashes(titleHash, authorHash)
            for (ti in 1..3) {
                for (tj in ti + 1..4) {
                    for (ai in 1..3) {
                        for (aj in ai + 1..4) {
                            val hash = hashes.getByIndex(ti, tj, ai, aj)
                            var list = maps.getOrCreateByIndex(ti, tj, ai, aj).get(hash)
                            if (list == null) {
                                list = IntArrayList()
                                maps.getOrCreateByIndex(ti, tj, ai, aj).put(hash, list)
                            }
                            list.add(doc.id)
                        }
                    }
                }
            }
            if (i % 1000000 == 0) {
                println("Added $i docs.")
            }
        }
    }
    /*correction*/
    fun fillMapsFromRedisNA() {
        val docCount = redisRepository.getDocCount()

        for (i in 0..docCount) {
            val doc = redisRepository.getDoc(i) ?: continue

            docStorage[i] = doc
            val titleHash = StringHash(doc.titleHash)

            val hashes = SimHashes(titleHash)

            for (ti in 1..2) {
                for (tj in ti + 1..3) {
                    for (tk in tj + 1..4) {
                        val hash = hashes.getByIndexNA(ti, tj, tk)
                        var list = maps.getOrCreateByIndexNA(ti, tj, tk).get(hash)
                        if (list == null) {
                            list = IntArrayList()
                            maps.getOrCreateByIndexNA(ti, tj, tk).put(hash, list)
                        }
                        list.add(doc.id)

                    }
                }
            }
            if (i % 1000000 == 0) {
                println("Added $i docs.")
            }
        }
    }
    /*--correction*/

    fun getString(id: Int): String {
        return strStorage[id]!!
    }

    private fun addTokens(intHash: Int) {
        val fromStorage = strStorage[intHash]
        if (fromStorage == null) {
            val string = redisRepository.getString(intHash)
            val tokens = stringHashService.getSimHashTokens(string)
            strStorage[intHash] = string
        }
    }

    fun getNearDuplicates(doc: EnrichedDocumentLite): List<EnrichedDocumentLite> {
        val titleHash = StringHash(doc.titleHash)
        val authorHash = StringHash(doc.authorHash)
        val id = doc.id
        val hashes = SimHashes(titleHash, authorHash)


        val resultIds = IntSets.mutable.empty()
        val docs = Lists.mutable.empty<EnrichedDocumentLite>()
        for (ti in 1..3) {
            for (tj in ti + 1..4) {
                for (ai in 1..3) {
                    for (aj in ai + 1..4) {
                        val hash = hashes.getByIndex(ti, tj, ai, aj)
                        maps.getOrCreateByIndex(ti, tj, ai, aj).get(hash).forEach {
                            if (it > id && resultIds.add(it)) {
                                docs.add(docStorage[it])
                            }
                        }
                    }
                }
            }
        }

        return docs
    }


    /*correction*/
    fun getNearDuplicatesNA(doc: EnrichedDocumentLite): List<EnrichedDocumentLite> {
        val titleHash = StringHash(doc.titleHash)
        val id = doc.id
        val hashes = SimHashes(titleHash)


        val resultIds = IntSets.mutable.empty()
        val docs = Lists.mutable.empty<EnrichedDocumentLite>()
        for (ti in 1..2){
            for (tj in ti + 1..3) {
                for (tk in tj + 1..4) {
                    val hash = hashes.getByIndexNA(ti, tj, tk)
                    maps.getOrCreateByIndexNA(ti, tj, tk).get(hash).forEach {
                        if (it > id && resultIds.add(it)) {
                            docs.add(docStorage[it])
                        }
                    }
                }
            }
        }

        return docs
    }
    /*--correction*/

}
