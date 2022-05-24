package cn.sunline.saas.dapr_wrapper.invoke.request

import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant

/**
 * @title: RPCRequestWithTenant
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/13 11:20
 */
abstract class RPCRequestWithTenant(tenant: String = ContextUtil.getTenant()) : RPCRequest(tenant) {
}