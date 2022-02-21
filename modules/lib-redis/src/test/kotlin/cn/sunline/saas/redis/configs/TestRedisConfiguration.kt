package cn.sunline.saas.redis.configs

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.stereotype.Component
import redis.embedded.RedisServer
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@TestConfiguration
class TestRedisConfiguration {
    private lateinit var redisServer: RedisServer

    constructor(redisConfig: RedisConfig) {
        val indexes = redisConfig.server.indexOf(":") + 1
        val port = redisConfig.server.substring(indexes, indexes+4).toInt()
        this.redisServer = RedisServer.builder().port(port).build()
        println("[Embedded-Redis] Creating redis server on port $port")
    }

    @PostConstruct
    private fun beforeAll() {
        redisServer.start()
        println("[Embedded-Redis] Starting embedded redis server")
    }

    @PreDestroy
    private fun afterAll() {
        println("[Embedded-Redis] Stopping embedded redis server")
        redisServer.stop()
    }
}