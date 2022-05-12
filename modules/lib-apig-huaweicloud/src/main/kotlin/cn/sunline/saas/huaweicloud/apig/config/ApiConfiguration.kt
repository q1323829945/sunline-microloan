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
        apiParamsList.add(getApiParams("login","/auth/login",ReqMethodType.POST,"登录接口"))

        apiParamsList.add(getApiParams("menus","/menus",ReqMethodType.GET,"查询菜单"))

        apiParamsList.add(getApiParams("queryPermissionPaged","/permissions",ReqMethodType.GET,"分页查询权限"))
        apiParamsList.add(getApiParams("queryPermissionAll","/permissions/all",ReqMethodType.GET,"查询所有权限"))

        apiParamsList.add(getApiParams("queryRolePaged","/roles",ReqMethodType.GET,"分页查询角色"))
        apiParamsList.add(getApiParams("queryRoleAll","/roles/all",ReqMethodType.GET,"查询所有角色"))
        apiParamsList.add(getApiParams("addRole","/roles",ReqMethodType.POST,"新增角色"))
        apiParamsList.add(getApiParams("updateRole","/roles/{id}",ReqMethodType.PUT,"修改角色"))

        apiParamsList.add(getApiParams("queryUserPaged","/users",ReqMethodType.GET,"分页查询用户"))
        apiParamsList.add(getApiParams("addUser","/users",ReqMethodType.POST,"新增用户"))
        apiParamsList.add(getApiParams("updateUser","/users/{id}",ReqMethodType.PUT,"修改用户"))

        apiParamsList.add(getApiParams("queryProductPaged","/LoanProduct",ReqMethodType.GET,"分页查询产品"))
        apiParamsList.add(getApiParams("addProduct","/LoanProduct",ReqMethodType.POST,"新增产品"))
        apiParamsList.add(getApiParams("updateProduct","/LoanProduct/{id}",ReqMethodType.PUT,"修改产品"))
        apiParamsList.add(getApiParams("updateProductStatus","/LoanProduct/status/{id}",ReqMethodType.PUT,"修改产品状态"))
        apiParamsList.add(getApiParams("getProductByIdentificationCode","/LoanProduct/{identificationCode}/retrieve",ReqMethodType.GET,"根据产品标识查询产品"))
        apiParamsList.add(getApiParams("getProductById","/LoanProduct/{id}",ReqMethodType.GET,"查询单个产品"))

        apiParamsList.add(getApiParams("queryRatePlanPaged","/RatePlan",ReqMethodType.GET,"分页查询利率计划"))
        apiParamsList.add(getApiParams("queryRatePlanAll","/RatePlan/all",ReqMethodType.GET,"查询所有利率计划"))
        apiParamsList.add(getApiParams("addRatePlan","/RatePlan",ReqMethodType.POST,"新增利率计划"))
        apiParamsList.add(getApiParams("updateRatePlan","/RatePlan/{id}",ReqMethodType.PUT,"修改利率计划"))

        apiParamsList.add(getApiParams("queryInterestRatePaged","/InterestRate",ReqMethodType.GET,"分页查询利率"))
        apiParamsList.add(getApiParams("queryInterestRateAll","/InterestRate",ReqMethodType.POST,"新增利率"))
        apiParamsList.add(getApiParams("addInterestRate","/InterestRate/{id}",ReqMethodType.PUT,"修改利率"))
        apiParamsList.add(getApiParams("updateInterestRate","/InterestRate/{id}",ReqMethodType.DELETE,"删除利率"))

        apiParamsList.add(getApiParams("queryTemplateDirectory","/DocumentTemplateDirectory",ReqMethodType.GET,"查询文件模版目录列表"))
        apiParamsList.add(getApiParams("addTemplateDirectory","/DocumentTemplateDirectory",ReqMethodType.POST,"新增文件模版目录"))
        apiParamsList.add(getApiParams("updateTemplateDirectory","/DocumentTemplateDirectory/{id}",ReqMethodType.PUT,"修改文件模版目录"))
        apiParamsList.add(getApiParams("deleteTemplateDirectory","/DocumentTemplateDirectory/{id}",ReqMethodType.DELETE,"删除文件模版目录"))

        apiParamsList.add(getApiParams("addDocumentTemplate","/DocumentTemplate",ReqMethodType.POST,"新增文件模版"))
        apiParamsList.add(getApiParams("updateDocumentTemplate","/DocumentTemplate/{id}",ReqMethodType.PUT,"修改文件模版"))
        apiParamsList.add(getApiParams("deleteDocumentTemplate","/DocumentTemplate/{id}",ReqMethodType.DELETE,"删除文件模版"))
        apiParamsList.add(getApiParams("downloadDocumentTemplate","/DocumentTemplate/download/{id}",ReqMethodType.GET,"下载文件模版"))

        apiParamsList.add(getApiParams("queryLoanUploadConfigurePaged","/LoanUploadConfigure",ReqMethodType.GET,"查询贷款申请上传配置列表"))
        apiParamsList.add(getApiParams("addLoanUploadConfigure","/LoanUploadConfigure",ReqMethodType.POST,"新增贷款申请上传配置"))
        apiParamsList.add(getApiParams("deleteLoanUploadConfigure","/LoanUploadConfigure/{id}",ReqMethodType.DELETE,"删除贷款申请上传配置"))


        apiParamsList.add(getApiParams("addRiskControlRule","/RiskControlRule",ReqMethodType.POST,"新增风控规则"))
        apiParamsList.add(getApiParams("updateRiskControlRule","/RiskControlRule/{id}",ReqMethodType.PUT,"修改风控规则"))
        apiParamsList.add(getApiParams("deleteRiskControlRule","/RiskControlRule/{id}",ReqMethodType.DELETE,"删除风控规则"))
        apiParamsList.add(getApiParams("queryRiskControlRuleAllList","/RiskControlRule",ReqMethodType.GET,"查询风控规则列表"))
        apiParamsList.add(getApiParams("RiskControlRuleSort","/RiskControlRule/sort",ReqMethodType.PUT,"风控规则排序"))

    }

    private fun getApiParams(apiName:String,backendUri:String,reqMethodType: ReqMethodType,remark:String? = null):APiCreateParams{
        return APiCreateParams(apiName,backendUri,reqMethodType,groupId,domainUrl,AuthType.NONE,remark)
    }
}


enum class AppType{
    MANAGEMENT,CUSTOMER
}