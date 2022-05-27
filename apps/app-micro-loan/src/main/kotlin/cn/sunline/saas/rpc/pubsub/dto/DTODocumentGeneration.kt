package cn.sunline.saas.rpc.pubsub.dto



data class DTODocumentGeneration(
    val templateId:String,
    val map:Map<String,String>
)
