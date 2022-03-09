package cn.sunline.saas.controllers.rbac

import cn.sunline.saas.rbac.modules.Permission
import cn.sunline.saas.rbac.services.PermissionService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("permissions")
class PermissionController {
    data class DTOPermission(val id: Long, val tag: String, val name: String, val remark: String)

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var permissionService: PermissionService

    @GetMapping
    fun getPaged(pageable: Pageable): ResponseEntity<Any> {
        return ResponseEntity.ok(permissionService.getPaged(pageable = pageable))
    }

    @PostMapping
    fun addOne(@RequestBody dtoPermission: DTOPermission): ResponseEntity<DTOPermission> {
        val permission = objectMapper.convertValue<Permission>(dtoPermission)
        val savedPermission = permissionService.save(permission)
        val dtoSavedPermission = objectMapper.convertValue<DTOPermission>(savedPermission)
        return ResponseEntity.ok(dtoSavedPermission)
    }
}