package cn.sunline.saas.gateway.api

import cn.sunline.saas.gateway.api.dto.*


interface GatewayGroup {
    fun create(createParams: GroupCreateParams):GroupResponseParams

    fun update(updateParams: GroupUpdateParams): GroupResponseParams

    fun delete(id:String)

    fun getPaged(groupPagedParams: GroupPagedParams): GroupPagedResponseParams

}