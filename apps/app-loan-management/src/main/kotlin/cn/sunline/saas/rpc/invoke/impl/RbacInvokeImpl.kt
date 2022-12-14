package cn.sunline.saas.rpc.invoke.impl

import cn.sunline.saas.dapr_wrapper.invoke.RPCService
import cn.sunline.saas.dapr_wrapper.constant.APP_LOAN_MANAGEMENT
import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.global.util.getUUID
import cn.sunline.saas.global.util.getUserId
import cn.sunline.saas.rbac.service.dto.DTOPerson
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.rpc.invoke.RbacInvoke
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Component

@Component
class RbacInvokeImpl: RbacInvoke {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    override fun getPerson(id: Long?): DTOPerson? {

        id?.run {
            val person = RPCService.get<DTOResponseSuccess<DTOPerson>>(
                serviceName = APP_LOAN_MANAGEMENT,
                methodName = "Person/$id",
                queryParams = mapOf(),
                headerParams = mapOf(
                    Header.TENANT_AUTHORIZATION.key to ContextUtil.getUUID(),
                    Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
                ),
                tenant = ContextUtil.getTenant(),
            )

            return if(person == null){
                null
            } else {
                objectMapper.convertValue(person.data!!)
            }
        }

        return null
    }
}