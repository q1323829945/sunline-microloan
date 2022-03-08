package cn.sunline.saas.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

/**
 * @title: CustomerOfferProcedureController
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/3 14:25
 */
@RestController
@RequestMapping("ProductDirectory")
class ProductController {

    @GetMapping("/Production/{productId}/Retrieve")
    fun retrieve(@RequestParam productId:Long): ResponseEntity<Unit> {
        return ResponseEntity.ok(Unit)
    }
}