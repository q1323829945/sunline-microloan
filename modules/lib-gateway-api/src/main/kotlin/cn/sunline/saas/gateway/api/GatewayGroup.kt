package cn.sunline.saas.gateway.api



interface GatewayGroup {
    fun create(createParams: Any):Any?

    fun update(updateParams: Any):Any?

    fun delete(id:String)

    fun getOne(groupName:String):Any?

}