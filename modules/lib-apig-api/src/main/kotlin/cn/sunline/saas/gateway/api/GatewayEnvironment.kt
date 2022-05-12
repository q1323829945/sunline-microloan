package cn.sunline.saas.gateway.api

import cn.sunline.saas.gateway.api.dto.*


interface GatewayEnvironment {
    fun create(environmentCreateParams: EnvironmentCreateParams):EnvironmentResponseParams

    fun update(environmentUpdateParams: EnvironmentUpdateParams):EnvironmentResponseParams

    fun delete(id:String)

    fun getPaged(environmentPagedParams: EnvironmentPagedParams): EnvironmentPagedResponseParams
}