package cn.sunline.saas.document.generate.service.dto

import cn.sunline.saas.document.template.modules.FileType

data class DTOGeneration(
    val templateId:Long,
    val params:Map<String,String>,
    val generateType: FileType,
    val key:String
)