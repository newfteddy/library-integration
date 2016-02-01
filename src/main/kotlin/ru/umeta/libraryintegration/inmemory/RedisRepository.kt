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
import ru.umeta.libraryintegration.model.StringHash
import ru.umeta.libraryintegration.model.StringHashLite

/**
 * Created by ctash on 29.01.16.
 */
@Component
public class RedisRepository {

    private val stringTemplate: StringRedisTemplate
    private val setTemplate: RedisTemplate<String, String>

    private val valueOps: ValueOperations<String, String>
    private val setOps: ZSetOperations<String, String>

    private val stringHashIdCounter: RedisAtomicLong
    private val documentIdCounter: RedisAtomicLong

    private val stringHashList: RedisList<String>
    private val documentList: RedisList<String>

    private val postMapper = DecoratingStringHashMapper<StringHashLite>(
            JacksonHashMapper<StringHashLite>(StringHashLite::class.java))

    @Autowired
    constructor(stringTemplate: StringRedisTemplate, setTemplate: RedisTemplate<String, String>) {
        this.stringTemplate = stringTemplate
        this.setTemplate = setTemplate
        valueOps = stringTemplate.opsForValue()
        setOps = setTemplate.opsForZSet()

        stringHashList = DefaultRedisList<String>("stringHash", stringTemplate)
        documentList = DefaultRedisList<String>("documentList", stringTemplate)
        stringHashIdCounter = RedisAtomicLong("global:shId", stringTemplate.connectionFactory)
        documentIdCounter = RedisAtomicLong("global:dId", stringTemplate.connectionFactory)
    }

    fun addStringHash(stringHash: StringHash) {
        val tokens = stringHash.tokens
        val stringHashLite = StringHashLite(-1, stringHash.hashCode, stringHash.simHash)
        val id = stringHashIdCounter.incrementAndGet()
        val idAsString = java.lang.String.valueOf(id)
        stringHashLite.id = id
        stringHash(idAsString).putAll(postMapper.toHash(stringHashLite))
        setOps.add("stringHash:tokens:id:$id", DefaultTypedTuple<String>())

        stringHashList.addLast(idAsString)
    }

    private fun stringHash(id: String): RedisMap<String, String> {
        return DefaultRedisMap("stringHash:id:$id", stringTemplate)
    }
}