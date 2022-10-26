package cn.sunline.saas.context

import cn.sunline.saas.modules.dto.TenantApi
import cn.sunline.saas.modules.dto.TenantInstance
import cn.sunline.saas.modules.dto.TenantServer
import cn.sunline.saas.redis.services.RedisClient
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Component

@Component
class GatewayContext(private var redisClient: RedisClient) {

    val objectMapper: ObjectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    private val gatewayHashMap = "gateway_hash_map"

    fun put(id: String, instance: TenantInstance) {
        redisClient.addToMap(gatewayHashMap,id,instance)
    }

    fun get(id: String): TenantInstance? {
        return  redisClient.getMapItem<TenantInstance>(gatewayHashMap,id)
    }

    fun remove(id: String) {
        redisClient.deleteMapItemByKey(gatewayHashMap,id)
    }

    fun addServer(instanceId: String,tenantServer: TenantServer){
        val instance = redisClient.getMapItem<TenantInstance>(gatewayHashMap,instanceId)?: return
        instance.server.add(tenantServer)
        redisClient.addToMap(gatewayHashMap,instanceId,instance)
    }

    fun removeServer(instanceId: String, serverId: String) {
        val instance = redisClient.getMapItem<TenantInstance>(gatewayHashMap,instanceId)?: return
        instance.server.removeIf { it.serverId == serverId }
        redisClient.addToMap(gatewayHashMap,instanceId,instance)
    }

    fun getServer(serverId: String): TenantServer? {
        redisClient.getMapAllItems<TenantInstance>(gatewayHashMap).forEach { instance ->
            return instance.server.firstOrNull { it.serverId == serverId }
        }
        return null
    }

    fun getServer(instanceId: String, server: String): TenantServer? {
        val instance = redisClient.getMapItem<TenantInstance>(gatewayHashMap,instanceId)?: return null
        return instance.server.firstOrNull { it.server == server }
    }

    fun addApi(serverId: String,tenantApi: TenantApi){
        val instances = redisClient.getMapAllItems<TenantInstance>(gatewayHashMap)

        instances.forEach {  instance ->
            instance.server.firstOrNull { it.serverId == serverId }?.run {
                this.apis.add(tenantApi)
                redisClient.addToMap(gatewayHashMap,instance.instanceId,instance)
                return
            }
        }
    }

    fun removeApi(serverId: String,api:String,method:String){
        val instances = redisClient.getMapAllItems<TenantInstance>(gatewayHashMap)

        instances.forEach {  instance ->
            instance.server.firstOrNull { it.serverId == serverId }?.run {
                this.apis.removeIf { it.path == api && it.method == method }
                redisClient.addToMap(gatewayHashMap,instance.instanceId,instance)
                return
            }
        }
    }

    fun getAll(): List<TenantInstance> {
        return redisClient.getMapAllItems(gatewayHashMap)
    }

}