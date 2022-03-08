package cn.sunline.saas.controllers

import cn.sunline.saas.rbac.modules.Role
import cn.sunline.saas.rbac.services.PermissionService
import cn.sunline.saas.rbac.services.RoleService
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
    data class DTOPermissionView(val name: String)
    data class DTORoleChange(val name: String, val remark: String, val permissionList: List<Long> = listOf())
    data class DTORoleView(val id: Long, val name: String, val remark: String, val permissions: List<DTOPermissionView>)

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var permissionService: PermissionService

    @GetMapping
    fun getPaged(pageable: Pageable): ResponseEntity<Any> {
        return ResponseEntity.ok(roleService.getPaged(pageable = pageable).map { objectMapper.convertValue<DTORoleView>(it) })
    }

    @PostMapping
    fun addOne(@RequestBody dtoRole: DTORoleChange): ResponseEntity<DTORoleView> {
        val role = objectMapper.convertValue<Role>(dtoRole)
        val savedRole = roleService.save(role)
        val responseRole = objectMapper.convertValue<DTORoleView>(savedRole)
        return ResponseEntity.ok(responseRole)
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id: Long, @RequestBody dtoRole: DTORoleChange): ResponseEntity<DTORoleView> {
        val oldRole = roleService.getOne(id)?: throw Exception("Invalid role")
        val newRole = objectMapper.convertValue<Role>(dtoRole)

        if (dtoRole.permissionList.isEmpty()) {
            newRole.permissions.clear()
        } else {
            newRole.permissions = permissionService.getByIds(dtoRole.permissionList).toMutableList()
        }

        val savedRole = roleService.updateOne(oldRole, newRole)
        val responseRole = objectMapper.convertValue<DTORoleView>(savedRole)
        return ResponseEntity.ok(responseRole)
    }
}