package cn.sunline.saas.product.controller

import cn.sunline.saas.customer.offer.modules.dto.DTOProductView
import cn.sunline.saas.loan.product.model.dto.DTOLoanProduct
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductView
import cn.sunline.saas.product.service.ProductService
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.rpc.invoke.dto.DTOInvokeLoanProduct
import cn.sunline.saas.rpc.invoke.dto.DTOInvokeLoanProducts
import com.fasterxml.jackson.module.kotlin.convertValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("product")
class ProductController {

    @Autowired
    private lateinit var productService: ProductService

    @GetMapping("{identificationCode}/retrieve")
    fun getProduct(@PathVariable identificationCode:String): ResponseEntity<DTOResponseSuccess<MutableList<DTOLoanProduct>>> {
        val product = productService.retrieve(identificationCode)
        return DTOResponseSuccess(product).response()
    }

    @GetMapping("{productId}")
    fun findById(@PathVariable productId: Long): ResponseEntity<DTOResponseSuccess<DTOLoanProductView>> {
        return  DTOResponseSuccess(productService.findById(productId)).response()
    }

    @GetMapping
    fun getProducts(): ResponseEntity<DTOResponseSuccess<List<DTOInvokeLoanProducts>>>{
        return DTOResponseSuccess(productService.getProducts()).response()
    }
}