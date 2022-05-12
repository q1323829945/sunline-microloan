package cn.sunline.saas.seq.snowflake.initializer.impl

import cn.sunline.saas.redis.services.RedisClient
import cn.sunline.saas.seq.snowflake.config.*
import cn.sunline.saas.seq.snowflake.initializer.SnowflakeInitialize
import org.springframework.stereotype.Component

/**
 * @title: SnowflakeInitializeRedisImpl
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/1 14:28
 */
@Component
class SnowflakeInitializeRedisImpl(private var config: SnowflakeConfig,private var redisClient: RedisClient) :
    SnowflakeInitialize {

    override fun getConfig(): SnowflakeConfig {
        config.workId = getWorkId()
        config.datacenterId = getDatacenterId()
        return config
    }

    private fun getWorkId(): Long {
        var workId = redisClient.getMapItem<Long>(SNOWFLAKE_REDIS_HASH, WORKER_ID_REDIS_KEY) ?: 0L
        workId = (workId + 1) and MAX_WORKER
        redisClient.addToMap(SNOWFLAKE_REDIS_HASH, WORKER_ID_REDIS_KEY, workId)

        return workId
    }

    private fun getDatacenterId(): Long {
        return config.datacenterId.apply {
            if (this > MAX_DATACENTER)
                throw Exception("")
        }
    }
}