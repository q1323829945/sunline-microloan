package cn.sunline.saas.redis.configs

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "redis")
class RedisConfig(

){
    @Value("\${redis.server}")
    lateinit var server: String
    @Value("\${redis.useCluster}")
    var useCluster: Boolean = false
    @Value("\${redis.useSentinel}")
    var useSentinel: Boolean = false
    @Value("\${redis.password}")
    lateinit var password: String
}