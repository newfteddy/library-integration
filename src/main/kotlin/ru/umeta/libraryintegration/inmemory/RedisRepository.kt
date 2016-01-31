package ru.umeta.libraryintegration.inmemory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.BoundHashOperations
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.data.redis.hash.DecoratingStringHashMapper
import org.springframework.data.redis.hash.JacksonHashMapper
import org.springframework.data.redis.support.atomic.RedisAtomicLong
import org.springframework.data.redis.support.collections.DefaultRedisList
import org.springframework.data.redis.support.collections.DefaultRedisMap
import org.springframework.data.redis.support.collections.RedisList
import org.springframework.data.redis.support.collections.RedisMap
import org.springframework.stereotype.Component
import ru.umeta.libraryintegration.model.StringHash

/**
 * Created by ctash on 29.01.16.
 */
@Component
public class RedisRepository {

    private val template: StringRedisTemplate

    private val valueOps: ValueOperations<String, String>

    private val stringHashIdCounter: RedisAtomicLong
    private val documentIdCounter: RedisAtomicLong

    private val stringHashList: RedisList<String>
    private val documentList: RedisList<String>

    private val postMapper = DecoratingStringHashMapper<StringHash>(
            JacksonHashMapper<StringHash>(StringHash::class.java))

    @Autowired
    constructor(template: StringRedisTemplate) {
        this.template = template
        valueOps = template.opsForValue()

        stringHashList = DefaultRedisList<String>("stringHash", template)
        documentList = DefaultRedisList<String>("documentList", template)
        stringHashIdCounter = RedisAtomicLong("global:shId", template.connectionFactory)
        documentIdCounter = RedisAtomicLong("global:dId", template.connectionFactory)
    }

    fun addStringHash(stringHash: StringHash) {
        val id = stringHashIdCounter.incrementAndGet()
        val idAsString = java.lang.String.valueOf(id)
        stringHash.id = id
        stringHash(idAsString).putAll(postMapper.toHash(stringHash))
        stringHashList.addLast(idAsString)
    }

    private fun stringHash(id: String): RedisMap<String, String> {
        return DefaultRedisMap(":shId$id", template)
    }
}