package cn.sunline.saas.tools

import cn.sunline.saas.context.GatewayContext
import cn.sunline.saas.modules.dto.DTOGateway
import cn.sunline.saas.modules.dto.TenantServer
import mu.KotlinLogging

object ApiVerificationTools {

    private var logger = KotlinLogging.logger {}

    private val regex = Regex("\\{.*}$")

    fun verificationAndGet(instanceId:String,path:String,method:String): DTOGateway?{
        val mapping = PathMappingTools.mapping(path) ?: return null

        val instance = GatewayContext.get(instanceId)?: return null

        val server = GatewayContext.getServer(instanceId,mapping.server)?: return null

        if(server.apis.isNotEmpty() && !checkApi(server, path, method)){
            return null
        }

        return DTOGateway(
            url = server.domain + mapping.path,
            path = mapping.path,
            server = server.server,
            secretKey = instance.secretKey,
            tenant = instance.tenant
        )
    }

    /**
     * 添加api校验
     */
    private fun checkApi(server: TenantServer, path:String, method:String):Boolean{
        server.apis.firstOrNull { "${it.path}_${it.method}" == "${path}_${method}" }?.run {
            logger.info { "Found the register api: ${server.serverId} ${this.path} ${this.method}" }
            return true
        }?: run {
            val pathGroup = path.split("/")
            server.apis.filter {it.method == method && it.pathGroup.size == pathGroup.size}.forEach {
                var flag = 0
                for(i in it.pathGroup.indices){
                    //check the group every one ture
                    if(it.pathGroup[i] == pathGroup[i] || regex.matches(it.pathGroup[i])){
                        flag++
                    }
                }
                if(flag == it.pathGroup.size){
                    logger.info { "Found the register api: ${server.serverId} ${it.path} ${it.method}" }
                    return true
                }
            }

            logger.info { "The api ${server.serverId} $path $method is not found" }
            return false
        }
    }
}