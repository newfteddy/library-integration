package ru.umeta.libraryintegration.inmemory

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

    @Autowired
    constructor(template: StringRedisTemplate) {
        this.template = template
        valueOps = template.opsForValue()

        stringHashList = DefaultRedisList<String>("stringHash", template)
        documentList = DefaultRedisList<String>("documentList", template)
        stringHashIdCounter = RedisAtomicLong("global:shId", template.connectionFactory)
        documentIdCounter = RedisAtomicLong("global:dId", template.connectionFactory)
    }

    fun addStringHash(stringHash: StringHash): String {
        val id = stringHashIdCounter.incrementAndGet().toString()
        val userOps = template.boundHashOps(KeyUtils.uid(uid))
        userOps.put("name", name)
        userOps.put("pass", password)
        valueOps.set(KeyUtils.user(name), uid)

        users.addFirst(name)
        return addAuth(name)
    }

}