package cn.sunline.saas.service

import cn.sunline.saas.dapr_wrapper.invoke.RPCService
import cn.sunline.saas.dapr_wrapper.invoke.response.RPCResponse
import cn.sunline.saas.gateway.api.GatewayServer
import cn.sunline.saas.gateway.api.dto.ServerParams
import cn.sunline.saas.gateway.api.dto.ServerView
import cn.sunline.saas.request.ServerGetOneRPCRequest
import cn.sunline.saas.request.ServerRegisterRPCRequest
import cn.sunline.saas.request.ServerRemoveRPCRequest
import org.springframework.stereotype.Service

@Service
class GatewayServerService:GatewayServer {
    override fun register(serverParams: ServerParams): ServerView? {
        val response = RPCService.post<RPCResponse<ServerView>>(ServerRegisterRPCRequest(serverParams))
         return response?.data
    }

    override fun getOne(tenant: String, server: String): ServerView? {
        val response = RPCService.get<RPCResponse<ServerView>>(ServerGetOneRPCRequest(tenant, server))
        return response?.data
    }

    override fun remove(client: String) {
        RPCService.delete<String>(ServerRemoveRPCRequest(client))
    }


}