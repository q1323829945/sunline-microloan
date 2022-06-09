package cn.sunline.saas.tenant.controller

import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.multi_tenant.model.Tenant
import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.tenant.service.TenantManagerService
import cn.sunline.saas.tenant.service.dto.DTOTenantChange
import cn.sunline.saas.tenant.service.dto.DTOTenantView
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("tenant")
class TenantController {
    data class DTOTenantAdd(
        val id:Long,
        val country: CountryType,
    )

    @Autowired
    private lateinit var tenantService: TenantService

    @Autowired
    private lateinit var tenantManagerService: TenantManagerService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @PostMapping
    fun addOne(@RequestBody dtoTenantAdd: DTOTenantAdd):ResponseEntity<DTOResponseSuccess<DTOTenantView>>{
        val tenant = objectMapper.convertValue<Tenant>(dtoTenantAdd)
        val saveTenant = tenantService.save(tenant)
        return DTOResponseSuccess(objectMapper.convertValue<DTOTenantView>(saveTenant)).response()
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id:String,@RequestBody dtoTenantChange: DTOTenantChange):ResponseEntity<DTOResponseSuccess<DTOTenantView>>{
        return DTOResponseSuccess(tenantManagerService.updateOne(id.toLong(),dtoTenantChange)).response()
    }
}