package cn.sunline.saas.seq.snowflake.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * @title: SnowflakeConfig
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/1 14:16
 */
@Component
@ConfigurationProperties(prefix = "seq.snowflake")
class SnowflakeConfig(
    var workId: Long = 1,
    var datacenterId: Long = 1
)
