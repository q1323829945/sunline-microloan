package cn.sunline.saas.services

import cn.sunline.saas.config.HttpConfiguration
import cn.sunline.saas.config.IpConfig
import cn.sunline.saas.global.constant.HttpRequestMethod
import com.google.gson.Gson
import org.apache.commons.httpclient.methods.multipart.FilePart
import org.apache.commons.httpclient.methods.multipart.StringPart
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@Service
class PDPAService(
    private var ipConfig:IpConfig,
    private var httpConfiguration: HttpConfiguration
    ) {

    private val charset = "utf-8"


    data class PDPAInformation (
        val personalInformation:List<PersonalInformation>,
        val corporateInformation:List<CorporateInformation>,
        val pdpaTemplateId:String
    )


    data class PersonalInformation(
        val key:String,
        val name:String
    )

    data class CorporateInformation(
        val key: String,
        val name: String
    )

    fun sign(
        customerId: Long,
        pdpaTemplateId: Long,
        fileName: String,
        inputStream: InputStream
    ): String {

        val uri = "http://${ipConfig.pdpaIp}/pdpa/sign"

        val file = File(fileName)

        val fos = FileOutputStream(file)

        val bytes = ByteArray(1024)
        while (true) {
            val len = inputStream.read(bytes)

            if (len == -1) {
                break
            }

            fos.write(bytes, 0, len)
        }

        fos.close()
        inputStream.close()

        val filePart = FilePart("signature", fileName, file, MediaType.MULTIPART_FORM_DATA_VALUE, charset)
        val customerIdPart = StringPart("customerId", customerId.toString())
        customerIdPart.contentType = MediaType.APPLICATION_JSON_VALUE
        val pdpaTemplateIdPart = StringPart("pdpaTemplateId", pdpaTemplateId.toString())
        pdpaTemplateIdPart.contentType = MediaType.APPLICATION_JSON_VALUE
        val parts = arrayOf(filePart, customerIdPart, pdpaTemplateIdPart)

        val postMethod = httpConfiguration.getHttpMethod(HttpRequestMethod.POST, uri, parts)

        try {
            httpConfiguration.sendClient(postMethod)
        } finally {
            file.delete()
        }

        return httpConfiguration.getResponse(postMethod)
    }


    fun retrieve(countryCode: String): PDPAInformation {

        val uri = "http://${ipConfig.pdpaIp}/pdpa/$countryCode/retrieve"

        val postMethod = httpConfiguration.getHttpMethod(HttpRequestMethod.GET, uri)

        httpConfiguration.sendClient(postMethod)

        val data = httpConfiguration.getResponse(postMethod)

        return Gson().fromJson(data, PDPAInformation::class.java)
    }
}
