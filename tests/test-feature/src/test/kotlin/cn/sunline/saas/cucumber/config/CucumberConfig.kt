package cn.sunline.saas.cucumber.config

import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.pdpa.modules.dto.DTOPdpaItem
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.cucumber.java.DocStringType
import io.cucumber.java.en.Given
import io.cucumber.spring.CucumberContextConfiguration
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@CucumberContextConfiguration
class CucumberConfig {
    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @DocStringType(contentType = "json")
    fun convert(json: String): List<DTOPdpaItem> {
        return objectMapper.readValue(json)
    }

    @Given("init tenant")
    fun initTenant(){
        ContextUtil.setTenant("9999")
    }
}