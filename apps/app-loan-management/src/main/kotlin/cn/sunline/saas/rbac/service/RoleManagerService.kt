package cn.sunline.saas.rbac.service

import cn.sunline.saas.exceptions.ManagementException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.rbac.dto.DTORoleChange
import cn.sunline.saas.rbac.dto.DTORoleView
import cn.sunline.saas.rbac.modules.Role
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import com.fasterxml.jackson.module.kotlin.convertValue
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import javax.persistence.criteria.Predicate

interface RoleManagerService {

    fun getPaged(pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess>

    fun getAll(): ResponseEntity<DTOPagedResponseSuccess>

    fun addOne(dtoRole: DTORoleChange): ResponseEntity<DTOResponseSuccess<DTORoleView>>

    fun updateOne(id: Long, dtoRole: DTORoleChange): ResponseEntity<DTOResponseSuccess<DTORoleView>>
}