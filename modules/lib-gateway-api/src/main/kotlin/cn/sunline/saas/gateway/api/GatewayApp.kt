package cn.sunline.saas.gateway.api




interface GatewayApp {
    fun create(appCreateParams: Any):Any?

    fun update(appUpdateParams: Any):Any?

    fun delete(id:String)

    fun auths(authsParams: Any):Any?

    fun revokeAuths(id:String)

    fun getOne(appName:String):Any?
}