package cn.sunline.saas.document.generation.convert.factory

import cn.sunline.saas.document.generation.convert.factory.impl.ConvertToPDF
import cn.sunline.saas.document.template.modules.FileType
import cn.sunline.saas.document.template.modules.FileType.*
import org.springframework.stereotype.Component

@Component
class ConvertFactory {
    fun instance(fileType: FileType): BaseFileConvert {
        when (fileType){
            PDF -> return ConvertToPDF()
            DOC -> TODO()
            DOCX -> TODO()
        }
    }
}