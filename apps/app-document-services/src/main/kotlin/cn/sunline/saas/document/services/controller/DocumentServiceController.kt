package cn.sunline.saas.document.services.controller

import cn.sunline.saas.document.generation.config.FileGeneration
import cn.sunline.saas.document.generation.config.TemplateParams
import cn.sunline.saas.document.template.modules.FileType
import cn.sunline.saas.document.template.services.DocumentTemplateService
import cn.sunline.saas.huaweicloud.obs.services.HuaweiCloudObsService
import cn.sunline.saas.obs.api.GetParams
import cn.sunline.saas.obs.api.PutParams
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.InputStream

/**
 * @title: DocumentServiceController
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/1 17:30
 */
@RestController
@RequestMapping("/document-services")
class DocumentServiceController {
    data class DTOGeneration(
            val template_id:Long,
            val params:Map<String,String>,
            val generate_type: FileType,
            val save_bucket_name:String,
            val key:String
    )

    @Autowired
    private lateinit var huaweiCloudService: HuaweiCloudObsService
    @Autowired
    private lateinit var documentTemplateService: DocumentTemplateService
    @Autowired
    private lateinit var fileGeneration: FileGeneration

    @PostMapping("/generate")
    fun generate(@RequestBody dtoGeneration:DTOGeneration){
        //get template params from db
        val documentTemplate = documentTemplateService.getOne(dtoGeneration.template_id)?:throw Exception("error")
        //get template stream from obs
        val getParams = GetParams(documentTemplate.documentStoreReference)
        val inputStream = huaweiCloudService.getObject(getParams) as InputStream
        //pdf generation
        val templateParams = TemplateParams(inputStream,documentTemplate.fileType)
        val pdfInputStream = fileGeneration.generation(templateParams,dtoGeneration.params, dtoGeneration.generate_type)
        //save pdf in obs
        val put = PutParams(dtoGeneration.key,pdfInputStream)
        huaweiCloudService.putObject(put)

    }

}