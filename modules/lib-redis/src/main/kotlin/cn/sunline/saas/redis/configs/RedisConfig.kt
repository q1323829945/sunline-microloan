package cn.sunline.saas.redis.configs

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "redis")
class RedisConfig(
    var server: String = "127.0.0.1:6379",
    var useCluster: Boolean = false,
    var useSentinel: Boolean = false,
    var password: String = ""
)