package cn.sunline.saas.gateway.api


interface GatewayEnvironment {
    fun create(environmentCreateParams: Any):Any?

    fun update(environmentUpdateParams: Any):Any?

    fun delete(id:String)

    fun getOne(environmentName:String):Any?
}