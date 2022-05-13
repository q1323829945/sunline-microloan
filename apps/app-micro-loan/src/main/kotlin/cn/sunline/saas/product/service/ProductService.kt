package cn.sunline.saas.product.service

import cn.sunline.saas.customer.offer.modules.dto.DTOProductView
import cn.sunline.saas.product.invoke.ProductInvoke
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging
import cn.sunline.saas.global.constant.HttpRequestMethod
import cn.sunline.saas.loan.product.model.dto.DTOLoanProduct
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductView
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Service

@Service
class ProductService(private val productInvoke: ProductInvoke)  {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun findById(productId: Long): DTOLoanProductView {
//        val uri = "http://${ipConfig.productIp}/LoanProduct/$productId"
//
//        val postMethod = appHttpConfiguration.getHttpMethod(HttpRequestMethod.GET, uri,appHttpConfiguration.getPublicHeaders())
//
//        appHttpConfiguration.sendClient(postMethod)
//
//        val data = appHttpConfiguration.getResponse(postMethod)
//
//        val dtoProductView = Gson().fromJson(data, DTOProductView::class.java)
//        dtoProductView.productId = productId
//
//        return dtoProductView
        val dtoLoanProductViewResponse = productInvoke.getProductInfoByProductId(productId)


        println("---------------------------------------------------------------------------------")
        println("2222222")
        println(objectMapper.valueToTree<JsonNode>(dtoLoanProductViewResponse).toPrettyString())
        println("---------------------------------------------------------------------------------")
        return objectMapper.convertValue(dtoLoanProductViewResponse!!.data!!)
    }

    fun retrieve(identificationCode: String): MutableList<DTOLoanProduct> {
        val dtoLoanProductViewResponse = productInvoke.getProductListByIdentificationCode(identificationCode)
        return objectMapper.convertValue(dtoLoanProductViewResponse!!.data!!)
    }

}
