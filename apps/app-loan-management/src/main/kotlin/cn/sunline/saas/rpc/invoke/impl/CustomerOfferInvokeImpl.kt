package cn.sunline.saas.rpc.invoke.impl

import cn.sunline.saas.dapr_wrapper.invoke.RPCService
import cn.sunline.saas.dapr_wrapper.invoke.response.RPCResponse
import cn.sunline.saas.global.constant.APP_LOAN_MANAGEMENT
import cn.sunline.saas.global.constant.APP_MICRO_LOAN
import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.global.util.getUUID
import cn.sunline.saas.global.util.getUserId
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductView
import cn.sunline.saas.rpc.invoke.CustomerOfferInvoke
import cn.sunline.saas.rpc.invoke.dto.DTOFeeItemView
import cn.sunline.saas.rpc.invoke.dto.DTOLoanAgreementView
import cn.sunline.saas.rpc.invoke.dto.DTOLoanAgreementViewInfo
import cn.sunline.saas.rpc.invoke.dto.DTOUnderwriting
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping

@Component
class CustomerOfferInvokeImpl: CustomerOfferInvoke {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    override fun getProduct(productId: Long): DTOLoanProductView? {
        val product =  RPCService.get<RPCResponse<DTOLoanProductView>>(
            serviceName = APP_LOAN_MANAGEMENT,
            methodName = "LoanProduct/$productId",
            queryParams = mapOf(),
            headerParams = mapOf(
                Header.TENANT_AUTHORIZATION.key to ContextUtil.getTenant(),
                Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
            ),
            tenant = ContextUtil.getTenant()
        )

        return product?.data?.let { objectMapper.convertValue(it) }
    }

    override fun getUnderwriting(applicationId: Long): DTOUnderwriting? {
        val underwriting = RPCService.get<RPCResponse<DTOUnderwriting>>(
            serviceName = APP_LOAN_MANAGEMENT,
            methodName = "UnderwritingManagement/$applicationId",
            queryParams = mapOf(),
            headerParams = mapOf(
                Header.TENANT_AUTHORIZATION.key to ContextUtil.getUUID(),
                Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
            ),
            tenant = ContextUtil.getTenant()
        )

        return underwriting?.run { objectMapper.convertValue(underwriting.data) }
    }

    override fun getLoanAgreement(applicationId: Long): DTOLoanAgreementView? {
        return RPCService.get<DTOLoanAgreementView>(
            serviceName = APP_MICRO_LOAN,
            methodName = "ConsumerLoan/LoanAgreement/$applicationId",
            queryParams = mapOf(),
            headerParams = mapOf(
                Header.TENANT_AUTHORIZATION.key to ContextUtil.getUUID(),
                Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
            ),
            tenant = ContextUtil.getTenant()
        )
    }

    override fun getLoanAgreementInfo(applicationId: Long): DTOLoanAgreementViewInfo? {
        return RPCService.get<DTOLoanAgreementViewInfo>(
            serviceName = APP_MICRO_LOAN,
            methodName = "ConsumerLoan/LoanAgreement/$applicationId/retrieve",
            queryParams = mapOf(),
            headerParams = mapOf(
                Header.TENANT_AUTHORIZATION.key to ContextUtil.getUUID(),
                Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
            ),
            tenant = ContextUtil.getTenant()
        )
    }

    override fun getLoanAgreementInfoByAgreementId(agreementId:Long): DTOLoanAgreementViewInfo?{
        return RPCService.get<DTOLoanAgreementViewInfo>(
            serviceName = APP_MICRO_LOAN,
            methodName = "ConsumerLoan/LoanAgreement/Info/$agreementId/retrieve",
            queryParams = mapOf(),
            headerParams = mapOf(
                Header.TENANT_AUTHORIZATION.key to ContextUtil.getUUID(),
                Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
            ),
            tenant = ContextUtil.getTenant()
        )
    }

    override fun getFeeItemListByAgreementId(agreementId:Long): List<DTOFeeItemView>?{
        return RPCService.get<MutableList<DTOFeeItemView>>(
            serviceName = APP_MICRO_LOAN,
            methodName = "ConsumerLoan/fee/item/$agreementId/retrieve",
            queryParams = mapOf(),
            headerParams = mapOf(
                Header.TENANT_AUTHORIZATION.key to ContextUtil.getUUID(),
                Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
            ),
            tenant = ContextUtil.getTenant()
        )
    }
}