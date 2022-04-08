package cn.sunline.saas.runner

import cn.sunline.saas.config.ApiConfiguration
import cn.sunline.saas.gateway.api.GatewayApi
import cn.sunline.saas.gateway.api.GatewayApp
import cn.sunline.saas.gateway.api.GatewayEnvironment
import cn.sunline.saas.gateway.api.GatewayGroup
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

    override fun run(vararg args: String?) {
        environmentId = reloadEnvironment()
        groupId = reloadGroup()
        val appId = reloadApp()

        reloadApi(groupId!!)
        onlineApi(environmentId!!, groupId!!)

        appId?.run {
            auths(this,groupId!!, environmentId!!)
        }
    }

    fun reloadEnvironment():String{
        println("init environment ...")
        var environmentId = huaweiCloudApigEnvironmentService.getOne(environmentName)
        environmentId?:run{
            environmentId = huaweiCloudApigEnvironmentService.create(EnvironmentCreateParams(environmentName))
        }
        println("init environment finish")

        return environmentId.toString()
    }

    fun reloadGroup():String{
        println("init group ...")
        var groupId = huaweiCloudApigGroupService.getOne(groupName)
        groupId?:run{
            groupId = huaweiCloudApigGroupService.create(GroupCreateParams(groupName))
        }
        println("init group finish")

        return groupId.toString()

    }

    fun reloadApi(groupId:String){
        println("init api ...")

        val apiConfiguration = ApiConfiguration(groupId,ip)

        val apiList = getApiList(groupId)

        registerApi(apiConfiguration, apiList)

        deleteApi(apiConfiguration, apiList)

        println("init api finish")

    }

    fun registerApi(apiConfiguration:ApiConfiguration,apiList:List<*>){

        println("register api ...")
        val nameList = apiList.map {
            it as ApiResponsePage
            it.name
        }

        apiConfiguration.apiParamsList.forEach{
            if(!nameList.contains(it.name)){
                huaweiCloudApigApiService.register(it)
            }
        }
        println("register api finish")
    }

    fun deleteApi(apiConfiguration:ApiConfiguration,apiList:List<*>){
        println("delete api ...")
        val nameList = apiConfiguration.apiParamsList.map {
            it.name
        }

        apiList.forEach {
            it as ApiResponsePage
            if(!nameList.contains(it.name)){
                huaweiCloudApigApiService.delete(it.id)
            }
        }

        println("delete api finish")
    }

    fun onlineApi(environmentId:String,groupId:String){
        println("api online ...")


        val apiList = getApiList(groupId)

        val idList = apiList.map {
            it as ApiResponsePage
            it.id
        }

        huaweiCloudApigApiService.batchPublish(BatchPublishParams(
            ActionType.online,
            idList,
            environmentId
        ))

        println("api online finish")
    }

    fun getApiList(groupId: String): List<*> {
        val pageParams = ApiPageParams(
            page_size = 0,
            group_id = groupId
        )

        return huaweiCloudApigApiService.getPaged(pageParams) as List<*>
    }

    fun reloadApp():String?{
        println("init app ...")
        appName?.run {
            var appId = huaweiCloudApigAppService.getOne(this)
            appId?:run{
                appId = huaweiCloudApigAppService.create(AppCreateParams(this))
            }
            println("init app finish")

            return appId.toString()
        }

        println("appName is null ")
        return null
    }

    fun auths(appId:String,groupId: String,environmentId: String){
        println("auth api ...")

        val apiList = getApiList(groupId)
        val idList = apiList.map {
            it as ApiResponsePage
            it.id
        }

        huaweiCloudApigAppService.auths(AuthsParams(idList, listOf(appId),environmentId))


        println("auth api finish")
    }

}