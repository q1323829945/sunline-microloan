package cn.sunline.saas.snowflake

import cn.sunline.saas.redis.configs.RedisConfig
import cn.sunline.saas.redis.services.RedisClient
import cn.sunline.saas.snowflake.config.InitSnowflakeParamsConfig
import cn.sunline.saas.snowflake.services.SnowflakeService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.ContextConfiguration

@SpringBootApplication
//@ContextConfiguration(classes = [RedisClient::class, InitSnowflakeParamsConfig::class, SnowflakeService::class, RedisConfig::class])
class SnowflakeApplication

fun main(args: Array<String>) {
    runApplication<SnowflakeApplication>(*args)
}