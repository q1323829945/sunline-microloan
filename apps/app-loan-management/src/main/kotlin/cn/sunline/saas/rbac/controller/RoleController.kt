package cn.sunline.saas.rbac.controller

import cn.sunline.saas.rbac.modules.Role
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

@RestController
@RequestMapping("roles")
class RoleController {
    data class DTOPermissionView(val id:Long,val name: String)
    data class DTORoleChange(val name: String, val remark: String, val permissionList: List<Long> = listOf())
    data class DTORoleView(val id: Long, val name: String, val remark: String, val permissions: List<DTOPermissionView>)

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var permissionService: PermissionService

    @GetMapping
    fun getPaged(pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess> {
        val page = roleService.getPaged(pageable = pageable)
        return DTOPagedResponseSuccess(page.map { objectMapper.convertValue<DTORoleView>(it)}).response()
    }

    @PostMapping
    fun addOne(@RequestBody dtoRole: DTORoleChange): ResponseEntity<DTOResponseSuccess<DTORoleView>> {
        val role = objectMapper.convertValue<Role>(dtoRole)
        val savedRole = roleService.save(role)
        val responseRole = objectMapper.convertValue<DTORoleView>(savedRole)
        return DTOResponseSuccess(responseRole).response()
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id: Long, @RequestBody dtoRole: DTORoleChange): ResponseEntity<DTOResponseSuccess<DTORoleView>> {
        val oldRole = roleService.getOne(id)?: throw NotFoundException("Invalid role")        val newRole = objectMapper.convertValue<Role>(dtoRole)

        if (dtoRole.permissionList.isEmpty()) {
            newRole.permissions.clear()
        } else {
            newRole.permissions = permissionService.getByIds(dtoRole.permissionList).toMutableList()
        }

        val savedRole = roleService.updateOne(oldRole, newRole)
        val responseRole = objectMapper.convertValue<DTORoleView>(savedRole)
        return DTOResponseSuccess(responseRole).response()
    }
}