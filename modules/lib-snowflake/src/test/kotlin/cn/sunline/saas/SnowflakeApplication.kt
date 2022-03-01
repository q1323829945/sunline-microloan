package cn.sunline.saas

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
//@ContextConfiguration(classes = [RedisClient::class, InitSnowflakeParamsConfig::class, SnowflakeService::class, RedisConfig::class])
class SnowflakeApplication {
    fun main(args: Array<String>) {
        runApplication<SnowflakeApplication>(*args)
    }
}

