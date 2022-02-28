package cn.sunline.saas.document.generation.services

import cn.sunline.saas.document.generation.models.FileType
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FileGenerationTest {
    @Autowired
    private lateinit var fileGeneration: FileGeneration

    @Test
    fun `generation from file path`(){
        val pathMap = mapOf(Pair(1,"D:\\新建文件夹\\合同填充\\征信授权书.docx"), Pair(2, "D:\\新建文件夹\\合同填充\\移动CA中心个人数字证书申请表.docx"))
        val params = mapOf(Pair("name","名字"))
        fileGeneration.generation(pathMap[1]!!,params, FileType.PDF,"D:\\","新的征信.pdf")
    }
}