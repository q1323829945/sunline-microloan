package cn.sunline.saas.pdpa.service

import cn.sunline.saas.config.AppHttpConfiguration
import cn.sunline.saas.config.IpConfig
import cn.sunline.saas.dapr_wrapper.pubsub.PubSubService
import cn.sunline.saas.pdpa.dto.PDPAInformation
import cn.sunline.saas.rpc.invoke.PdpaInvoke
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
class PDPAMicroService(
    private var ipConfig:IpConfig,
    private var appHttpConfiguration: AppHttpConfiguration,
    private val pdpaInvoke: PdpaInvoke
) {

    fun sign(
        customerId: Long,
        pdpaTemplateId: Long,
        fileName: String,
        inputStream: InputStream
    ): String {


        //uri
//        val uri = "http://${ipConfig.pdpaIp}/pdpa/sign"
//        //parts
//        val file = File(fileName)
//        val fos = FileOutputStream(file)
//        val bytes = ByteArray(1024)
//        while (true) {
//            val len = inputStream.read(bytes)
//            if (len == -1) {
//                break
//            }
//            fos.write(bytes, 0, len)
//        }
//        fos.close()
//        inputStream.close()
//        val filePart = FilePart("signature", fileName, file, MediaType.MULTIPART_FORM_DATA_VALUE, "utf-8")
//        val customerIdPart = StringPart("customerId", customerId.toString())
//        customerIdPart.contentType = MediaType.APPLICATION_JSON_VALUE
//        val pdpaTemplateIdPart = StringPart("pdpaTemplateId", pdpaTemplateId.toString())
//        pdpaTemplateIdPart.contentType = MediaType.APPLICATION_JSON_VALUE
//        val parts = arrayOf(filePart, customerIdPart, pdpaTemplateIdPart)
//
//        //get post method
//        val postMethod = appHttpConfiguration.getHttpMethod(HttpRequestMethod.POST, uri,appHttpConfiguration.getPublicHeaders(), parts)
//
//        //send http
//        try {
//            appHttpConfiguration.sendClient(postMethod)
//        } finally {
//            file.delete()
//        }
//
//        return appHttpConfiguration.getResponse(postMethod)
        return ""
    }


    fun retrieve(countryCode: String): PDPAInformation {
        return pdpaInvoke.getPDPAInformation(countryCode)!!.data!!
    }
}
