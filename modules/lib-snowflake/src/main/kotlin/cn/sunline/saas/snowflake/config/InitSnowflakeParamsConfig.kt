package cn.sunline.saas.snowflake.config

import cn.sunline.saas.redis.services.RedisClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct



@Component
class InitSnowflakeParamsConfig {

//    @Autowired
//    private lateinit var redisClient: RedisClient

    var workId: Long = -1
    var datacenterId:Long = -1

    @PostConstruct
    fun initWorkId(){
        workId = 1
//        if(workId == -1L){
//            workId = redisClient.getMapItem<Long>(SNOWFLAKE_REDIS_HASH, WORKER_ID_REDIS_KEY)?:0L
//            workId = (workId + 1) and MAX_WORKER
//            println("workId: $workId")
//            redisClient.addToMap(SNOWFLAKE_REDIS_HASH, WORKER_ID_REDIS_KEY,workId)
//        }
    }

    @PostConstruct
    fun initDatacenterId(){
        datacenterId = 1

        //TODO: get datacenterId from reg center
    }
}