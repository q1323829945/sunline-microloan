package cn.sunline.saas.controller

import cn.sunline.saas.exception.InstanceNotFoundException
import cn.sunline.saas.modules.dto.DTOInstance
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.service.GatewayService
import cn.sunline.saas.service.InstanceService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("instance")
class InstanceController {
    @Autowired
    private lateinit var instanceService: InstanceService

    @Autowired
    private lateinit var gatewayService: GatewayService

    val objectMapper: ObjectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    data class DTOInstanceView(
        val id:String,
        val accessKey:String
    )

    @PostMapping
    fun addOne(@RequestBody dtoInstance: DTOInstance):ResponseEntity<DTOResponseSuccess<DTOInstanceView>>{
        val instance = instanceService.register(dtoInstance)
        val response = objectMapper.convertValue<DTOInstanceView>(instance)
        gatewayService.addOrUpdate(instance.id)
        return DTOResponseSuccess(response).response()
    }

    @GetMapping("{tenant}")
    fun findByTenantAndServer(@PathVariable tenant:String):ResponseEntity<DTOResponseSuccess<DTOInstanceView>>{
        val instance = instanceService.findByTenant(tenant)?:throw InstanceNotFoundException("Invalid instance!")
        val response = objectMapper.convertValue<DTOInstanceView>(instance)
        return DTOResponseSuccess(response).response()
    }

    @GetMapping("{id}")
    fun findById(@PathVariable id:String):ResponseEntity<DTOResponseSuccess<DTOInstanceView>>{
        val instance = instanceService.getInstance(id)?:throw InstanceNotFoundException("Invalid instance!")
        val response = objectMapper.convertValue<DTOInstanceView>(instance)
        return DTOResponseSuccess(response).response()
    }

    @DeleteMapping("{id}")
    fun deleteOne(@PathVariable id:String):ResponseEntity<DTOResponseSuccess<Boolean>>{
        instanceService.deleteInstance(id)
        gatewayService.remove(id)
        return DTOResponseSuccess(true).response()
    }
}