package cn.sunline.saas.product.controller

import cn.sunline.saas.customer.offer.modules.dto.DTOProductView
import cn.sunline.saas.product.service.ProductService
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
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
    fun getProduct(@PathVariable identificationCode:String): ResponseEntity<DTOResponseSuccess<DTOProductView>> {
        val product = productService.retrieve(identificationCode)

        return DTOResponseSuccess(product).response()
    }
}