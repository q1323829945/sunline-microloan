package cn.sunline.saas.runner

import cn.sunline.saas.config.ApiConfiguration
import cn.sunline.saas.gateway.api.GatewayApi
import cn.sunline.saas.gateway.api.GatewayApp
import cn.sunline.saas.gateway.api.GatewayEnvironment
import cn.sunline.saas.gateway.api.GatewayGroup
import cn.sunline.saas.gateway.api.constant.ActionType
import cn.sunline.saas.gateway.api.dto.*
import cn.sunline.saas.huaweicloud.apig.constant.*
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
        val environmentList = huaweiCloudApigEnvironmentService.getPaged(EnvironmentPagedParams(name = environmentName))
        environmentId =  if(environmentList.isEmpty()){
            huaweiCloudApigEnvironmentService.create(EnvironmentCreateParams(environmentName)).id
        } else{
            environmentList[0].id
        }
        println("init environment finish")
    }

    fun reloadGroup(){
        println("init group ...")
        val groupList = huaweiCloudApigGroupService.getPaged(GroupPagedParams(name = groupName))
        groupId = if(groupList.isEmpty()){
            huaweiCloudApigGroupService.create(GroupCreateParams(groupName)).id
        } else{
            groupList[0].id
        }
        println("init group finish")

    }

    fun reloadApi(groupId:String){
        println("init api ...")

        val apiConfiguration = ApiConfiguration(groupId,ip)

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
        val pageParams = ApiPagedParams(
            pageSize = 0,
            groupId = groupId
        )

        return huaweiCloudApigApiService.getPaged(pageParams)
    }

    fun reloadApp(){
        println("init app ...")
        appName?.run {
            val appList = huaweiCloudApigAppService.getPaged(AppPagedParams(name = appName))
            appId = if(appList.isEmpty()){
                huaweiCloudApigAppService.create(AppCreateParams(this)).id
            } else {
                appList[0].id
            }
            println("init app finish")
        }
        println("appName is null ")
        println("init app finish")
    }

    fun auths(appId:String,groupId: String,environmentId: String){
        println("auth api ...")

        val apiList = getApiList(groupId)
        val idList = apiList.map {
            it.id
        }

        huaweiCloudApigAppService.auths(AuthsParams(idList, listOf(appId),environmentId))


        println("auth api finish")
    }

}