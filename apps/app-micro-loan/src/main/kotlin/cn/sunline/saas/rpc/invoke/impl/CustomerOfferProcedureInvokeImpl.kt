package cn.sunline.saas.rpc.invoke.impl

import cn.sunline.saas.dapr_wrapper.invoke.RPCService
import cn.sunline.saas.dapr_wrapper.constant.APP_LOAN_MANAGEMENT
import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.global.util.getUUID
import cn.sunline.saas.global.util.getUserId
import cn.sunline.saas.rpc.exception.UploadConfigNotFoundException
import cn.sunline.saas.rpc.invoke.CustomerOfferProcedureInvoke
import cn.sunline.saas.rpc.invoke.dto.DTOProductUploadConfig
import mu.KotlinLogging
import org.springframework.stereotype.Component

@Component
class CustomerOfferProcedureInvokeImpl: CustomerOfferProcedureInvoke {
    private val logger = KotlinLogging.logger {  }

    override fun getProductUploadConfig(productId: Long): List<DTOProductUploadConfig> {
        return try {
            RPCService.get(
                APP_LOAN_MANAGEMENT,
                "LoanProduct/uploadConfig/$productId",
                mapOf(),
                headerParams = mapOf(
                    Header.TENANT_AUTHORIZATION.key to ContextUtil.getUUID().toString(),
                ),
                tenant = ContextUtil.getTenant().toString()
            )!!
        } catch (e :Exception){
            logger.error { e.localizedMessage }
            throw UploadConfigNotFoundException("Invalid uploadConfig!!")
        }

    }


}