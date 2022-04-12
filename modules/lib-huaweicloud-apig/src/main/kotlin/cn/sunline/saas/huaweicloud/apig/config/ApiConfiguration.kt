package cn.sunline.saas.huaweicloud.apig.config

import cn.sunline.saas.gateway.api.constant.AuthType
import cn.sunline.saas.gateway.api.constant.ReqMethodType
import cn.sunline.saas.gateway.api.dto.APiCreateParams
import cn.sunline.saas.huaweicloud.apig.config.AppType.*

class ApiConfiguration(private val groupId:String,private val domainUrl:String,private val appType: AppType) {

    val apiParamsList = ArrayList<APiCreateParams>()

    init {
        when(appType){
            MANAGEMENT -> initManagementApi()
            CUSTOMER -> TODO()
        }

    }

    private fun initManagementApi(){
        apiParamsList.add(getApiParams("后管登录","/auth/login",ReqMethodType.POST,"登录接口测试"))

        apiParamsList.add(getApiParams("查询菜单","/menus",ReqMethodType.GET,"查询菜单测试"))

        apiParamsList.add(getApiParams("分页查询权限","/permissions",ReqMethodType.GET,"分页查询权限"))
        apiParamsList.add(getApiParams("查询所有权限","/permissions/all",ReqMethodType.GET,"查询所有权限"))

        apiParamsList.add(getApiParams("分页查询角色","/roles",ReqMethodType.GET,"分页查询角色"))
        apiParamsList.add(getApiParams("查询所有角色","/roles/all",ReqMethodType.GET,"查询所有角色"))
        apiParamsList.add(getApiParams("新增角色","/roles",ReqMethodType.POST,"新增角色"))
        apiParamsList.add(getApiParams("修改角色","/roles/{id}",ReqMethodType.PUT,"修改角色"))

        apiParamsList.add(getApiParams("分页查询用户","/users",ReqMethodType.GET,"分页查询用户"))
        apiParamsList.add(getApiParams("新增用户","/users",ReqMethodType.POST,"新增用户"))
        apiParamsList.add(getApiParams("修改用户","/users/{id}",ReqMethodType.PUT,"修改用户"))

        apiParamsList.add(getApiParams("分页查询产品","/LoanProduct",ReqMethodType.GET,"分页查询产品"))
        apiParamsList.add(getApiParams("新增产品","/LoanProduct",ReqMethodType.POST,"新增产品"))
        apiParamsList.add(getApiParams("修改产品","/LoanProduct/{id}",ReqMethodType.PUT,"修改产品"))
        apiParamsList.add(getApiParams("修改产品状态","/LoanProduct/status/{id}",ReqMethodType.PUT,"修改产品状态"))
        apiParamsList.add(getApiParams("根据产品标识查询产品","/LoanProduct/{identificationCode}/retrieve",ReqMethodType.GET,"根据产品标识查询产品"))
        apiParamsList.add(getApiParams("查询单个产品","/LoanProduct/{id}",ReqMethodType.GET,"查询单个产品"))

        apiParamsList.add(getApiParams("分页查询利率计划","/RatePlan",ReqMethodType.GET,"分页查询利率计划"))
        apiParamsList.add(getApiParams("查询所有利率计划","/RatePlan/all",ReqMethodType.GET,"查询所有利率计划"))
        apiParamsList.add(getApiParams("新增利率计划","/RatePlan",ReqMethodType.POST,"新增利率计划"))
        apiParamsList.add(getApiParams("修改利率计划","/RatePlan/{id}",ReqMethodType.PUT,"修改利率计划"))

        apiParamsList.add(getApiParams("分页查询利率","/InterestRate",ReqMethodType.GET,"分页查询利率"))
        apiParamsList.add(getApiParams("新增利率","/InterestRate",ReqMethodType.POST,"新增利率"))
        apiParamsList.add(getApiParams("修改利率","/InterestRate/{id}",ReqMethodType.PUT,"修改利率"))
        apiParamsList.add(getApiParams("删除利率","/InterestRate/{id}",ReqMethodType.DELETE,"删除利率"))

        apiParamsList.add(getApiParams("查询文件模版目录列表","/DocumentTemplateDirectory",ReqMethodType.GET,"查询文件模版目录列表"))
        apiParamsList.add(getApiParams("新增文件模版目录","/DocumentTemplateDirectory",ReqMethodType.POST,"新增文件模版目录"))
        apiParamsList.add(getApiParams("修改文件模版目录","/DocumentTemplateDirectory/{id}",ReqMethodType.PUT,"修改文件模版目录"))
        apiParamsList.add(getApiParams("删除文件模版目录","/DocumentTemplateDirectory/{id}",ReqMethodType.DELETE,"删除文件模版目录"))

        apiParamsList.add(getApiParams("新增文件模版","/DocumentTemplate",ReqMethodType.POST,"新增文件模版"))
        apiParamsList.add(getApiParams("修改文件模版","/DocumentTemplate/{id}",ReqMethodType.PUT,"修改文件模版"))
        apiParamsList.add(getApiParams("删除文件模版","/DocumentTemplate/{id}",ReqMethodType.DELETE,"删除文件模版"))
        apiParamsList.add(getApiParams("下载文件模版","/DocumentTemplate/download/{id}",ReqMethodType.GET,"下载文件模版"))

        apiParamsList.add(getApiParams("查询贷款申请上传配置列表","/LoanUploadConfigure",ReqMethodType.GET,"查询贷款申请上传配置列表"))
        apiParamsList.add(getApiParams("新增贷款申请上传配置","/LoanUploadConfigure",ReqMethodType.POST,"新增贷款申请上传配置"))
        apiParamsList.add(getApiParams("删除贷款申请上传配置","/LoanUploadConfigure/{id}",ReqMethodType.DELETE,"删除贷款申请上传配置"))

    }

    private fun getApiParams(apiName:String,backendUri:String,reqMethodType: ReqMethodType,remark:String? = null):APiCreateParams{
        return APiCreateParams(apiName,backendUri,reqMethodType,groupId,domainUrl,AuthType.NONE,remark)
    }
}


enum class AppType{
    MANAGEMENT,CUSTOMER
}