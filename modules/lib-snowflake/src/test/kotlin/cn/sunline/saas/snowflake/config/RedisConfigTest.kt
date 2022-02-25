package cn.sunline.saas.snowflake.config

import org.springframework.boot.test.context.TestConfiguration
import redis.embedded.RedisServer
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@TestConfiguration
class RedisConfigTest {
    private lateinit var redisServer: RedisServer

    @PostConstruct
    private fun start(){
        redisServer = RedisServer.builder().port(6379).build()
        redisServer.start()
    }

    @PreDestroy
    private fun stop(){
        redisServer.stop()
    }
}