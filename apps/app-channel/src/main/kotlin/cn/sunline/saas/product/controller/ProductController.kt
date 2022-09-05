package cn.sunline.saas.product.controller

import cn.sunline.saas.global.constant.ProductType
import cn.sunline.saas.channel.product.model.dto.DTOProductAdd
import cn.sunline.saas.channel.product.model.dto.DTOProductChange
import cn.sunline.saas.channel.product.model.dto.DTOProductSimpleView
import cn.sunline.saas.channel.product.model.dto.DTOProductView
import cn.sunline.saas.channel.product.service.ProductService
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.websocket.server.PathParam


@RestController
@RequestMapping("product")
class ProductController {
    @Autowired
    private lateinit var productService: ProductService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @PostMapping
    fun addOne(@RequestBody dtoProductAdd: DTOProductAdd): ResponseEntity<DTOResponseSuccess<DTOProductView>> {
        val product = productService.addOne(dtoProductAdd)
        return DTOResponseSuccess(product).response()
    }

    @PutMapping
    fun updateOne(@RequestBody dtoProductChange: DTOProductChange): ResponseEntity<DTOResponseSuccess<DTOProductView>> {
        val product = productService.updateOne(dtoProductChange)
        return DTOResponseSuccess(product).response()
    }

    @GetMapping("paged")
    fun paged(pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess> {
        val paged = productService.paged(pageable)
        return DTOPagedResponseSuccess(paged.map { it }).response()
    }

    @GetMapping
    fun getOne(@PathParam("productType") productType: ProductType): ResponseEntity<DTOResponseSuccess<DTOProductView>> {
        val product = productService.getProductByProductType(productType)
        return DTOResponseSuccess(product).response()
    }

    @GetMapping("{id}")
    fun getOne(@PathVariable id: String): ResponseEntity<DTOResponseSuccess<DTOProductView>> {
        val product = productService.getProduct(id.toLong())
        return DTOResponseSuccess(product).response()
    }

    @GetMapping("all")
    fun getAll(): ResponseEntity<DTOResponseSuccess<List<DTOProductSimpleView>>> {
        val paged = productService.paged(Pageable.unpaged())
        val map = paged.content.map { objectMapper.convertValue<DTOProductSimpleView>(it) }
        return DTOResponseSuccess(map).response()
    }
}