package cn.sunline.saas.rbac.controller

import cn.sunline.saas.exceptions.ManagementException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.rbac.dto.DTOUserAdd
import cn.sunline.saas.rbac.dto.DTOUserChange
import cn.sunline.saas.rbac.dto.DTOUserView
import cn.sunline.saas.rbac.exception.UserNotFoundException
import cn.sunline.saas.rbac.modules.User
import cn.sunline.saas.rbac.service.UserManagerService
import cn.sunline.saas.rbac.services.RoleService
import cn.sunline.saas.rbac.services.UserService
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
@RequestMapping("users")
class UserController {

    @Autowired
    private lateinit var userManagerService: UserManagerService

    @GetMapping
    fun getPaged(pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess> {
        return userManagerService.getPaged(pageable = pageable)
    }

    @PostMapping
    fun addOne(@RequestBody dtoUser: DTOUserAdd): ResponseEntity<DTOResponseSuccess<DTOUserView>> {
        return userManagerService.addOne(dtoUser)
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id: Long, @RequestBody dtoUser: DTOUserChange): ResponseEntity<DTOResponseSuccess<DTOUserView>> {
        return userManagerService.updateOne(id,dtoUser)
    }
}