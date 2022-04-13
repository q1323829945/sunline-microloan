package cn.sunline.saas.seq.snowflake.simple.service

import org.springframework.stereotype.Service

import cn.sunline.saas.seq.Sequence
import kotlin.random.Random

@Service
class SnowflakeSimpleService:Sequence {
    var seq = 0L


    @Synchronized
    override fun nextId(): Long {
        var currentTime = timeGen()
        val random = Random.nextLong(4096) + 1
        seq = (seq + 1) and (-1L xor (-1L shl 10))

        if(seq == 0L){
            currentTime = tilNextMillis(currentTime)
        }

        return (currentTime shl 22) or
                (random shl 10) or
                seq
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
