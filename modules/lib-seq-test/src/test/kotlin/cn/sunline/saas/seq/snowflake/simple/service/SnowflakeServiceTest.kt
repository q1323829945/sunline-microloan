package cn.sunline.saas.seq.snowflake.simple.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SnowflakeServiceTest(@Autowired private val snowflakeService: SnowflakeSimpleService) {


    @Test
    fun `get one Id`() {
        val id = snowflakeService.nextId()
        println(id)
        assertThat(id).isNotNull
        assertThat(id).isGreaterThan(0)
    }

    @Test
    fun `getId by loop`() {
        val m = hashSetOf<Long>()
        val expect = 5000000
        val start  = System.currentTimeMillis()
        for (i in 1..expect) {
            val id = snowflakeService.nextId()
            m.add(id)
        }
        val end = System.currentTimeMillis()

        println("time:${end - start}")

        assertThat(m.size).isEqualTo(expect)

    }


}