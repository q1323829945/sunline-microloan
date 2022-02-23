package cn.sunline.saas.snowflake.services

import cn.sunline.saas.snowflake.`interface`.SnowflakeInterface
import cn.sunline.saas.snowflake.config.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SnowflakeService : SnowflakeInterface{
    private var sequence: Long = 0
    private var lastTimestamp: Long = 0
    private var clockBack:Long = 0

    @Autowired
    private lateinit var initSnowflakeParamsConfig: InitSnowflakeParamsConfig

    @Synchronized
    override fun nextId():Long{
        var timestamp = timeGen()
        val workId = initSnowflakeParamsConfig.workId
        val datacenterId = initSnowflakeParamsConfig.datacenterId

        if(timestamp < lastTimestamp) {
            if(sequence == 0L){
                clockBack = (clockBack + 1) and MAX_CLOCK_BACK
            }

            sequence = (sequence + 1) and MAX_SEQUENCE //seq in 0 ... MAX_SEQUENCE

            if(clockBack == 0L){
                timestamp = tilNextMillis(lastTimestamp)
            }
        }else if(lastTimestamp == timestamp){
            sequence = (sequence + 1) and MAX_SEQUENCE //seq in 0 ... MAX_SEQUENCE
            if(sequence == 0L){
                timestamp = tilNextMillis(lastTimestamp)
            }
            clockBack = 0
        }else {
            sequence = 0
            clockBack = 0
        }

        lastTimestamp = timestamp

//        println("timestamp: $timestamp ,init_timestamp: $INIT_TIMESTAMP,timestamp - init_timestamp binary : ${(timestamp - INIT_TIMESTAMP).toString(2)}" )
//        println("workId: $workId , binary : ${workId.toString(2) }")
//        println("datacenterId: $datacenterId , binary : ${datacenterId.toString(2) }")
//        println("clockBack: $clockBack, binary : ${clockBack.toString(2)}")
//        println("sequence: $sequence , binary : ${sequence.toString(2)}")

        return ((timestamp - INIT_TIMESTAMP) shl TIMESTAMP_SHIFT) or
                (workId shl WORKER_ID_SHIFT) or
                (datacenterId shl DATACENTER_ID_SHIFT) or
                (clockBack shl CLOCK_BACK_SHIFT) or
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