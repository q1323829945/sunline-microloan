package cn.sunline.saas.product.invoke.impl

import cn.sunline.saas.product.invoke.ProductInvoke
import cn.sunline.saas.dapr_wrapper.DaprHelper
import cn.sunline.saas.loan.product.model.dto.DTOLoanProduct
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductView
import cn.sunline.saas.response.DTOResponseSuccess
import io.dapr.client.domain.HttpExtension
import org.springframework.stereotype.Service

@Service
class ProductInvokeImpl: ProductInvoke {

    private val applId = "app-loan-management"

    override fun getProductInfoByProductId(productId: Long): DTOResponseSuccess<DTOLoanProduct>? {
        return DaprHelper.invoke(
            applId,
            "LoanProduct/$productId",
            null,
            HttpExtension.GET,
            DTOResponseSuccess<DTOLoanProduct>()::class.java
        )
    }

    override fun getProductListByIdentificationCode(identificationCode: String): DTOResponseSuccess<MutableList<DTOLoanProduct>>? {
        return DaprHelper.invoke(
            applId,
            "LoanProduct/$identificationCode/retrieve",
            null,
            HttpExtension.GET,
            DTOResponseSuccess<MutableList<DTOLoanProduct>>()::class.java
        )
    }
}