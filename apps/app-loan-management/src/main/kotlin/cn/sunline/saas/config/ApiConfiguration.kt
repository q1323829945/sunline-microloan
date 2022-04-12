package cn.sunline.saas.config

import cn.sunline.saas.huaweicloud.apig.constant.*

class ApiConfiguration(private val groupId:String,private val domainUrl:String) {

    val apiParamsList = ArrayList<ApiParams>()

    init {
        val login = getApiParams("后管登录","/auth/login",MatchMode.NORMAL,ReqMethod.POST,"登录接口测试")

        val menus = getApiParams("查询菜单","/menus",MatchMode.NORMAL,ReqMethod.GET,"查询菜单测试")

        apiParamsList.add(login)
        apiParamsList.add(menus)
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