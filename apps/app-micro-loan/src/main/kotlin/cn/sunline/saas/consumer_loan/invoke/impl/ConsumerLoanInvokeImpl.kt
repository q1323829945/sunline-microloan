package cn.sunline.saas.consumer_loan.invoke.impl


import cn.sunline.saas.consumer_loan.invoke.ConsumerLoanInvoke
import cn.sunline.saas.consumer_loan.invoke.dto.DTOCustomerOffer
import cn.sunline.saas.consumer_loan.invoke.dto.DTOLoanProduct
import cn.sunline.saas.dapr_wrapper.invoke.RPCService
import cn.sunline.saas.global.constant.APP_LOAN_MANAGEMENT
import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.global.util.getUserId
import cn.sunline.saas.interest.model.InterestRate
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Component

@Component
class ConsumerLoanInvokeImpl: ConsumerLoanInvoke {
    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    override fun retrieveCustomerOffer(applicationId: Long): DTOCustomerOffer {
        return RPCService.get<DTOCustomerOffer>(
            serviceName = APP_LOAN_MANAGEMENT,
            methodName = "CustomerOffer/invoke/$applicationId",
            queryParams = mapOf(),
            headerParams = mapOf(
                Header.TENANT_AUTHORIZATION.key to ContextUtil.getTenant().toString(),
                Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
            ),
            tenant = ContextUtil.getTenant().toString()

        )!!
    }

    override fun retrieveLoanProduct(productId: Long): DTOLoanProduct {
        return  RPCService.get<DTOLoanProduct>(
            serviceName = APP_LOAN_MANAGEMENT,
            methodName = "LoanProduct/invoke/$productId",
            queryParams = mapOf(),
            headerParams = mapOf(
                Header.TENANT_AUTHORIZATION.key to ContextUtil.getTenant().toString(),
                Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
            ),
            tenant = ContextUtil.getTenant().toString()
        )!!
    }

    override fun retrieveBaseInterestRate(ratePlanId: Long): MutableList<InterestRate>? {
        return  RPCService.get<MutableList<InterestRate>>(
            serviceName = APP_LOAN_MANAGEMENT,
            methodName = "InterestRate/all",
            queryParams = mapOf("ratePlanId" to ratePlanId.toString()),
            headerParams = mapOf(
                Header.TENANT_AUTHORIZATION.key to ContextUtil.getTenant().toString(),
                Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
            ),
            tenant = ContextUtil.getTenant().toString()
        )
    }
}