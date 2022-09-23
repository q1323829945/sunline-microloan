package cn.sunline.saas.rbac.service

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.rbac.controller.dto.DTORoleChange
import cn.sunline.saas.rbac.controller.dto.DTORoleView
import cn.sunline.saas.rbac.exception.RoleBusinessException
import cn.sunline.saas.rbac.exception.RoleNotFoundException
import cn.sunline.saas.rbac.modules.Role
import cn.sunline.saas.rbac.services.PermissionService
import cn.sunline.saas.rbac.services.RoleService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service


@Service
class  RoleManagerService  {
    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var permissionService: PermissionService

    fun getPaged(pageable: Pageable): Page<DTORoleView> {
        val page = roleService.getPageWithTenant(pageable = pageable)
        return page.map { objectMapper.convertValue(it) }
    }

    fun getAll(): Page<DTORoleView> {
        val page = roleService.getPageWithTenant(pageable = Pageable.unpaged())
        return page.map { objectMapper.convertValue(it) }
    }

    fun addOne(dtoRole: DTORoleChange): DTORoleView {
        val role = objectMapper.convertValue<Role>(dtoRole)
        val oldRole = roleService.getByName(role.name)
        if(oldRole != null){
            throw RoleBusinessException("This role has already exist",ManagementExceptionCode.DATA_ALREADY_EXIST)
        }
        val savedRole = roleService.save(role)
        return objectMapper.convertValue(savedRole)
    }

    fun updateOne(id: Long, dtoRole: DTORoleChange): DTORoleView {
        val oldRole = roleService.getOne(id)?: throw RoleNotFoundException("Invalid role", ManagementExceptionCode.DATA_NOT_FOUND)
        val newRole = objectMapper.convertValue<Role>(dtoRole)

        if (dtoRole.permissionList.isEmpty()) {
            newRole.permissions.clear()
        } else {
            newRole.permissions = permissionService.getByIds(dtoRole.permissionList).toMutableList()
        }

        val savedRole = roleService.updateOne(oldRole, newRole)
        return objectMapper.convertValue(savedRole)
    }
}