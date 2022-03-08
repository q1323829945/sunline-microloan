package cn.sunline.saas.document.generation.template.factory

import cn.sunline.saas.document.generation.template.factory.impl.DocTypeTemplateOperation
import cn.sunline.saas.document.generation.template.factory.impl.DocxTypeTemplateOperation
import cn.sunline.saas.document.generation.template.factory.impl.PdfTypeTemplateOperation
import cn.sunline.saas.document.template.modules.FileType
import cn.sunline.saas.document.template.modules.FileType.*
import org.springframework.stereotype.Component

@Component
class TemplateFactory {
    fun instance(documentType: FileType): BaseTemplateOperation {
        return when (documentType){
            DOCX -> DocxTypeTemplateOperation()
            DOC -> DocTypeTemplateOperation()
            PDF -> PdfTypeTemplateOperation()
        }
    }
}