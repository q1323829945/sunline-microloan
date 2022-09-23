package cn.sunline.saas.underwriting.invoke.impl.dto

import cn.sunline.saas.dapr_wrapper.invoke.request.RPCRequestWithTenant
import cn.sunline.saas.global.constant.APP_LOAN_MANAGEMENT
import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.global.util.getUUID
import cn.sunline.saas.global.util.getUserId

/**
 * @title: DTOPartnerIntegrated
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/13 16:50
 */
class PartnerIntegratedRPCRequest : RPCRequestWithTenant() {
    override fun getQueryParams(): Map<String, String> {
        return mapOf()
    }

    override fun getHeaderParams(): Map<String, String> {
        return mapOf(
            Header.TENANT_AUTHORIZATION.key to ContextUtil.getUUID(),
            Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
        )
    }

    override fun getPayload(): Any? {
        return null
    }

    override fun getModuleName(): String {
        return APP_LOAN_MANAGEMENT
    }

    override fun getMethodName(): String {
        return "PartnerIntegrated/Retrieve"
    }
}
