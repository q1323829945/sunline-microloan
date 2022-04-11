package cn.sunline.saas.config

import cn.sunline.saas.huaweicloud.apig.constant.*
import cn.sunline.saas.gateway.api.constant.ReqMethodType
import cn.sunline.saas.gateway.api.dto.APiCreateParams

class ApiConfiguration(private val groupId:String,private val domainUrl:String) {

    val apiParamsList = ArrayList<ApiParams>()
    val apiParamsList = ArrayList<APiCreateParams>()

    init {
        val login = getApiParams("后管登录","/auth/login",MatchMode.NORMAL,ReqMethod.POST,"登录接口测试")
        val login = APiCreateParams("后管登录","/auth/login",ReqMethodType.POST,groupId,domainUrl,"登录接口测试")

        val menus = getApiParams("查询菜单","/menus",MatchMode.NORMAL,ReqMethod.GET,"查询菜单测试")
        val menus = APiCreateParams("查询菜单","/menus",ReqMethodType.GET,groupId,domainUrl,"查询菜单测试")

        val getOneProduct = getApiParams("查询单个产品","/LoanProduct/{id}",MatchMode.NORMAL,ReqMethod.GET,"查询单个产品")


        val getOneProduct = APiCreateParams("查询单个产品","/LoanProduct/{id}",ReqMethodType.GET,groupId,domainUrl,"查询单个产品")


        apiParamsList.add(login)
        apiParamsList.add(menus)
//        apiParamsList.add(getOneProduct)
        apiParamsList.add(getOneProduct)
    }

    private fun getApiParams(name:String,uri:String,matchMode: MatchMode,reqMethod: ReqMethod,remark:String? = null):ApiParams{
        return ApiParams(
            group_id = groupId,
            name = name,
            type = 1,
            req_protocol = ReqProtocolEnum.HTTPS,
            req_method = reqMethod,
            req_uri = uri,
            match_mode = matchMode,
            remark = remark,
            auth_type = AuthType.APP,
            backend_type = BackendType.HTTP,
            cors = true,
            backend_api = BackendApi(
                url_domain = domainUrl,
                req_protocol = ReqProtocolEnum.HTTP,
                req_method = reqMethod,
                req_uri = uri,
                timeout = 60000,
                remark = remark,
                vpc_status = 2
            )
        )
    }
}