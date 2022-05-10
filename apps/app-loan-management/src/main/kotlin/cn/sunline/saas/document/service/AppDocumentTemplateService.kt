package cn.sunline.saas.document.service

import cn.sunline.saas.document.template.services.DocumentTemplateService
import cn.sunline.saas.exceptions.ManagementException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import java.net.URLEncoder
import javax.servlet.http.HttpServletResponse

@Service
class AppDocumentTemplateService {


    @Autowired
    private lateinit var documentTemplateService: DocumentTemplateService

    fun download(@PathVariable id:Long, response: HttpServletResponse) {
        val template = documentTemplateService.getOne(id)?:throw ManagementException(ManagementExceptionCode.DATA_NOT_FOUND,"Invalid template")
        val inputStream = documentTemplateService.download(template)

        val fileName = if(template.documentStoreReference.lastIndexOf("/") == -1){
            template.documentStoreReference
        }else{
            template.documentStoreReference.substring(template.documentStoreReference.lastIndexOf("/")+1)
        }
        response.reset()
        response.contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE
        response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"))

        IOUtils.write(inputStream.readBytes(),response.outputStream)
        inputStream.close()
    }
}