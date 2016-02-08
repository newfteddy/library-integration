package ru.umeta.libraryintegration.configuration

import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created by ctash on 08.02.16.
 */
@Configuration
open class RedisConfiguration {

    @Bean(name = arrayOf("org.springframework.autoconfigure.redis.RedisProperties"))
    open fun redisProperties(): RedisProperties {
        val redisProperties = RedisProperties()
        redisProperties.timeout = 600000
        return redisProperties
    }

}