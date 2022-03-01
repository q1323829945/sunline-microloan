package cn.sunline.saas.document.generation.models

import cn.sunline.saas.document.generation.factory.service.BaseFileConvert
import cn.sunline.saas.document.generation.factory.service.impl.ConvertToPDF

enum class FileType(val convent:BaseFileConvert,val suffix:String) {
    PDF(ConvertToPDF(),".pdf")
}