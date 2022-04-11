package cn.sunline.saas.rbac.controller

import cn.sunline.saas.exceptions.ManagementException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.rbac.dto.DTORoleChange
import cn.sunline.saas.rbac.dto.DTORoleView
import cn.sunline.saas.rbac.exception.RoleNotFoundException
import cn.sunline.saas.rbac.modules.Role
import cn.sunline.saas.rbac.modules.User
import cn.sunline.saas.rbac.service.RoleManagerService
import cn.sunline.saas.rbac.services.PermissionService
import cn.sunline.saas.rbac.services.RoleService
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.persistence.criteria.Predicate

@RestController
@RequestMapping("roles")
class RoleController {

    @Autowired
    private lateinit var roleManagerService: RoleManagerService

    @GetMapping
    fun getPaged(pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess> {
        return roleManagerService.getPaged(pageable = pageable)
    }

    @GetMapping("all")
    fun getAll(): ResponseEntity<DTOPagedResponseSuccess> {
        return roleManagerService.getAll()
    }

    @PostMapping
    fun addOne(@RequestBody dtoRole: DTORoleChange): ResponseEntity<DTOResponseSuccess<DTORoleView>> {
        return roleManagerService.addOne(dtoRole)
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id: Long, @RequestBody dtoRole: DTORoleChange): ResponseEntity<DTOResponseSuccess<DTORoleView>> {
        return roleManagerService.updateOne(id,dtoRole)
    }
}