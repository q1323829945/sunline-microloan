package cn.sunline.saas.seq.snowflake.services

import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.seq.snowflake.config.*
import cn.sunline.saas.seq.snowflake.initializer.SnowflakeInitialize
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class SnowflakeService(private val snowflakeInitialize: SnowflakeInitialize) : Sequence {
    private var sequence: Long = 0
    private var lastTimestamp: Long = 0
    private var clockBack: Long = 0
    lateinit var snowflakeConfig: SnowflakeConfig

    @PostConstruct
    private fun initialize() {
        snowflakeConfig = snowflakeInitialize.getConfig()
    }

    @Synchronized
    override fun nextId(): Long {
        var timestamp = timeGen()

        if (timestamp < lastTimestamp) {
            if (sequence == 0L) {
                clockBack = (clockBack + 1) and MAX_CLOCK_BACK
            }

            sequence = (sequence + 1) and MAX_SEQUENCE //seq in 0 ... MAX_SEQUENCE

            if (clockBack == 0L) {
                timestamp = tilNextMillis(lastTimestamp)
            }
        } else if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) and MAX_SEQUENCE //seq in 0 ... MAX_SEQUENCE
            if (sequence == 0L) {
                timestamp = tilNextMillis(lastTimestamp)
            }
            clockBack = 0
        } else {
            sequence = 0
            clockBack = 0
        }

        lastTimestamp = timestamp

        return ((timestamp - INIT_TIMESTAMP) shl TIMESTAMP_SHIFT) or
                (snowflakeConfig.workId shl WORKER_ID_SHIFT) or
                (snowflakeConfig.datacenterId shl DATACENTER_ID_SHIFT) or
                (clockBack shl CLOCK_BACK_SHIFT) or
                sequence
    }

    private fun tilNextMillis(lastTimestamp: Long): Long {
        var timestamp = timeGen()
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen()
        }

        return timestamp
    }

    private fun timeGen(): Long {
        return System.currentTimeMillis()
    }
}