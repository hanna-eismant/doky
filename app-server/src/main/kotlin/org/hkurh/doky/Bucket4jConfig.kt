package org.hkurh.doky

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import java.time.Duration

@Configuration
class Bucket4jConfig {

    @Bean
    fun jedisPool(@Value("\${spring.data.redis.port}") port: String): JedisPool {
        val poolConfig = buildPoolConfig()
        return JedisPool(poolConfig, "localhost", port.toInt())
    }

    private fun buildPoolConfig(): JedisPoolConfig {
        val poolConfig = JedisPoolConfig()
        poolConfig.maxTotal = 128
        poolConfig.maxIdle = 128
        poolConfig.minIdle = 16
        poolConfig.testOnBorrow = true
        poolConfig.testOnReturn = true
        poolConfig.testWhileIdle = true
        poolConfig.minEvictableIdleDuration = Duration.ofSeconds(60)
        poolConfig.timeBetweenEvictionRuns = Duration.ofSeconds(30)
        poolConfig.numTestsPerEvictionRun = 3
        poolConfig.blockWhenExhausted = true
        return poolConfig
    }
}
