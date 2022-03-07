package cn.sunline.saas.document.generation.services

import cn.sunline.saas.document.generation.config.FileGeneration
import cn.sunline.saas.document.generation.config.TemplateParams
import cn.sunline.saas.document.template.modules.FileType
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
    fun `generation from docx stream`(){
        val file = FileInputStream(File("D:\\test\\ca.docx"))
        val params = mapOf(Pair("\${sign}","授权人"),Pair("\${idNo}","身份证"),Pair("\${year}","2022")
        ,Pair("\${month}","3"),Pair("\${day}","1"))
        val temp = TemplateParams(file,FileType.DOCX)
        val inputStream = fileGeneration.generation(temp,params, FileType.PDF)

        val outputFile = FileOutputStream(File("D:\\test\\myNewCA.pdf"))

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

    @Test
    fun `generation from pdf stream`(){
        val file = FileInputStream(File("D:\\test\\ok.pdf"))
        val params = mapOf(Pair("\$","asd"),Pair("\${idNo}","99885511"),Pair("\${year}","2022")
                ,Pair("\${month}","3"),Pair("\${day}","1"),Pair("\${sign}","Mr.Bean"),Pair("\${params}","GETONE"))
        val temp = TemplateParams(file,FileType.PDF)
        val inputStream = fileGeneration.generation(temp,params, FileType.PDF)

        val outputFile = FileOutputStream(File("D:\\test\\newEnglish3.pdf"))

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