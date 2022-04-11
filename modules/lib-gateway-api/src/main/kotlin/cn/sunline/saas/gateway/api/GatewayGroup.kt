package cn.sunline.saas.gateway.api

import cn.sunline.saas.gateway.api.dto.GroupCreateParams
import cn.sunline.saas.gateway.api.dto.GroupPagedParams
import cn.sunline.saas.gateway.api.dto.GroupResponseParams
import cn.sunline.saas.gateway.api.dto.GroupUpdateParams


interface GatewayGroup {
    fun create(createParams: GroupCreateParams):GroupResponseParams

    fun update(updateParams: GroupUpdateParams): GroupResponseParams

    fun delete(id:String)

    fun getPaged(groupPagedParams: GroupPagedParams):List<GroupResponseParams>

}