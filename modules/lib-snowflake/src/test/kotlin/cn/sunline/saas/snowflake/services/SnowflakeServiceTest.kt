package cn.sunline.saas.snowflake.services

import cn.sunline.saas.redis.services.RedisClient
import cn.sunline.saas.snowflake.`interface`.SnowflakeInterface
import cn.sunline.saas.snowflake.config.MAX_CLOCK_BACK
import cn.sunline.saas.snowflake.config.MAX_SEQUENCE
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.assertj.core.api.Assertions.assertThat

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SnowflakeServiceTest {


    @Autowired
    private lateinit var snowflakeService: SnowflakeInterface

    @Test
    fun `get one Id`(){
        val id = snowflakeService.nextId()

        assertThat(id).isNotNull
    }

    @Test
    fun `getId by loop`(){
        val m = hashSetOf<Long>()
        for(i in 1..1000000){
            val id = snowflakeService.nextId()
            m.add(id)
        }

        assertThat(m.size).isEqualTo(1000000)

    }

    @Test
    fun `simulation clock back`(){
        val lastTimestamp = 100L
        var timestamp = 10L
        var sequence = 0L
        var clockBack = 0L

        for(i in 1..100000){

            //code from SnowflakeService line 23 to 33
            if(timestamp < lastTimestamp){
                if(sequence == 0L){
                    clockBack = (clockBack + 1) and MAX_CLOCK_BACK
                }

                sequence = (sequence + 1) and MAX_SEQUENCE //seq in 0 ... MAX_SEQUENCE

                if(clockBack == 0L){
                    timestamp = tilNextMillis(lastTimestamp)
                }
            }else{
                println("timestamp is over lastTimestamp")
                return
            }


            println("index: $i ,clockBack: $clockBack, sequence: $sequence, timestamp: $timestamp")
        }

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