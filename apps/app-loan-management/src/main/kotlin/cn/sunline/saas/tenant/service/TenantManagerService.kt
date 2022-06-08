package cn.sunline.saas.tenant.service

import cn.sunline.saas.multi_tenant.exception.TenantNotFoundException
import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.tenant.service.dto.DTOTenantChange
import cn.sunline.saas.tenant.service.dto.DTOTenantView
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TenantManagerService {
    @Autowired
    private lateinit var tenantService: TenantService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    fun updateOne(id:Long,dtoTenantChange: DTOTenantChange):DTOTenantView{
        //TODO:permission
        val tenant = tenantService.getOne(id)?: throw TenantNotFoundException("Invalid tenant")
        tenant.enabled = dtoTenantChange.enabled

        return objectMapper.convertValue(tenantService.save(tenant))
    }


}