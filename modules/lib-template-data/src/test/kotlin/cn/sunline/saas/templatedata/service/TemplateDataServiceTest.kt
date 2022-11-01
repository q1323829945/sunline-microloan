package cn.sunline.saas.templatedata.service

import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.templatedata.service.TemplateDataService
import cn.sunline.saas.templatedata.service.TestClass
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TemplateDataServiceTest {

    @Autowired
    private lateinit var templateDataService: TemplateDataService
    @Test
    fun `get template data`(){
        val templateData = templateDataService.getTemplateData<TestClass>(TestClass::class, false)
        val i = 0
    }
}