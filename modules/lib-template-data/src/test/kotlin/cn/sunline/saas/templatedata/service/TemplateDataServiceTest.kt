package cn.sunline.saas.templatedata.service

import cn.sunline.saas.template.data.service.TemplateDataService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TemplateDataServiceTest {

    @Autowired
    private lateinit var templateDataService: TemplateDataService

//    @Test
//    fun `get template data`() {
//        val templateData = templateDataService.getTemplateData<TestClass>(TestClass::class, null, false)
//        val i = 0
//    }
}