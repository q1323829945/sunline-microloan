package cn.sunline.saas.tenant.controller

import cn.sunline.saas.tenant.service.TenantManagerService
import cn.sunline.saas.tenant.service.dto.DTOTenant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("tenant")
class TenantController {
    @Autowired
    private lateinit var tenantManagerService: TenantManagerService

    @PostMapping
    fun subscribeTenant(@RequestBody dtoTenant: DTOTenant){
        tenantManagerService.saveTenant(dtoTenant)
    }


}