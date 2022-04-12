package cn.sunline.saas.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.security.cert.Certificate
import java.util.Currency

/**
 * @title: CustomerOfferProcedureController
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/3 14:25
 */
@RestController
@RequestMapping("CustomerOffer")
class CustomerOfferProcedureController {

    data class DTOCustomer(
        val customerId: Long,
        val name: String,
        val certificateType: String,
        val certificateNo: String
    )

    data class DTOEmployeeOrBusinessUnit(val businessUntil: String, val employee: String)

    data class DTOCustomerAgreement(
        val amount: BigDecimal,
        val currency: String,
        val term: String,
        val purpose: String,
        val calculateRepaymentScheduleType: String,
    )

    data class DTOCustomerOfferProcedure(
        val customer: DTOCustomer,
        val employeeOrBusinessUnit: DTOEmployeeOrBusinessUnit,
        val customerAgreement: DTOCustomerAgreement
    )


    @PostMapping("/Initiate")
    fun initiate(@RequestBody dtoCustomerOfferProcedure: DTOCustomerOfferProcedure): ResponseEntity<Unit> {
        return ResponseEntity.ok(Unit)
    }
}