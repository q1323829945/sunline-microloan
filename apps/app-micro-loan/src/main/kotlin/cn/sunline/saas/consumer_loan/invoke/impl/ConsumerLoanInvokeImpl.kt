package cn.sunline.saas.consumer_loan.invoke.impl


import cn.sunline.saas.consumer_loan.invoke.ConsumerLoanInvoke
import cn.sunline.saas.consumer_loan.invoke.dto.DTOCustomerOffer
import cn.sunline.saas.consumer_loan.invoke.dto.DTOLoanProduct
import cn.sunline.saas.customer.offer.exceptions.CustomerOfferNotFoundException
import cn.sunline.saas.dapr_wrapper.invoke.RPCService
import cn.sunline.saas.dapr_wrapper.constant.APP_LOAN_MANAGEMENT
import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.global.util.getUUID
import cn.sunline.saas.global.util.getUserId
import cn.sunline.saas.interest.model.InterestRate
import cn.sunline.saas.rpc.exception.InterestRateNotFoundException
import cn.sunline.saas.rpc.exception.ProductNotFoundException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging
import org.springframework.stereotype.Component

@Component
class ConsumerLoanInvokeImpl: ConsumerLoanInvoke {
    private val logger = KotlinLogging.logger {  }
    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    override fun retrieveCustomerOffer(applicationId: Long): DTOCustomerOffer {
        return try {
            RPCService.get<DTOCustomerOffer>(
                serviceName = APP_LOAN_MANAGEMENT,
                methodName = "CustomerOffer/invoke/$applicationId",
                queryParams = mapOf(),
                headerParams = mapOf(
                    Header.TENANT_AUTHORIZATION.key to ContextUtil.getUUID().toString(),
                    Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
                ),
                tenant = ContextUtil.getTenant().toString()

            )!!

        } catch (e:Exception){
            logger.error { e.localizedMessage }
            throw CustomerOfferNotFoundException("Invalid customer offer")
        }
    }

    override fun retrieveLoanProduct(productId: Long): DTOLoanProduct {

        return try {
            RPCService.get<DTOLoanProduct>(
                serviceName = APP_LOAN_MANAGEMENT,
                methodName = "LoanProduct/invoke/$productId",
                queryParams = mapOf(),
                headerParams = mapOf(
                    Header.TENANT_AUTHORIZATION.key to ContextUtil.getUUID().toString(),
                    Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
                ),
                tenant = ContextUtil.getTenant().toString()
            )!!
        } catch (e:Exception){
            logger.error { e.localizedMessage }
            throw ProductNotFoundException("Invalid product !!")
        }
    }

    override fun retrieveBaseInterestRate(ratePlanId: Long): MutableList<InterestRate>? {
        return try {
            RPCService.get<MutableList<InterestRate>>(
                serviceName = APP_LOAN_MANAGEMENT,
                methodName = "InterestRate/all",
                queryParams = mapOf("ratePlanId" to ratePlanId.toString()),
                headerParams = mapOf(
                    Header.TENANT_AUTHORIZATION.key to ContextUtil.getUUID().toString(),
                    Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
                ),
                tenant = ContextUtil.getTenant().toString()
            )
        } catch (e:Exception){
            logger.error { e.localizedMessage }
            throw InterestRateNotFoundException("Invalid interest rate!!")
        }

    }
}