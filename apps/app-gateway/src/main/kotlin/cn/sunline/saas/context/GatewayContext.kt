package cn.sunline.saas.context

import cn.sunline.saas.modules.dto.TenantInstance
import cn.sunline.saas.modules.dto.TenantServer

object GatewayContext {
    private val map = mutableMapOf<String, TenantInstance>()

    fun put(id:String,server:TenantInstance){
        map[id] = server
    }

    fun get(id:String):TenantInstance?{
        return map[id]
    }

    fun remove(id:String){
        map.remove(id)
    }

    fun removeServer(instanceId: String,serverId:String){
        val instance = map[instanceId]?: return
        instance.server.removeIf { it.serverId == serverId }
    }

    fun getServer(serverId:String):TenantServer?{
        map.forEach { (instanceId, tenantInstance) ->
            return tenantInstance.server.firstOrNull {  it.serverId == serverId  }
        }
        return null
    }

    fun getServer(instanceId:String,server:String):TenantServer?{
        val instance = map[instanceId]?: return null
        return instance.server.firstOrNull { it.server == server }
    }

    fun getAll():List<TenantInstance>{
        return map.values.toMutableList()
    }

}