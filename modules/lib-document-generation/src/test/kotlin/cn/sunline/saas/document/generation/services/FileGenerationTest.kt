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
        val file = FileInputStream(File("src\\test\\resources\\doc\\docxReplace.docx"))
        val params = mapOf(Pair("\${sign}","Mr.Bean")
            ,Pair("\${idNo}","123456")
            ,Pair("\${year}","2022")
            ,Pair("\${month}","3")
            ,Pair("\${day}","1")
            , Pair("\${params}","replaceValue")
        )
        val temp = TemplateParams(file,FileType.DOCX)

        val start = System.currentTimeMillis()
        val inputStream = fileGeneration.generation(temp,params, FileType.PDF)

        println(System.currentTimeMillis() - start)

        val outputFile = FileOutputStream(File("src\\test\\resources\\doc\\docx2pdf.pdf"))

        val bytes = ByteArray(1024)

        while (true){
            val len = inputStream.read(bytes)

            if(len == -1){
                break
            }
            outputFile.write(bytes)
        }
    }


    @Test
    fun `generation from pdf stream`(){
        val file = FileInputStream(File("src\\test\\resources\\doc\\pdfReplace.pdf"))
        val params = mapOf(Pair("\${sign}","Mr.Bean")
            ,Pair("\${idNo}","123456")
            ,Pair("\${year}","2022")
            ,Pair("\${month}","3")
            ,Pair("\${day}","1")
            , Pair("\${params}","replaceValue")
        )

        val temp = TemplateParams(file,FileType.PDF)

        val start = System.currentTimeMillis()

        val inputStream = fileGeneration.generation(temp,params, FileType.PDF)

        println(System.currentTimeMillis() - start)

        val outputFile = FileOutputStream(File("src\\test\\resources\\doc\\pdf2pdf.pdf"))

        val bytes = ByteArray(1024)

        while (true){
            val len = inputStream.read(bytes)

            if(len == -1){
                break
            }
            outputFile.write(bytes)
        }
    }

}