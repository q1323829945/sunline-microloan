package cn.sunline.saas.product.service

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.rpc.invoke.ProductInvoke
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import cn.sunline.saas.loan.product.model.dto.DTOLoanProduct
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductView
import cn.sunline.saas.rpc.invoke.dto.DTOInvokeLoanProducts
import org.springframework.stereotype.Service

@Service
class ProductService(private val productInvoke: ProductInvoke)  {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun findById(productId: Long): DTOLoanProductView {
        val dtoLoanProductViewResponse = productInvoke.getProductInfoByProductId(productId)
        return objectMapper.convertValue(dtoLoanProductViewResponse)
    }

    fun retrieve(identificationCode: String): MutableList<DTOLoanProduct> {
        val dtoLoanProductViewResponse = productInvoke.getProductListByIdentificationCode(identificationCode)
        return objectMapper.convertValue(dtoLoanProductViewResponse)
    }

    fun getProducts():List<DTOInvokeLoanProducts>{
        return productInvoke.getProductsByStatus("INITIATED")
    }

    fun getInterestRateByProductId(productId: String):List<LoanTermType>{
        return productInvoke.getInterestRate(productId.toLong())
    }

}
