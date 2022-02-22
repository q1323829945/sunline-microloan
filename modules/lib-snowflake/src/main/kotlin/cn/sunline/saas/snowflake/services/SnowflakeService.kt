package cn.sunline.saas.snowflake.services

import cn.sunline.saas.redis.services.RedisClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


const val INIT_TIMESTAMP:Long = 1645414066687L

const val WORKER_ID_BITS:Int = 10
const val SEQUENCE_BITS:Int = 12

const val MAX_WORKER:Long = -1L xor (-1L shl WORKER_ID_BITS)
const val MAX_SEQUENCE:Long = -1L xor (-1L shl SEQUENCE_BITS)

const val WORKER_ID_SHIFT = SEQUENCE_BITS
const val TIMESTAMP_SHIFT = WORKER_ID_BITS + WORKER_ID_SHIFT

const val SNOWFLAKE_REDIS_HASH = "snowflake_hash_map"
const val LAST_TIMESTAMP_REDIS_KEY = "last_timestamp_key_map"
const val SEQUENCE_REDIS_KEY = "sequence_key_map"
const val WORKER_ID_REDIS_KEY = "work_id_key_map"

@Service
class SnowflakeService {

    @Autowired
    private lateinit var redisClient: RedisClient

    private var workId: Long = -1
    private var sequence: Long = 0
    private var lastTimestamp: Long = 0


    private fun getWorkId():Long{
        if(workId == -1L){
            workId = redisClient.getMapItem<Long>(SNOWFLAKE_REDIS_HASH,WORKER_ID_REDIS_KEY)?:0L
            workId ++
            workId = workId and MAX_WORKER

            redisClient.addToMap(SNOWFLAKE_REDIS_HASH, WORKER_ID_REDIS_KEY,workId)
        }
        return workId
    }


//    private fun getLastTimestamp():Long{
//        var lastTimestamp = redisClient.getMapItem<Long>(SNOWFLAKE_REDIS_HASH,LAST_TIMESTAMP_REDIS_KEY)
//
//        if(lastTimestamp == null){
//            lastTimestamp = 0L
//        }
//
//        return lastTimestamp
//    }
//
//    private fun getSequence():Long{
//        var sequence = redisClient.getMapItem<Long>(SNOWFLAKE_REDIS_HASH,SEQUENCE_REDIS_KEY)
//
//        if(sequence == null){
//            sequence = 0L
//
//        }
//
//        return sequence
//    }

    @Synchronized
    fun nextId():Long{
        var timestamp = timeGen()
        val workId = getWorkId()
//        val lastTimestamp = getLastTimestamp()
//        var sequence = getSequence()

        if(timestamp < lastTimestamp)throw Exception("clock error")
        if(lastTimestamp == timestamp){
            sequence = (sequence + 1) and MAX_SEQUENCE //seq in 0 ... MAX_SEQUENCE
            if(sequence == 0L){
                timestamp = tilNextMillis(lastTimestamp)
            }
        }else {
            sequence = 0
        }

        lastTimestamp = timestamp


//        redisClient.addToMap(SNOWFLAKE_REDIS_HASH,LAST_TIMESTAMP_REDIS_KEY,timestamp)
//        redisClient.addToMap(SNOWFLAKE_REDIS_HASH, SEQUENCE_REDIS_KEY,sequence)
        println("timestamp: $timestamp ,init_timestamp: $INIT_TIMESTAMP,timestamp - init_timestamp binary : ${(timestamp - INIT_TIMESTAMP).toString(2)}" )
        println("workId: $workId , binary : ${workId.toString(2) }")
        println("sequence: $sequence , binary : ${sequence.toString(2)}")

        return ((timestamp - INIT_TIMESTAMP) shl TIMESTAMP_SHIFT) or
                (workId shl WORKER_ID_SHIFT) or
                sequence
    }

    private fun tilNextMillis(lastTimestamp:Long):Long{
        var timestamp = timeGen()
        while (timestamp <= lastTimestamp){
            timestamp = timeGen()
        }

        return timestamp
    }

    private fun timeGen():Long{
        return System.currentTimeMillis()
    }
}