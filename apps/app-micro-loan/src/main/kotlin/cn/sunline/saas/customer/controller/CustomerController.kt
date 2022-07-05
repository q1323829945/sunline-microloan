package cn.sunline.saas.customer.controller

import cn.sunline.saas.rbac.modules.dto.DTOCustomerAdd
import cn.sunline.saas.rbac.modules.dto.DTOCustomerView
import cn.sunline.saas.rbac.services.CustomerService
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("Customer")
class CustomerController {

    @Autowired
    lateinit var customerService: CustomerService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    @PostMapping
    fun addOne(@RequestBody dtoCustomerAdd: DTOCustomerAdd):ResponseEntity<DTOResponseSuccess<DTOCustomerView>>{
        val customer = customerService.addOne(dtoCustomerAdd)

        return DTOResponseSuccess(objectMapper.convertValue<DTOCustomerView>(customer)).response()
    }


    @GetMapping("{userId}")
    fun getOneByUserid(@PathVariable("userId") userId:String):ResponseEntity<DTOResponseSuccess<DTOCustomerView>>{
        val customer = customerService.findByUserId(userId)

        var dtoCustomer:DTOCustomerView? = null

        return if(customer == null){
            DTOResponseSuccess(dtoCustomer).response()
        } else {
            dtoCustomer = objectMapper.convertValue<DTOCustomerView>(customer)
            DTOResponseSuccess(dtoCustomer).response()
        }
    }
}