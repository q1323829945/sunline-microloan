package cn.sunline.saas.gateway.api

import cn.sunline.saas.gateway.api.dto.ServerParams
import cn.sunline.saas.gateway.api.dto.ServerView


interface GatewayServer {
    fun register(serverParams: ServerParams):ServerView?

    fun getOne(client: String,server:String): ServerView?

    fun remove(client:String)

}