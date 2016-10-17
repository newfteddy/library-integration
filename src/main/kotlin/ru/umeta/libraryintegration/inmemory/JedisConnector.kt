package ru.umeta.libraryintegration.inmemory

import org.springframework.stereotype.Component
import redis.clients.jedis.Jedis

/**
 * Created by k.kosolapov on 3/8/2016.
 */
@Component
open class JedisConnector {
    internal var jedis = Jedis("localhost", 6379, 20000)
}
