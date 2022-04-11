package cn.sunline.saas.gateway.api

import cn.sunline.saas.gateway.api.dto.*

interface  GatewayApi{


    fun register(registerParams: APiCreateParams): ApiResponseParams

    fun update(apiUpdateParams: APiUpdateParams): ApiResponseParams

    fun delete(id:String)

    fun online(onlineParams: OnlineParams)

    fun offline(offlineParams: OfflineParams)

    fun batchPublish(batchPublishParams: BatchPublishParams)

    fun getPaged(pageParams: ApiPagedParams):List<ApiResponseParams>


}