package cn.sunline.saas.document.generation.factory

import cn.sunline.saas.document.generation.models.FileType
import cn.sunline.saas.document.generation.factory.service.BaseFileConvert
import cn.sunline.saas.document.generation.factory.service.impl.ConvertToPDF
import cn.sunline.saas.document.generation.models.FileType.*
import cn.sunline.saas.exceptions.NotFoundException
import org.springframework.stereotype.Component

@Component
class FileConvertFactory {

    fun getFIleConvert(fileType: FileType): BaseFileConvert {
        return when(fileType){
            PDF -> ConvertToPDF()
        }
    }
}