package cn.sunline.saas.rbac.service

import cn.sunline.saas.exceptions.ManagementException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.rbac.dto.DTOUserAdd
import cn.sunline.saas.rbac.dto.DTOUserChange
import cn.sunline.saas.rbac.dto.DTOUserView
import cn.sunline.saas.rbac.modules.User
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import com.fasterxml.jackson.module.kotlin.convertValue
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import javax.persistence.criteria.Predicate


@Service
interface UserManagerService {

    fun getPaged(pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess>

    fun addOne(dtoUser: DTOUserAdd): ResponseEntity<DTOResponseSuccess<DTOUserView>>

    fun updateOne(id: Long, dtoUser: DTOUserChange): ResponseEntity<DTOResponseSuccess<DTOUserView>>
}