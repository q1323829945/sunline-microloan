package cn.sunline.saas.seq.snowflake.config

import cn.sunline.saas.redis.configs.RedisConfig
import org.springframework.boot.test.context.TestConfiguration
import redis.embedded.RedisServer
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@TestConfiguration
class RedisConfigTest {
    private var redisServer: RedisServer

    constructor(redisConfig: RedisConfig) {
        val indexes = redisConfig.server.indexOf(":") + 1
        val port = redisConfig.server.substring(indexes, indexes+4).toInt()
        this.redisServer = RedisServer.builder().port(port).setting("maxheap 25600000").build()
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