package cn.sunline.saas.document.library.controller

import cn.sunline.saas.document.generation.config.FileGeneration
import cn.sunline.saas.document.generation.models.FileType
import cn.sunline.saas.document.generation.services.DocumentTemplateService
import cn.sunline.saas.huaweicloud.services.HuaweiCloudService
import cn.sunline.saas.obs.api.PutParams
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.io.FileInputStream

@RestController
@RequestMapping("test")
class TestController {
    @Autowired
    private lateinit var huaweiCloudService: HuaweiCloudService
    @Autowired
    private lateinit var documentTemplateService: DocumentTemplateService
    @Autowired
    private lateinit var fileGeneration: FileGeneration


    @GetMapping("upload")
    fun createPDFFromTemplateAndUploadToOBS(){
        val documentTemplate = documentTemplateService.getOne(1)?:throw Exception("error")
        //TODO:get template inputStream from obs
        val inputStream = FileInputStream(File(documentTemplate.documentStoreReference))

        val params = mapOf(Pair("\${sign}","授权人"),Pair("\${idNo}","身份证"),Pair("\${year}","2022")
                ,Pair("\${month}","3"),Pair("\${day}","1"))
        val pdfInputStream = fileGeneration.generation(inputStream,params,FileType.PDF)


        val put = PutParams(documentTemplate.bucketName,documentTemplate.name+".pdf",pdfInputStream)

        huaweiCloudService.putObject(put)
    }
}