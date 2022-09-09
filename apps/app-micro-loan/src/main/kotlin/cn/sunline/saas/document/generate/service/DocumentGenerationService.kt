package cn.sunline.saas.document.generate.service

import cn.sunline.saas.document.generate.service.dto.DTOGeneration
import cn.sunline.saas.document.generation.config.FileGeneration
import cn.sunline.saas.document.generation.config.TemplateParams
import cn.sunline.saas.document.template.services.DocumentTemplateService
import cn.sunline.saas.obs.api.GetParams
import cn.sunline.saas.obs.api.ObsApi
import cn.sunline.saas.obs.api.PutParams
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
class DocumentGenerationService {

    @Autowired
    private lateinit var huaweiCloudService: ObsApi
    @Autowired
    private lateinit var documentTemplateService: DocumentTemplateService
    @Autowired
    private lateinit var fileGeneration: FileGeneration

    fun generate(dtoGeneration: DTOGeneration){
        //get template params from db
        val documentTemplate = documentTemplateService.getOne(dtoGeneration.templateId)?:throw Exception("error")
        //get template stream from obs
        val getParams = GetParams(documentTemplate.documentStoreReference)
        val inputStream = huaweiCloudService.getObject(getParams) as InputStream
        //pdf generation
        val templateParams = TemplateParams(inputStream,documentTemplate.fileType)
        val pdfInputStream = fileGeneration.generation(templateParams,dtoGeneration.params, dtoGeneration.generateType)
        //save pdf in obs
        val put = PutParams(dtoGeneration.key,pdfInputStream)
        huaweiCloudService.putObject(put)

        inputStream.close()
    }
}