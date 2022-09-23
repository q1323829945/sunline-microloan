package cn.sunline.saas.multi_tenant.util

import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.global.util.setUUID
import cn.sunline.saas.multi_tenant.model.Tenant
import cn.sunline.saas.multi_tenant.services.TenantService
import mu.KotlinLogging
import java.util.*

object TenantMap {
    var logger = KotlinLogging.logger {}

    private val map = mutableMapOf<String,Tenant>()

    private fun set(key:String,value:Tenant){
        map[key] = value
    }

    private fun get(key:String): Tenant?{
        return map[key]
    }

    fun setContextUtil(tenantService: TenantService,domain:String):Tenant?{
        ContextUtil.setUUID(domain)
        val tenant = TenantMap.get(domain)?.run {
            this
        }?:run{
            try{
                tenantService.findByUUID(UUID.fromString(domain))
            } catch (e:Exception){
                logger.error{ e.message }
                null
            }
        }
        tenant?.run {
            TenantMap.set(domain,this)
            ContextUtil.setTenant(this.id.toString())
        }

        return tenant
    }
}