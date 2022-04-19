package cn.sunline.saas.credit.rating.controllers

import cn.sunline.saas.credit.rating.dto.DTOCreditRating
import cn.sunline.saas.credit.rating.service.CustomerCreditRatingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

/**
 * @title: CustomerOfferProcedureController
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/3 14:25
 */
@RestController
@RequestMapping("CreditRating")
class CustomerCreditRatingController {
    @Autowired
    private lateinit var customerCreditRatingService: CustomerCreditRatingService

    @PostMapping
    fun getCreditRating(@RequestBody(required = false) creditRating: DTOCreditRating): Mono<Unit> {
        //TODO:
        println(creditRating)
        return Mono.fromRunnable(){
            customerCreditRatingService.getCreditRating(creditRating)
        }
    }
}