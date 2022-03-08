package cn.sunline.saas.document.generation.template.factory

import java.io.InputStream

interface BaseTemplateOperation {
    fun fillTemplate(inputStream:InputStream,params:Map<String,String>):Any
}