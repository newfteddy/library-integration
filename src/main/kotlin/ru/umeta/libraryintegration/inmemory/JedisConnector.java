package ru.umeta.libraryintegration.inmemory;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * Created by k.kosolapov on 3/8/2016.
 */
@Component
public class JedisConnector {
    Jedis jedis = new Jedis("localhost", 6379, 20000);
}
