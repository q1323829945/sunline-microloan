package cn.sunline.saas.rpc.invoke.impl

import cn.sunline.saas.dapr_wrapper.invoke.RPCService
import cn.sunline.saas.dapr_wrapper.invoke.response.RPCResponse
import cn.sunline.saas.dapr_wrapper.constant.APP_LOAN_MANAGEMENT
import cn.sunline.saas.global.constant.LanguageType
import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.global.util.getUUID
import cn.sunline.saas.global.util.getUserId
import cn.sunline.saas.pdpa.exception.PDPANotFoundException
import cn.sunline.saas.rpc.invoke.PdpaInvoke
import cn.sunline.saas.rpc.invoke.dto.DTOCustomerPdpaInformation
import cn.sunline.saas.rpc.invoke.dto.DTOPdpaAuthority
import cn.sunline.saas.rpc.invoke.dto.DTOPdpaView
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class PdpaInvokeImpl: PdpaInvoke {
    private val logger = KotlinLogging.logger {  }

    override fun getPDPAInformation(country: CountryType, language: LanguageType): RPCResponse<DTOPdpaView> {
        return try {
            RPCService.get<RPCResponse<DTOPdpaView>>(
                serviceName = APP_LOAN_MANAGEMENT,
                methodName = "pdpa/${country.name}/${language.name}/retrieve",
                queryParams = mapOf(),
                headerParams = mapOf(
                    Header.TENANT_AUTHORIZATION.key to ContextUtil.getUUID(),
                ),
                tenant = ContextUtil.getTenant()
            )!!
        } catch (e:Exception){
            logger.error { e.localizedMessage }
            throw PDPANotFoundException("Invalid pdpa!!")
        }

    }

    override fun getPDPAInformation(pdpaId: String): RPCResponse<DTOPdpaView>? {
        return try {
            RPCService.get<RPCResponse<DTOPdpaView>>(
                serviceName = APP_LOAN_MANAGEMENT,
                methodName = "pdpa/$pdpaId",
                queryParams = mapOf(),
                headerParams = mapOf(
                    Header.TENANT_AUTHORIZATION.key to ContextUtil.getUUID(),
                ),
                tenant = ContextUtil.getTenant()
            )
        } catch (e:Exception){
            logger.error { e.localizedMessage }
            throw PDPANotFoundException("Invalid pdpa!!")
        }
    }

    override fun getCustomerPdpaAuthorityInformation(customerId: String): DTOCustomerPdpaInformation? {
        return try {
            RPCService.get<DTOCustomerPdpaInformation>(
                serviceName = APP_LOAN_MANAGEMENT,
                methodName = "customer/pdpa/$customerId",
                queryParams = mapOf(),
                headerParams = mapOf(
                    Header.TENANT_AUTHORIZATION.key to ContextUtil.getUUID(),
                ),
                tenant = ContextUtil.getTenant()
            )
        } catch (e:Exception){
            logger.error { e.localizedMessage }
            throw PDPANotFoundException("Invalid pdpa!!")
        }
    }

    override fun getPdpaAuthority(): RPCResponse<DTOPdpaAuthority>? {

        return try {
            RPCService.get<RPCResponse<DTOPdpaAuthority>>(
                serviceName = APP_LOAN_MANAGEMENT,
                methodName = "pdpa/authority",
                queryParams = mapOf(),
                headerParams = mapOf(
                    Header.TENANT_AUTHORIZATION.key to ContextUtil.getUUID(),
                ),
                tenant = ContextUtil.getTenant()
            )
        } catch (e:Exception){
            logger.error { e.localizedMessage }
            throw PDPANotFoundException("Invalid pdpa!!")
        }

    }

}