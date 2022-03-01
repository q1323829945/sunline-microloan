package cn.sunline.saas.document.generation.factory

import cn.sunline.saas.document.generation.factory.impl.ConvertToPDF
import cn.sunline.saas.document.generation.models.FileType
import cn.sunline.saas.document.generation.models.FileType.*
import org.springframework.stereotype.Component

@Component
class ConvertFactory {
    fun instance(fileType: FileType): BaseFileConvert {
        when (fileType){
            PDF -> return ConvertToPDF()
        }
    }
}