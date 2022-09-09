package cn.sunline.saas.huaweicloud.apig.runner

import cn.sunline.saas.gateway.api.GatewayApi
import cn.sunline.saas.gateway.api.GatewayApp
import cn.sunline.saas.gateway.api.GatewayEnvironment
import cn.sunline.saas.gateway.api.GatewayGroup
import cn.sunline.saas.gateway.api.constant.ActionType
import cn.sunline.saas.gateway.api.dto.*
import cn.sunline.saas.huaweicloud.apig.config.ApiConfiguration
import cn.sunline.saas.huaweicloud.apig.config.AppType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "huawei.cloud.apig")
class ApiGatewayRunner(
    val huaweiCloudApigEnvironmentService: GatewayEnvironment,
    val huaweiCloudApigGroupService: GatewayGroup,
    val huaweiCloudApigApiService: GatewayApi,
    val huaweiCloudApigAppService: GatewayApp,
    var groupName:String = "",
    var environmentName:String = "",
    var appName:String? = null,
    var ip:String = "",
    var domainUrl:String? = null,
    var appType:AppType = AppType.MANAGEMENT
): CommandLineRunner {
    private val logger: Logger = LoggerFactory.getLogger(ApiGatewayRunner::class.java)

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
        logger.debug("init environment ...")
        val environmentList = huaweiCloudApigEnvironmentService.getPaged(EnvironmentPagedParams(name = environmentName)).envs
        environmentId =  if(environmentList.isEmpty()){
            huaweiCloudApigEnvironmentService.create(EnvironmentCreateParams(environmentName)).id
        } else{
            environmentList[0].id
        }
        logger.debug("init environment finish")
    }

    fun reloadGroup(){
        logger.debug("init group ...")
        val groupList = huaweiCloudApigGroupService.getPaged(GroupPagedParams(name = groupName)).groups
        groupId = if(groupList.isEmpty()){
            huaweiCloudApigGroupService.create(GroupCreateParams(groupName)).id
        } else{
            groupList[0].id
        }
        logger.debug("init group finish")

    }

    fun reloadApi(groupId:String){
        logger.debug("init api ...")

        val apiConfiguration = ApiConfiguration(groupId,ip,appType)

        val apiList = getApiList(groupId)

        registerApi(apiConfiguration, apiList)

        deleteApi(apiConfiguration, apiList)

        logger.debug("init api finish")

    }

    fun registerApi(apiConfiguration:ApiConfiguration,apiList:List<ApiResponseParams>){

        logger.debug("register api ...")
        val nameList = apiList.map {
            it.apiName
        }

        apiConfiguration.apiParamsList.forEach{
            if(!nameList.contains(it.apiName)){
                huaweiCloudApigApiService.register(it)
            }
        }
        logger.debug("register api finish")
    }

    fun deleteApi(apiConfiguration:ApiConfiguration,apiList:List<ApiResponseParams>){

        logger.debug("delete api ...")
        val nameList = apiConfiguration.apiParamsList.map {
            it.apiName
        }

        apiList.forEach {
            if(!nameList.contains(it.apiName)){
                huaweiCloudApigApiService.delete(it.id)
            }
        }

        logger.debug("delete api finish")
    }

    fun onlineApi(environmentId:String,groupId:String){

        logger.debug("api online ...")


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

        logger.debug("api online finish")
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
        logger.debug("init app ...")
        appName?.run {
            if(this.isNotEmpty()){
                val appList = huaweiCloudApigAppService.getPaged(AppPagedParams(name = appName)).apps
                appId = if(appList.isEmpty()){
                    huaweiCloudApigAppService.create(AppCreateParams(this)).id
                } else {
                    appList[0].id
                }
                logger.debug("init app finish")

                return
            }
        }
        logger.debug("appName is null or empty")
        logger.debug("init app finish")
    }

    fun auths(appId:String,groupId: String,environmentId: String){
        logger.debug("auth api ...")

        val apiList = getApiList(groupId)
        val idList = apiList.map {
            it.id
        }

        if(idList.isNotEmpty()){
            huaweiCloudApigAppService.auths(AuthsParams(idList, listOf(appId),environmentId))
        }



        logger.debug("auth api finish")
    }

}