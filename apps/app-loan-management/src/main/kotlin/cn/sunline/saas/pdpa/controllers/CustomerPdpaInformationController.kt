package cn.sunline.saas.pdpa.controllers

import cn.sunline.saas.pdpa.modules.dto.DTOCustomerPdpaInformation
import cn.sunline.saas.pdpa.modules.dto.DTOCustomerPdpaInformationChange
import cn.sunline.saas.pdpa.services.CustomerPdpaInformationService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("customer/pdpa")
class CustomerPdpaInformationController {
    @Autowired
    private lateinit var customerPdpaInformationService: CustomerPdpaInformationService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @PostMapping
    fun getAndRegister(@PathVariable dtoCustomerPdpaInformation: DTOCustomerPdpaInformation):DTOCustomerPdpaInformation{
        val customerPdpaInformation = customerPdpaInformationService.getAndRegisterCustomerPdpaInformation(dtoCustomerPdpaInformation)
        return objectMapper.convertValue(customerPdpaInformation)
    }

    @PutMapping("confirm/{id}")
    fun confirm(@PathVariable id:String,dtoCustomerPdpaInformationChange: DTOCustomerPdpaInformationChange):DTOCustomerPdpaInformation{
        val customerPdpaInformation = customerPdpaInformationService.confirm(id.toLong(), dtoCustomerPdpaInformationChange)
        return objectMapper.convertValue(customerPdpaInformation)
    }

    @PutMapping("withdraw/{id}")
    fun withdraw(@PathVariable id: String):DTOCustomerPdpaInformation{
        val customerPdpaInformation = customerPdpaInformationService.withdraw(id.toLong())
        return objectMapper.convertValue(customerPdpaInformation)
    }
}