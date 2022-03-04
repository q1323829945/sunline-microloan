package cn.sunline.saas.document.generation.convert.factory

import cn.sunline.saas.document.generation.convert.factory.impl.ConvertToPDF
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