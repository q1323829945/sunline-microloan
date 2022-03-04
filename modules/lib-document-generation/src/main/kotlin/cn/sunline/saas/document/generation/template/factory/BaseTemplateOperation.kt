package cn.sunline.saas.document.generation.template.factory

import cn.sunline.saas.document.generation.config.TemplateParams
import cn.sunline.saas.document.generation.models.FileType
import java.io.InputStream

interface BaseTemplateOperation {
    fun fillTemplate(inputStream:InputStream,params:Map<String,String>):Any
}