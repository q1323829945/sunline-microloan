package cn.sunline.saas.product.invoke.impl

import cn.sunline.saas.product.invoke.ProductInvoke
import cn.sunline.saas.loan.product.model.dto.DTOLoanProduct
import cn.sunline.saas.response.DTOResponseSuccess
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.dapr.client.domain.HttpExtension
import org.springframework.stereotype.Service

@Service
class ProductInvokeImpl: ProductInvoke {

    private val applId = "app-loan-management"

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    override fun getProductInfoByProductId(productId: Long): DTOResponseSuccess<DTOLoanProductView>? {

        val product = DaprHelper.invoke(
            applId,
            "LoanProduct/$productId",
            null,
            HttpExtension.GET,
            DTOResponseSuccess<DTOLoanProductView>()::class.java
        )
        println("---------------------------------------------------------------------------------")
        println("1111111")
        println(objectMapper.valueToTree<JsonNode>(product).toPrettyString())
        println("---------------------------------------------------------------------------------")

        return product
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