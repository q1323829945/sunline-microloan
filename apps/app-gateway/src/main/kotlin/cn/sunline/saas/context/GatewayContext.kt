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
        val instanceStr = objectMapper.writeValueAsString(instance)
        redisClient.addToMap(gatewayHashMap,id,instanceStr)
    }

    fun get(id: String): TenantInstance? {
        val instanceStr =  redisClient.getMapItem<String>(gatewayHashMap,id)
        return instanceStr?.run { objectMapper.readValue(this) }
    }

    fun remove(id: String) {
        redisClient.deleteMapItemByKey(gatewayHashMap,id)
    }

    fun addServer(instanceId: String,tenantServer: TenantServer){
        val instanceStr = redisClient.getMapItem<String>(gatewayHashMap,instanceId)?: return
        val instance = objectMapper.readValue<TenantInstance>(instanceStr)
        instance.server.add(tenantServer)
        redisClient.addToMap(gatewayHashMap,instanceId,objectMapper.writeValueAsString(instance))
    }

    fun removeServer(instanceId: String, serverId: String) {
        val instanceStr = redisClient.getMapItem<String>(gatewayHashMap,instanceId)?: return
        val instance = objectMapper.readValue<TenantInstance>(instanceStr)
        instance.server.removeIf { it.serverId == serverId }
        redisClient.addToMap(gatewayHashMap,instanceId,objectMapper.writeValueAsString(instance))
    }

    fun getServer(serverId: String): TenantServer? {
        redisClient.getMapAllItems<String>(gatewayHashMap).forEach { instanceStr ->
            val instance = objectMapper.readValue<TenantInstance>(instanceStr)
            return instance.server.firstOrNull { it.serverId == serverId }
        }
        return null
    }

    fun getServer(instanceId: String, server: String): TenantServer? {
        val instanceStr = redisClient.getMapItem<String>(gatewayHashMap,instanceId)?: return null
        val instance = objectMapper.readValue<TenantInstance>(instanceStr)

        return instance.server.firstOrNull { it.server == server }
    }

    fun addApi(serverId: String,tenantApi: TenantApi){
        val instances = redisClient.getMapAllItems<String>(gatewayHashMap).map {
            objectMapper.readValue<TenantInstance>(it)
        }

        instances.forEach {  instance ->
            instance.server.firstOrNull { it.serverId == serverId }?.run {
                this.apis.add(tenantApi)
                redisClient.addToMap(gatewayHashMap,instance.instanceId,objectMapper.writeValueAsString(instance))
                return
            }
        }
    }

    fun removeApi(serverId: String,api:String,method:String){
        val instances = redisClient.getMapAllItems<String>(gatewayHashMap).map {
            objectMapper.readValue<TenantInstance>(it)
        }

        instances.forEach {  instance ->
            instance.server.firstOrNull { it.serverId == serverId }?.run {
                this.apis.removeIf { it.path == api && it.method == method }
                redisClient.addToMap(gatewayHashMap,instance.instanceId,objectMapper.writeValueAsString(instance))
                return
            }
        }

    }

    fun getAll(): List<TenantInstance> {
        val instanceStrs = redisClient.getMapAllItems<String>(gatewayHashMap)
        val instances:MutableList<TenantInstance> = mutableListOf()
        instanceStrs.forEach {
            instances.add(objectMapper.readValue(it))
        }
        return instances
    }


}