package cn.sunline.saas.service

import cn.sunline.saas.context.GatewayContext
import cn.sunline.saas.modules.db.Api
import cn.sunline.saas.modules.db.Instance
import cn.sunline.saas.modules.db.Server
import cn.sunline.saas.modules.dto.TenantApi
import cn.sunline.saas.modules.dto.TenantInstance
import cn.sunline.saas.modules.dto.TenantServer
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class GatewayService(
    private val instanceService: InstanceService,
    private val serverService: ServerService
) {
    private var logger = KotlinLogging.logger {}

    init {
        logger.info { "gateway init..." }
        val instances = instanceService.findAll()
        instances.forEach {
            registerInstance(it)
        }
        logger.info { "gateway init finish!" }
    }

    fun addOrUpdate(instanceId:String){
        val instance = instanceService.getOne(instanceId)?: return
        logger.info { "add tenant..." }
        registerInstance(instance)
        logger.info { "add tenant finish!" }
    }

    fun remove(id:String){
        logger.info { "remove tenant ..." }
        GatewayContext.remove(id)
        logger.info { "remove tenant finish!" }
    }

    fun addServer(instanceId: String,server:Server){
        val instance = GatewayContext.get(instanceId)?: return
        instance.server.add(TenantServer(server.domain,server.server,server.id.toString(), mutableListOf()))
    }

    fun removeServer(instanceId:String,serverId:String){
        logger.info { "remove server ..." }
        GatewayContext.removeServer(instanceId, serverId)
        logger.info { "remove server finish!" }
    }

    private fun registerInstance(instance: Instance){
        if(!instance.enabled) return
        val servers = instance.server

        val tenantServers = mutableListOf<TenantServer>()
        servers.forEach { server ->
            val apis = server.apis
            val tenantApis = mutableListOf<TenantApi>()
            if(apis.any { it.enabled }){
                apis.filter { it.enabled }.forEach { api ->
                    tenantApis.add(
                        TenantApi(
                            api.api,
                            api.api.split("/"),
                            api.method,
                            api.formatType
                        )
                    )
                }
            }
            tenantServers.add(TenantServer(server.domain,server.server,server.id.toString(),tenantApis))
        }

        GatewayContext.put(instance.id, TenantInstance(instance.id,instance.secretKey,instance.tenant,tenantServers) )
    }

    fun addApi(api: Api){
        val server = GatewayContext.getServer(api.serverId)?:return


        logger.info { "add api ${api.serverId} ${api.api} ${api.method} ..." }
        server.apis.add(
            TenantApi(
                api.api,
                api.api.split("/"),
                api.method,
                api.formatType
            )
        )

        logger.info { "add api ${api.serverId} ${api.api} ${api.method} finish!" }
    }

    fun removeApi(api: Api){
        val server = GatewayContext.getServer(api.serverId)?: return
        logger.info { "remove api ${api.serverId} ${api.api} ${api.method} ..." }
        server.apis.removeIf { it.path == api.api && it.method == api.method}
        logger.info { "remove api ${api.serverId} ${api.api} ${api.method} finish!" }
    }
}