package cn.sunline.saas.document.service

import cn.sunline.saas.document.template.services.DocumentTemplateService
import cn.sunline.saas.exceptions.ManagementException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
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
        response.reset();
        response.contentType = "application/octet-stream";
        response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
//        val outputStream = response.outputStream


        IOUtils.write(inputStream.readAllBytes(),response.outputStream)

//        val bytes = ByteArray(1024)
//        while (true){
//            val len = inputStream.read(bytes)
//
//            if(len == -1){
//                break
//            }
//            outputStream.write(bytes,0,len)
//        }

        inputStream.close()
    }
}