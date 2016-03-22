package ru.umeta.libraryintegration.inmemory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import redis.clients.jedis.Jedis
import redis.clients.jedis.Response
import ru.umeta.libraryintegration.model.EnrichedDocumentLite
import java.util.*

/**
 * Created by ctash on 29.01.16.
 */
@Component
public class RedisRepository {

    private val jedis: Jedis


    @Autowired
    constructor(jedisConnector: JedisConnector) {
        jedis = jedisConnector.jedis
    }

    private fun stringIncr() = jedis.incr("global:stringId").toInt()

    private fun docIncr() = jedis.incr("global:docId").toInt()

    fun addString(string: String) {
        val hash = string.hashCode()
        jedis.setnx("string:$hash", string)
    }

    fun getString(hash: Int): String {
        return jedis.get("string:$hash")
    }

    fun addDoc(doc: EnrichedDocumentLite) {
        val id = docIncr()
        jedis.hset("doc:$id", "1", doc.authorToLong().toString())
        jedis.hset("doc:$id", "2", doc.titleToLong().toString())
        jedis.hset("doc:$id", "3", doc.isbnYearToLong().toString())
    }

    fun getDoc(id: Int): EnrichedDocumentLite? {
        try {
            val map = jedis.hgetAll("doc:$id");
            return getDocumentFromMap(id, map)
        } catch (e: NumberFormatException) {
            return null;
        } catch (e: NullPointerException) {
            return null;
        }

    }

    private fun getDocumentFromMap(id: Int, map: Map<String, String>): EnrichedDocumentLite {
        val authorLong = map["1"]!!.toLong()
        val titleLong = map["2"]!!.toLong()
        val isbnYearLong = map["3"]!!.toLong()

        val authorId: Int = (authorLong ushr 32).toInt()
        val authorHash: Int = (authorLong and ((1L shl 32) - 1)).toInt()
        val titleId: Int = (titleLong ushr 32).toInt()
        val titleHash: Int = (titleLong and ((1L shl 32) - 1)).toInt()
        val isbn: Int = (isbnYearLong ushr 32).toInt()
        val year: Int = (isbnYearLong and ((1L shl 32) - 1)).toInt()

        return EnrichedDocumentLite(id, authorId, authorHash, titleId, titleHash, isbn, year, 0.0)
    }

    fun getDocs(ids: Collection<Int>): List<EnrichedDocumentLite> {
        val pipeline = jedis.pipelined()

        val responseResult = ArrayList<ResponseWithId<Map<String, String>>>()
        val result = ArrayList<EnrichedDocumentLite>()
        val idList = ArrayList<Int>()

        ids.forEach {
            val response = pipeline.hgetAll("doc:$it")
            responseResult.add(ResponseWithId(response, it))
            idList.add(it)
        }

        pipeline.sync()

        responseResult.forEach {
            val docMap = it.response.get()
            result.add(getDocumentFromMap(it.id, docMap))
        }

        return result
    }

    data class ResponseWithId<T>(val response: Response<T>, val id: Int)

}
