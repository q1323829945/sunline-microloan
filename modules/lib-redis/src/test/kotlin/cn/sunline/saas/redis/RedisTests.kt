package cn.sunline.saas.redis

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RedisTests {
    fun main(args: Array<String>) {
        runApplication<RedisTests>(*args)
    }
}