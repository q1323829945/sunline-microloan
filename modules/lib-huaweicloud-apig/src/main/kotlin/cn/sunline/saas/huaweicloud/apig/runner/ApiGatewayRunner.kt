package cn.sunline.saas.huaweicloud.apig.runner

import cn.sunline.saas.gateway.api.GatewayApi
import cn.sunline.saas.gateway.api.GatewayApp
import cn.sunline.saas.gateway.api.GatewayEnvironment
import cn.sunline.saas.gateway.api.GatewayGroup
import cn.sunline.saas.gateway.api.constant.ActionType
import cn.sunline.saas.gateway.api.dto.*
import cn.sunline.saas.huaweicloud.apig.config.ApiConfiguration
import cn.sunline.saas.huaweicloud.apig.config.AppType
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class ApiGatewayRunner(
    val huaweiCloudApigEnvironmentService: GatewayEnvironment,
    val huaweiCloudApigGroupService: GatewayGroup,
    val huaweiCloudApigApiService: GatewayApi,
    val huaweiCloudApigAppService: GatewayApp
): CommandLineRunner {

    @Value("\${huawei.cloud.apig.groupName}")
    lateinit var groupName:String

    @Value("\${huawei.cloud.apig.environmentName}")
    lateinit var environmentName:String

    @Value("\${huawei.cloud.apig.appName}")
    var appName:String? = null

    @Value("\${huawei.cloud.apig.ip}")
    private lateinit var ip:String

    @Value("\${huawei.cloud.apig.domainUrl}")
    var domainUrl:String? = null

    @Value("\${huawei.cloud.appType}")
    private lateinit var appType:AppType

    var environmentId:String? = null
    var groupId:String? = null
    var appId:String? = null

    override fun run(vararg args: String?) {
        reloadEnvironment()
        reloadGroup()
        reloadApp()

        reloadApi(groupId!!)
        onlineApi(environmentId!!, groupId!!)

        appId?.run {
            auths(this,groupId!!, environmentId!!)
        }
    }

    fun reloadEnvironment(){
        println("init environment ...")
        val environmentList = huaweiCloudApigEnvironmentService.getPaged(EnvironmentPagedParams(name = environmentName)).envs
        environmentId =  if(environmentList.isEmpty()){
            huaweiCloudApigEnvironmentService.create(EnvironmentCreateParams(environmentName)).id
        } else{
            environmentList[0].id
        }
        println("init environment finish")
    }

    fun reloadGroup(){
        println("init group ...")
        val groupList = huaweiCloudApigGroupService.getPaged(GroupPagedParams(name = groupName)).groups
        groupId = if(groupList.isEmpty()){
            huaweiCloudApigGroupService.create(GroupCreateParams(groupName)).id
        } else{
            groupList[0].id
        }
        println("init group finish")

    }

    fun reloadApi(groupId:String){
        println("init api ...")

        val apiConfiguration = ApiConfiguration(groupId,ip,appType)

        val apiList = getApiList(groupId)

        registerApi(apiConfiguration, apiList)

        deleteApi(apiConfiguration, apiList)

        println("init api finish")

    }

    fun registerApi(apiConfiguration:ApiConfiguration,apiList:List<ApiResponseParams>){

        println("register api ...")
        val nameList = apiList.map {
            it.apiName
        }

        apiConfiguration.apiParamsList.forEach{
            if(!nameList.contains(it.apiName)){
                huaweiCloudApigApiService.register(it)
            }
        }
        println("register api finish")
    }

    fun deleteApi(apiConfiguration:ApiConfiguration,apiList:List<ApiResponseParams>){
        println("delete api ...")
        val nameList = apiConfiguration.apiParamsList.map {
            it.apiName
        }

        apiList.forEach {
            if(!nameList.contains(it.apiName)){
                huaweiCloudApigApiService.delete(it.id)
            }
        }

        println("delete api finish")
    }

    fun onlineApi(environmentId:String,groupId:String){
        println("api online ...")


        val apiList = getApiList(groupId)

        val idList = apiList.map {
            it.id
        }

        huaweiCloudApigApiService.batchPublish(
                BatchPublishParams(
                ActionType.online,
                idList,
                environmentId
            )
        )

        println("api online finish")
    }

    fun getApiList(groupId: String): List<ApiResponseParams> {
        val list = ArrayList<ApiResponseParams>()

        var index = 1
        val pageSize = 500
        while (true){
            val pageParams = ApiPagedParams(
                pageSize = pageSize,
                pageNo = index,
                groupId = groupId
            )

            val response = huaweiCloudApigApiService.getPaged(pageParams)
            list.addAll(response.apis)

            if(pageSize * (index-1) + response.size >= response.total){
                break
            }

            index++
        }

        return list
    }

    fun reloadApp(){
        println("init app ...")
        println(appName)
        appName?.run {
            if(this.isNotEmpty()){
                val appList = huaweiCloudApigAppService.getPaged(AppPagedParams(name = appName)).apps
                appId = if(appList.isEmpty()){
                    huaweiCloudApigAppService.create(AppCreateParams(this)).id
                } else {
                    appList[0].id
                }
                println("init app finish")

                return
            }
        }
        println("appName is null or empty")
        println("init app finish")
    }

    fun auths(appId:String,groupId: String,environmentId: String){
        println("auth api ...")

        val apiList = getApiList(groupId)
        val idList = apiList.map {
            it.id
        }

        if(idList.isNotEmpty()){
            huaweiCloudApigAppService.auths(AuthsParams(idList, listOf(appId),environmentId))
        }



        println("auth api finish")
    }

}