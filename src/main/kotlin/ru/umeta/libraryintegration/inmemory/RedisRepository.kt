package ru.umeta.libraryintegration.inmemory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.SetOperations
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
import ru.umeta.libraryintegration.model.StringHashLite

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

    private val stringHashList: RedisList<String>
    private val documentList: RedisList<String>

    private val postMapper = DecoratingStringHashMapper<StringHashLite>(
            JacksonHashMapper<StringHashLite>(StringHashLite::class.java))

    @Autowired
    constructor(stringTemplate: StringRedisTemplate) {
        this.stringTemplate = stringTemplate
        valueOps = stringTemplate.opsForValue()
        setOps = stringTemplate.opsForSet()

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
        setOps.add(stringHashTokensKey(idAsString), *tokens.toTypedArray())
        valueOps.set("stringHash:hash:${stringHash.hashCode}", idAsString)
        stringHashList.addLast(idAsString)
    }

    private fun stringHashTokensKey(id: String) = "stringHash:tokens:id:$id"

    private fun stringHash(id: String): RedisMap<String, String> {
        return DefaultRedisMap(stringHashKey(id), stringTemplate)
    }

    private fun stringHashKey(id: String) = "stringHash:id:$id"

    fun getStringHashByHashCode(hashCode: Int): StringHash? {
        val id: String? = valueOps.get("stringHash:hash:$hashCode")
        if (id != null) {
            val boundHashOps = stringTemplate.boundHashOps<String, String>(stringHashKey(id))
            val simHash = boundHashOps.get("simHash").toInt()
            val setOps = stringTemplate.boundSetOps(stringHashTokensKey(id))
            val tokens = setOps.members() ?: emptySet<String>()
            return StringHash(id.toLong(), hashCode, simHash, tokens)
        } else {
            return null
        }

    }
}