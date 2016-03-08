package ru.umeta.libraryintegration.inmemory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import redis.clients.jedis.Jedis
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
        val id = stringIncr()
        jedis.set("string:$id", string)
    }

    fun getString(id: Int): String {
        return jedis.get("string:$id")
    }

    fun addDoc(doc: EnrichedDocumentLite) {
        val id = docIncr()
        jedis.set("doc:$id", String(doc.toByteArray()))
    }

    fun getDoc(id: Int): EnrichedDocumentLite {
        return EnrichedDocumentLite.fromByteArray(id, jedis.get("doc:$id").toByteArray());
    }

}
