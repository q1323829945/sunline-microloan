package cn.sunline.saas.document.generation.config

import cn.sunline.saas.document.generation.convert.factory.ConvertFactory
import cn.sunline.saas.document.generation.models.FileType
import cn.sunline.saas.document.generation.template.factory.TemplateFactory
import cn.sunline.saas.document.template.modules.DocumentType
import cn.sunline.saas.document.template.modules.TemplateType
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.usermodel.XWPFParagraph
import org.apache.poi.xwpf.usermodel.XWPFRun
import org.apache.poi.xwpf.usermodel.XWPFTable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.InputStream

data class TemplateParams(
        val inputStream: InputStream,
        val documentType: DocumentType
)

@Component
class FileGeneration {
    @Autowired
    private lateinit var convertFactory: ConvertFactory

    @Autowired
    private lateinit var templateFactory: TemplateFactory

    fun generation(templateParams: TemplateParams, params: Map<String, String>, convertType: FileType):InputStream {

        val templateOperation = templateFactory.instance(templateParams.documentType)

        val document = templateOperation.fillTemplate(templateParams.inputStream,params)

        val convert = convertFactory.instance(convertType)

        return convert.convert(document)
    }

}