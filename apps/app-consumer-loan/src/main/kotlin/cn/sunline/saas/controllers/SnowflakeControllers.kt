package cn.sunline.saas.controllers

import cn.sunline.saas.snowflake.`interface`.SnowflakeInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("snowflake")
class SnowflakeControllers {

    @Autowired
    private lateinit var snowflakeService: SnowflakeInterface

    @GetMapping
    fun getId(): Long {
        return snowflakeService.nextId()
    }


}