package cn.sunline.saas.controllers

import cn.sunline.saas.loan.product.model.dto.DTOLoanProductView
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.service.ProductDirectoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * @title: CustomerOfferProcedureController
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/3 14:25
 */
@RestController
@RequestMapping("ProductDirectory")
class ProductController(private val productDirectoryService: ProductDirectoryService) {

    @GetMapping("/Production/{productId}/Retrieve")
    fun retrieve(@PathVariable productId:Long): ResponseEntity<DTOResponseSuccess<DTOLoanProductView>> {
        return productDirectoryService.retrieveProduct(productId)
    }
}