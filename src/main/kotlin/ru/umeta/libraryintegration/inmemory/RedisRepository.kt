package ru.umeta.libraryintegration.inmemory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.*
import org.springframework.data.redis.hash.DecoratingStringHashMapper
import org.springframework.data.redis.hash.JacksonHashMapper
import org.springframework.data.redis.support.atomic.RedisAtomicInteger
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


    private val stringIdCounter: RedisAtomicInteger

    @Autowired
    constructor(stringTemplate: StringRedisTemplate) {
        this.stringTemplate = stringTemplate
        valueOps = stringTemplate.opsForValue()

        stringIdCounter = RedisAtomicInteger("global:id", stringTemplate.connectionFactory)
    }

    fun addString(string: String) {
        val id = stringIdCounter.incrementAndGet()
        valueOps.set("string:$id", string)
    }

    fun getString(id: Int): String {
        return valueOps.get("string:$id")
    }

}
