package cn.sunline.saas.document.generation.services

import cn.sunline.saas.document.generation.config.FileGeneration
import cn.sunline.saas.document.generation.models.FileType
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FileGenerationTest {
    @Autowired
    private lateinit var fileGeneration: FileGeneration

    @Test
    fun `generation from stream`(){
        val file = FileInputStream(File("D:\\新建文件夹\\合同填充\\征信授权书.docx"))
        val params = mapOf(Pair("\${sign}","授权人"),Pair("\${idNo}","身份证"),Pair("\${year}","2022")
        ,Pair("\${month}","3"),Pair("\${day}","1"))
        val inputStream = fileGeneration.generation(file,params, FileType.PDF)

        val outputFile = FileOutputStream(File("D:\\新的征信2222.pdf"))

        var len = 0

        val bytes = ByteArray(1024)

        while (true){
            len = inputStream.read(bytes)

            if(len == -1){
                break
            }

            outputFile.write(bytes)
        }


    }

}