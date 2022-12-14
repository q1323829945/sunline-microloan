package cn.sunline.saas.rpc.pubsub.dto

import cn.sunline.saas.document.template.modules.FileType


data class DTODocumentGeneration(
    val templateId:Long,
    val params:Map<String,String>,
    val generateType: FileType,
    val key:String
)
