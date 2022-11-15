package cn.sunline.saas.rpc.invoke.impl

import cn.sunline.saas.dapr_wrapper.invoke.RPCService
import cn.sunline.saas.dapr_wrapper.constant.APP_LOAN_MANAGEMENT
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.global.util.getUUID
import cn.sunline.saas.global.util.getUserId
import cn.sunline.saas.interest.exception.InterestRateNullException
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.rpc.exception.InterestRateNotFoundException
import cn.sunline.saas.rpc.exception.ProductNotFoundException
import cn.sunline.saas.rpc.invoke.ProductInvoke
import cn.sunline.saas.rpc.invoke.dto.DTOInvokeLoanProduct
import cn.sunline.saas.rpc.invoke.dto.DTOInvokeLoanProducts
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Service

@Service
class ProductInvokeImpl: ProductInvoke {
    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    override fun getProductInfoByProductId(productId: Long): DTOInvokeLoanProduct {

        val loanProductResponse = RPCService.get<DTOResponseSuccess<DTOInvokeLoanProduct>>(
            serviceName = APP_LOAN_MANAGEMENT,
            methodName = "LoanProduct/$productId",
            queryParams = mapOf(),
            headerParams = mapOf(
                Header.TENANT_AUTHORIZATION.key to ContextUtil.getUUID(),
            ),
            tenant = ContextUtil.getTenant()
        )
        // TODO Throw Exception
        loanProductResponse?: run {
            throw ProductNotFoundException("Invalid product !!")
        }
        return loanProductResponse.data!!
    }


    override fun getProductListByIdentificationCode(identificationCode: String): MutableList<DTOInvokeLoanProduct> {
        val loanProductResponse =  RPCService.get<DTOResponseSuccess<MutableList<DTOInvokeLoanProduct>>>(
            serviceName = APP_LOAN_MANAGEMENT,
            methodName = "LoanProduct/$identificationCode/retrieve",
            queryParams = mapOf(),
            headerParams = mapOf(
                Header.TENANT_AUTHORIZATION.key to ContextUtil.getUUID().toString(),
            ),
            tenant = ContextUtil.getTenant().toString()
        )
        // TODO Throw Exception
        return loanProductResponse?.data!!
    }

    override fun getProductsByStatus(status: String): MutableList<DTOInvokeLoanProducts> {
        val products = RPCService.get<MutableList<DTOInvokeLoanProducts>>(
            serviceName = APP_LOAN_MANAGEMENT,
            methodName = "LoanProduct/invoke/list",
            queryParams = mapOf(
                "status" to status
            ),
            headerParams = mapOf(
                Header.TENANT_AUTHORIZATION.key to ContextUtil.getUUID()
            ),
            tenant = ContextUtil.getTenant()
        )

        // TODO Throw Exception
        return products!!
    }

    override fun getInterestRate(productId: Long): List<LoanTermType> {
        return try {
            RPCService.get(
                APP_LOAN_MANAGEMENT,
                "LoanProduct/interestRate/$productId",
                mapOf(),
                headerParams = mapOf(
                    Header.TENANT_AUTHORIZATION.key to ContextUtil.getTenant()
                ),
                tenant = ContextUtil.getTenant().toString()
            )!!
        } catch (e:Exception){
            throw InterestRateNotFoundException("Invalid interest rate!!")
        }
    }
}