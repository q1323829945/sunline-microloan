package cn.sunline.saas.gateway.api

import cn.sunline.saas.gateway.api.dto.*


interface GatewayApp {
    fun create(appCreateParams: AppCreateParams):AppResponseParams

    fun update(appUpdateParams: AppUpdateParams):AppResponseParams

    fun delete(id:String)

    fun auths(authsParams: AuthsParams): List<AppAuthsResponseParams>

    fun revokeAuths(id:String)

    fun getPaged(appPagedParams: AppPagedParams):AppPagedResponseParams
}