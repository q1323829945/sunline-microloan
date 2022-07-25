package cn.sunline.saas.multi_tenant.util

import cn.sunline.saas.multi_tenant.model.Tenant

class TenantUtil {

    companion object {
        private val tenantMap:MutableMap<Long,Tenant> = mutableMapOf()


        fun getTenant(tenantId:Long):Tenant?{
            return tenantMap[tenantId]
        }

        fun setTenant(tenantId:Long,tenant: Tenant){
            tenantMap[tenantId] = tenant
        }
    }
}