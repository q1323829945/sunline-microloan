package cn.sunline.saas.rbac.service.impl

import cn.sunline.saas.exceptions.ManagementException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.rbac.dto.DTORoleChange
import cn.sunline.saas.rbac.dto.DTORoleView
import cn.sunline.saas.rbac.exception.RoleNotFoundException
import cn.sunline.saas.rbac.modules.Role
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


class RoleManagerServiceImpl: RoleManagerService {
    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var permissionService: PermissionService

    override fun getPaged(pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess> {
        val page = roleService.getPaged(pageable = pageable)
        return DTOPagedResponseSuccess(page.map { objectMapper.convertValue<DTORoleView>(it)}).response()
    }

    override fun getAll(): ResponseEntity<DTOPagedResponseSuccess> {
        val page = roleService.getPaged(pageable = Pageable.unpaged())
        return DTOPagedResponseSuccess(page.map { objectMapper.convertValue<DTORoleView>(it)}).response()
    }

    override fun addOne(dtoRole: DTORoleChange): ResponseEntity<DTOResponseSuccess<DTORoleView>> {
        val role = objectMapper.convertValue<Role>(dtoRole)
        val oldRole = roleService.getPaged({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<String>("name"), role.name))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.ofSize(1)).firstOrNull()
        if(oldRole != null){
            throw ManagementException(ManagementExceptionCode.DATA_ALREADY_EXIST)
        }
        val savedRole = roleService.save(role)
        val responseRole = objectMapper.convertValue<DTORoleView>(savedRole)
        return DTOResponseSuccess(responseRole).response()
    }

    override fun updateOne(id: Long, dtoRole: DTORoleChange): ResponseEntity<DTOResponseSuccess<DTORoleView>> {
        val oldRole = roleService.getOne(id)?: throw RoleNotFoundException("Invalid role", ManagementExceptionCode.DATA_NOT_FOUND)
        val newRole = objectMapper.convertValue<Role>(dtoRole)

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