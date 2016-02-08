package ru.umeta.libraryintegration.inmemory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.*
import org.springframework.data.redis.hash.DecoratingStringHashMapper
import org.springframework.data.redis.hash.JacksonHashMapper
import org.springframework.data.redis.support.atomic.RedisAtomicLong
import org.springframework.data.redis.support.collections.DefaultRedisList
import org.springframework.data.redis.support.collections.DefaultRedisMap
import org.springframework.data.redis.support.collections.RedisList
import org.springframework.data.redis.support.collections.RedisMap
import org.springframework.stereotype.Component
import ru.umeta.libraryintegration.model.EnrichedDocument
import ru.umeta.libraryintegration.model.StringHash

/**
 * Created by ctash on 29.01.16.
 */
@Component
public class RedisRepository {

    private val stringTemplate: StringRedisTemplate
    private val valueOps: ValueOperations<String, String>
    private val setOps: SetOperations<String, String>


    private val stringHashIdCounter: RedisAtomicLong
    private val documentIdCounter: RedisAtomicLong

    private val stringHashMapper = DecoratingStringHashMapper<StringHash>(
            JacksonHashMapper<StringHash>(StringHash::class.java))
    private val documentMapper = DecoratingStringHashMapper<EnrichedDocument>(
            JacksonHashMapper<EnrichedDocument>(EnrichedDocument::class.java))

    private var listOps: ListOperations<String, String>

    @Autowired
    constructor(stringTemplate: StringRedisTemplate) {
        this.stringTemplate = stringTemplate
        valueOps = stringTemplate.opsForValue()
        setOps = stringTemplate.opsForSet()
        listOps = stringTemplate.opsForList()

        stringHashIdCounter = RedisAtomicLong("global:shId", stringTemplate.connectionFactory)
        documentIdCounter = RedisAtomicLong("global:dId", stringTemplate.connectionFactory)
    }

    fun addStringHash(stringHash: StringHash) {
        val id = stringHashIdCounter.incrementAndGet()
        val idAsString = java.lang.String.valueOf(id)
        stringHash.id = id
        stringHash(idAsString).putAll(stringHashMapper.toHash(stringHash))
        valueOps.set("stringHash:hash:${stringHash.hashCode}", idAsString)
    }

    private fun stringHash(id: String): RedisMap<String, String> {
        return DefaultRedisMap(stringHashKey(id), stringTemplate)
    }

    private fun stringHashKey(id: String) = "stringHash:id:$id"

    fun getStringHashByHashCode(hashCode: Int): StringHash? {
        val id: String? = valueOps.get("stringHash:hash:$hashCode")
        if (id != null) {
            val boundHashOps = stringTemplate.boundHashOps<String, String>(stringHashKey(id))
            val simHash = boundHashOps.get("simHash").toInt()
            val value = boundHashOps.get("value")
            return StringHash(id.toLong(), hashCode, simHash, value)
        } else {
            return null
        }

    }

    fun saveDocument(document: EnrichedDocument) {
        val id = documentIdCounter.incrementAndGet()
        document.id = id
        document(id.toString()).putAll(documentMapper.toHash(document))
    }

    private fun document(id: String): RedisMap<String, String> {
        return DefaultRedisMap(documentKey(id), stringTemplate)
    }

    private fun documentKey(id: String) = "document:id:$id"

    fun getStringHashById(idAsLong: Long): StringHash {
        val id = idAsLong.toString()
        val boundHashOps = stringTemplate.boundHashOps<String, String>(stringHashKey(id))
        val simHash = boundHashOps.get("simHash").toInt()
        val value = boundHashOps.get("value")
        val hashCode = boundHashOps.get("hashCode").toInt()
        return StringHash(idAsLong, hashCode, simHash, value)
    }

    fun addHashLinkToDocument(ti: Int, tj: Int, ai: Int, aj: Int, id: Long, value: Int) {
        DefaultRedisList(hashLink(ai, aj, ti, tj, value), stringTemplate).add(id.toString())
    }

    private fun hashLink(ai: Int, aj: Int, ti: Int, tj: Int, value: Int) = "document:hash:$ti$tj$ai$aj:$value"
}