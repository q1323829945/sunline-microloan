package cn.sunline.saas.document.generation.config

import cn.sunline.saas.document.generation.convert.factory.ConvertFactory
import cn.sunline.saas.document.generation.template.factory.TemplateFactory
import cn.sunline.saas.document.template.modules.FileType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.InputStream

data class TemplateParams(
        val inputStream: InputStream,
        val fileType: FileType
)

@Component
class FileGeneration {
    @Autowired
    private lateinit var convertFactory: ConvertFactory

    @Autowired
    private lateinit var templateFactory: TemplateFactory

    fun generation(templateParams: TemplateParams, params: Map<String, String>, convertType: FileType):InputStream {

        val templateOperation = templateFactory.instance(templateParams.fileType)

        val document = templateOperation.fillTemplate(templateParams.inputStream,params)

        val convert = convertFactory.instance(convertType)

        return convert.convert(document)
    }

}