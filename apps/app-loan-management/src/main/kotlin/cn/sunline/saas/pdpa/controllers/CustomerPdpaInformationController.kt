package cn.sunline.saas.pdpa.controllers

import cn.sunline.saas.pdpa.modules.dto.DTOCustomerPdpaInformation
import cn.sunline.saas.pdpa.modules.dto.DTOCustomerPdpaInformationChange
import cn.sunline.saas.pdpa.services.CustomerPdpaInformationService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("customer/pdpa")
class CustomerPdpaInformationController {

    data class Withdraw(
        val id:String
    )
    @Autowired
    private lateinit var customerPdpaInformationService: CustomerPdpaInformationService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @GetMapping("{id}")
    fun getOne(@PathVariable id:String):DTOCustomerPdpaInformation{
        val customerPdpaInformation = customerPdpaInformationService.getAndRegisterCustomerPdpaInformation(id.toLong())
        return objectMapper.convertValue(customerPdpaInformation)
    }

    @PostMapping("confirm")
    fun confirm(@RequestBody dtoCustomerPdpaInformationChange: DTOCustomerPdpaInformationChange):DTOCustomerPdpaInformation{
        val customerPdpaInformation = customerPdpaInformationService.confirm(dtoCustomerPdpaInformationChange.customerId.toLong(), dtoCustomerPdpaInformationChange)
        return objectMapper.convertValue(customerPdpaInformation)
    }

    @PostMapping("withdraw")
    fun withdraw(@RequestBody withdraw:Withdraw):DTOCustomerPdpaInformation{
        val customerPdpaInformation = customerPdpaInformationService.withdraw(withdraw.id.toLong())
        return objectMapper.convertValue(customerPdpaInformation)
    }


}