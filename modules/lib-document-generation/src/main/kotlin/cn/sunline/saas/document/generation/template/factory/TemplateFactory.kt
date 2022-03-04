package cn.sunline.saas.document.generation.template.factory

import cn.sunline.saas.document.generation.template.factory.impl.DocTypeTemplateOperation
import cn.sunline.saas.document.generation.template.factory.impl.DocxTypeTemplateOperation
import cn.sunline.saas.document.generation.template.factory.impl.PdfTypeTemplateOperation
import cn.sunline.saas.document.template.modules.DocumentType
import cn.sunline.saas.document.template.modules.DocumentType.*
import org.springframework.stereotype.Component

@Component
class TemplateFactory {
    fun instance(documentType: DocumentType): BaseTemplateOperation {
        return when (documentType){
            DOCX -> DocxTypeTemplateOperation()
            DOC -> DocTypeTemplateOperation()
            PDF -> PdfTypeTemplateOperation()
        }
    }
}