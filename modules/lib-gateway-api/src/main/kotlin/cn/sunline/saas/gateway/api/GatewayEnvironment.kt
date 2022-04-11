package cn.sunline.saas.gateway.api

import cn.sunline.saas.gateway.api.dto.EnvironmentCreateParams
import cn.sunline.saas.gateway.api.dto.EnvironmentPagedParams
import cn.sunline.saas.gateway.api.dto.EnvironmentResponseParams
import cn.sunline.saas.gateway.api.dto.EnvironmentUpdateParams


interface GatewayEnvironment {
    fun create(environmentCreateParams: EnvironmentCreateParams):EnvironmentResponseParams

    fun update(environmentUpdateParams: EnvironmentUpdateParams):EnvironmentResponseParams

    fun delete(id:String)

    fun getPaged(environmentPagedParams: EnvironmentPagedParams):List<EnvironmentResponseParams>
}