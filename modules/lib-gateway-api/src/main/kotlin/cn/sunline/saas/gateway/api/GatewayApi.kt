package cn.sunline.saas.gateway.api

interface  GatewayApi{


    fun register(registerParams: Any):Any?

    fun update(apiUpdateParams: Any):Any?

    fun delete(id:String)

    fun online(onlineParams: Any)

    fun offline(offlineParams: Any)

    fun batchPublish(batchPublishParams: Any)

    fun getPaged(pageParams:Any):Any?


}