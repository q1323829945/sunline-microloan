package cn.sunline.saas.controllers

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.loan.product.service.LoanProductService
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal


@RestController
@RequestMapping("product")
class LoanProductController {
    data class DTOLoanProductView(
            var productId:Long,
            val identificationCode:String,
            val name:String,
            val description:String,
            val amountConfiguration:DTOAmountConfigurationView?,
            val termConfiguration:DTOTermConfiguration?,
    )

    data class DTOAmountConfigurationView(
            val maxValueRange:BigDecimal?,
            val minValueRange:BigDecimal?,
    )

    data class DTOTermConfiguration(
            val maxValueRange: LoanTermType?,
            val minValueRange: LoanTermType?,
    )

    @Autowired
    private lateinit var loanProductService: LoanProductService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    @GetMapping("{identificationCode}/retrieve")
    fun getProductInfo(@PathVariable identificationCode:String): ResponseEntity<DTOResponseSuccess<DTOLoanProductView>>{
        val product = loanProductService.findByIdentificationCode(identificationCode)

        val responseProduct = objectMapper.convertValue<DTOLoanProductView>(product)
        responseProduct.productId = product.id
        return DTOResponseSuccess(responseProduct).response()
    }

}