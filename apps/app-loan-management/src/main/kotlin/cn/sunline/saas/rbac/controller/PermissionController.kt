package cn.sunline.saas.rbac.controller

import cn.sunline.saas.rbac.modules.Permission
import cn.sunline.saas.rbac.services.PermissionService
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
@RequestMapping("permissions")
class PermissionController {
    data class DTOPermission(val id: Long, val tag: String, val name: String, val remark: String)

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var permissionService: PermissionService

    @GetMapping
    fun getPaged(pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess> {
        val page = permissionService.getPaged(pageable = pageable)
        return DTOPagedResponseSuccess(page.map { objectMapper.convertValue<DTOPermission>(it)}).response()
    }


    @GetMapping("all")
    fun getAll(): ResponseEntity<DTOPagedResponseSuccess> {
        val page = permissionService.getPaged(pageable = Pageable.unpaged())
        return DTOPagedResponseSuccess(page.map { objectMapper.convertValue<DTOPermission>(it)}).response()
    }

    @PostMapping
    fun addOne(@RequestBody dtoPermission: DTOPermission): ResponseEntity<DTOResponseSuccess<DTOPermission>> {
        val permission = objectMapper.convertValue<Permission>(dtoPermission)
        val savedPermission = permissionService.save(permission)
        val dtoSavedPermission = objectMapper.convertValue<DTOPermission>(savedPermission)
        return DTOResponseSuccess(dtoPermission).response()
    }

}